package com.ihyas.soharamkarubar.download

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.ui.duas.rabbanadua.RabbanaDuaFragment
import com.ihyas.soharamkarubar.ui.favoriteduas.fragments.RabbanaDuaFavoriteFragment
import com.ihyas.soharamkarubar.utils.Constants
import com.ihyas.soharamkarubar.utils.DataBaseFile
import java.io.*
import java.lang.Exception
import java.net.URL
import java.text.DecimalFormat
import java.util.concurrent.Executors

class DownloadFavoriteRabbanaDuaAudioFile(
    private val activity: Activity,
    private val obj: RabbanaDuaFavoriteFragment?
) {
    private var pb: ProgressBar? = null
    private var dialog: Dialog? = null
    var file: DataBaseFile
    private var downloadedSize = 0f
    private var totalSize = 0f
    private var cur_val: TextView? = null
    fun convertKBToMB(value: Float): String {
        assert(obj != null)
        return if (value / 1024 > 1024) formateString(value / 1024 / 1024) + (obj?.requireContext()
            ?.resources?.getString(R.string.downloading_mb)) else formateString(value / 1024) + (obj?.requireContext()
            ?.resources?.getString(R.string.downloading_kb))
    }

    fun formateString(value: Float): String {
        val form = DecimalFormat("0.00")
        return form.format(value.toDouble())
    }

    fun download(file: String?) {
        showProgress()
        downloadFileFromServer(file)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    fun showProgress() {
        dialog = obj?.requireContext()?.let { Dialog(it) }
        dialog?.setContentView(R.layout.myprogressdialog)
        if (obj != null) {
            dialog?.setTitle(obj.requireContext().resources.getString(R.string.downloading_download_progress))
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        val text = dialog?.findViewById<TextView>(R.id.tv1)
        if (text != null) {
            if (obj != null) {
                text.text =
                    obj.requireContext().resources.getString(R.string.qurantp_download) + " " + (RabbanaDuaFragment.currentDua + 1) + " " + obj.requireContext()
                        .resources.getString(R.string.downloading_rabbana_dua_audio)
            }
        }
        cur_val = dialog?.findViewById(R.id.cur_pg_tv)
        if (obj != null) {
            cur_val?.setText(obj.requireContext().resources.getString(R.string.downloading_start_downloading))
        }
        dialog?.show()
        pb = dialog?.findViewById(R.id.progress_bar)
        pb?.setProgress(0)
        if (obj != null) {
            pb?.setProgressDrawable(obj.requireContext().resources.getDrawable(R.drawable.green_progress))
        }
    }

    @SuppressLint("SetTextI18n")
    fun downloadFileFromServer(downloadUrl: String?) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        val message2 = arrayOf("")
        executor.execute {

            //Background work here
            var count: Int
            try {
                val url = URL(downloadUrl)
                val urlConnection = url.openConnection()
                urlConnection.connect()
                val input: InputStream = BufferedInputStream(url.openStream())
                val fileDirectory = "MuslimGuideProRabbana"
                val f = File(
                    DataBaseFile.getFilePath(
                        fileDirectory,
                        "rabbanaDua" + (RabbanaDuaFragment.currentDua + 3) + ".mp3",
                        obj?.context
                    )
                )
                val output: OutputStream = FileOutputStream(f.absoluteFile)
                totalSize = urlConnection.contentLength.toFloat()
                val data = ByteArray(1024)
                while (input.read(data).also { count = it } != -1) {
                    output.write(data, 0, count)
                    downloadedSize += count.toFloat()
                    // update the progressbar //
                    activity.runOnUiThread {
                        pb?.progress = downloadedSize.toInt()
                        pb?.max = totalSize.toInt()
                        val per = downloadedSize / totalSize * 100
                        if (obj != null) {
                            cur_val?.text =
                                obj.requireContext().resources.getString(R.string.qurantp_download) + " " + convertKBToMB(
                                    downloadedSize
                                ) + "/ " + convertKBToMB(totalSize) + "(" + per.toInt() + "%)"
                        }
                    }
                }
                output.flush()
                output.close()
                input.close()
                message2[0] =
                    Constants.NetworkConstants.DONE
            } catch (e: Exception) {
                message2[0] =
                    Constants.NetworkConstants.NETWORK_EXCEPTION
            }
            handler.post {
                //UI Thread work here
                if (message2[0] == Constants.NetworkConstants.DONE) {
                    if (dialog != null) dialog?.dismiss()
                    //obj?.animateToSoundSettings(true)
                    if (obj != null) {
                        obj.playAudio()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "DownloadDuaAudioFile"
    }

    init {
        file = DataBaseFile(obj?.context)
    }
}