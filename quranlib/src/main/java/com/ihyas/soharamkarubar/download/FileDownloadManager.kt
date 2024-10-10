package com.ihyas.soharamkarubar.download

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.utils.enums.Enums
import com.ihyas.soharamkarubar.utils.extensions.ContextExtensions.getScreenWidth
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.managers.InternetManager
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL
import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicBoolean

class FileDownloadManager(
    private var context: Context,
    private var link: String,
    private var lifeCycleScope: LifecycleCoroutineScope,
    private var filePath: String,
    private var fileNameDisplayable: String,
    private var downloadCancel: (error: String, file: File) -> Unit,
    private var downloadComplete: (uri: String) -> Unit,
) {

    private var totalSize = 0f
    private var downloadedSize = 0f
    private var progressDialog = Dialog(context)
    private var currentValue: TextView? = null
    private var currentProgressPercentage: TextView? = null
    private var pb: ProgressBar? = null
    private var abort: AtomicBoolean = AtomicBoolean(false)

    suspend fun download() {
        if (InternetManager.checkForInternet(context)) {
            showProgressDialog()
            val message2 = arrayOf("")
            withContext(Dispatchers.IO) {

                //Background work here
                var count: Int
                try {
                    val urle = URL(link)

                    val connection = urle.openConnection()
                    connection.connect()

                    val input: InputStream = BufferedInputStream(urle.openStream())
                    val output: OutputStream =
                        FileOutputStream(filePath)
                    totalSize = connection.contentLength.toFloat()
                    withContext(Main) {
                        pb?.max = totalSize.toInt()
                    }

                    val data = ByteArray(1024)
                    while (input.read(data).also { count = it } != -1) {
                        if (!abort.get()) {
                            output.write(data, 0, count)
                            downloadedSize += count.toFloat()
                            withContext(Main) {
                                val per = downloadedSize / totalSize * 100
                                pb?.progress = downloadedSize.toInt()
                                currentValue?.text =
                                    convertKBToMB(downloadedSize) + "/ " + convertKBToMB(totalSize)
                                currentProgressPercentage?.text = "${per.toInt()}%"
                            }
                        } else {
                            withContext(Main) {

                                progressDialog.dismiss()
                                downloadCancel(
                                    Enums.downloadErrors.CANCELED_BY_USER.error,
                                    File(filePath)
                                )
                            }


                            break
                        }
                    }
                    if (!abort.get()) {
                        message2[0] = "done"
                        output.flush()
                        output.close()
                        input.close()
                        withContext(Main) {
                            progressDialog.dismiss()
                            downloadComplete(
                                filePath
                            )
                        }
                    } else {
                        withContext(Main) {

                            progressDialog.dismiss()
                            downloadCancel(
                                Enums.downloadErrors.CANCELED_BY_USER.error,
                                File(filePath)
                            )
                        }

                    }

                } catch (e: Exception) {
                    e.message?.let { Log.e("error1", it) }
                    message2[0] = "Server not found please check your network."
                }
                try {
                    withContext(Main) {
                        //UI Thread work here
                        if (message2[0] == "done") {
                            progressDialog.dismiss()
                        } else {
                            progressDialog.dismiss()
                            downloadCancel(Enums.downloadErrors.SERVER_ERROR.error, File(filePath))
                        }
                    }
                } catch (e: Exception) {
                    e.message?.let { Log.d("error2", it) }
                }
            }
        } else {
            downloadCancel(Enums.downloadErrors.NO_INTERNET.error, File(filePath))
        }

    }

    fun showDownloadDialog() {
        if (InternetManager.checkForInternet(context)) {
            val dialog = context.let { Dialog(it) }
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(
                    context.resources.getColor(
                        R.color.transparentColor,
                        null
                    )
                )
            )
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialog_for_download)
            val headingText = dialog.findViewById<TextView>(R.id.textHeading)
            val btnDownload = dialog.findViewById<Button>(R.id.btn_download)
            val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
            headingText.text = context.getString(R.string.tafseer_download_message)

            btnDownload?.setSafeOnClickListener {
                dialog.dismiss()

                lifeCycleScope.launch {
                    download()
                }
            }
            btnCancel?.setSafeOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        } else {
            downloadCancel(Enums.downloadErrors.NO_INTERNET.error, File(filePath))
        }
    }

    private fun showProgressDialog() {
        if (InternetManager.checkForInternet(context)) {
            progressDialog.setContentView(R.layout.new_progress_dialog)
            progressDialog.setTitle(context.resources.getString(R.string.downloading_download_progress))
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.window?.setBackgroundDrawable(
                ColorDrawable(
                    context.resources.getColor(
                        R.color.transparentColor,
                        null
                    )
                )
            )
            progressDialog.window?.setLayout(context.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
            progressDialog.setCancelable(false)
            val textFileName = progressDialog.findViewById<TextView>(R.id.txtFileName)
            val btnCancel = progressDialog.findViewById<Button>(R.id.btnCancelDownloadDialog)

            btnCancel.setSafeOnClickListener {
                abort.set(true)
                progressDialog.dismiss()
            }
            textFileName?.text = fileNameDisplayable
            currentValue = progressDialog.findViewById(R.id.txtProgressCurrent)
            currentProgressPercentage = progressDialog.findViewById(R.id.txtDownloadPercentage)
            currentValue?.text = context.resources.getString(R.string.downloading_start_downloading)
            progressDialog.show()
            pb = progressDialog.findViewById(R.id.progress_bar)
            pb?.progress = 0
        } else {
            downloadCancel(Enums.downloadErrors.NO_INTERNET.error, File(filePath))
        }
    }

    private fun convertKBToMB(value: Float): String? {
        return if (value / 1024 > 1024) formatString(value / 1024 / 1024) + context.resources.getString(
            R.string.downloading_mb
        ) else formatString(
            value / 1024
        ) + context.resources.getString(R.string.downloading_kb)
    }

    private fun formatString(value: Float): String? {
        val form = DecimalFormat("0.00")
        return form.format(value.toDouble())
    }
}