package com.ihyas.soharamkarubar.ui.quran.quranbookview

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.managers.InternetManager
import java.io.*
import java.text.DecimalFormat

class DownloadBookViewRecitation(var currentSurah: Int, var con: Context) {

    private var reciter: String? = null
    private var file: DataBaseFile? = null
    private var surahList: List<String>? = null

    init {
        file = DataBaseFile(con)
        reciter = file?.getStringData(DataBaseFile.recitorAudioKey, "sudais")
        getSurahFromFile()
    }

    private fun getSurahFromFile() {
        val text =
            DataBaseFile.removeCharacter(DataBaseFile.LoadData("Quran/surahInformation.txt", con))
                .split("\n").toTypedArray()
        surahList = listOf(*text)
    }


    var currentDownloadedFilePath = ""
    lateinit var FileDownloaded: (String) -> Unit
    fun download(fileExist: (filePath: String) -> Unit) {
        FileDownloaded = fileExist
        val f = File(DataBaseFile.getFilePath("MuslimGuidePro$reciter", setSurah() + ".mp3", con))
        currentDownloadedFilePath = f.absolutePath
        if (f.exists()) {
            fileExist(f.absolutePath)
        } else {
            if (InternetManager.checkForInternet(con)) {
                downloadAudioFile()
            } else {
                return
            }
        }

    }

    private var enqueue: Long = -1
    var title = ""
    private fun downloadAudioFile() {
        val server_path = "http://www.muslimguidepro.com/data/"
        val url = server_path + reciter + "/" + setSurah() + ".mp3"
        title =
            "Surah " + (surahList?.get(currentSurah - 1)?.split(",")?.toTypedArray()?.get(2) ?: "")
        try {
            if (enqueue <= 0) {
                val dManager = con.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val request = DownloadManager.Request(Uri.parse(url))
                request.setTitle(title)
                request.setDescription(con.resources.getString(R.string.downloading_downloading))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                enqueue = dManager.enqueue(request)
                hideProgress()
                // now setup cursor so we can get the download
                // status in updateProgress via the observer.
                setupCursor()
//                if (null == lastErrorMsg) {
                startObserver()
//                } else {
//                    cancelDownload()
//                }
                showDownloadProgress(con.resources.getString(R.string.downloading_downloading) + " " + title)
            }
        } catch (e: Exception) {
            Log.d("Exception in direct RubotoCore download: ", e.toString())
        }
    }

    private var loadingDialog: ProgressDialog? = null
    private var dialog: Dialog? = null
    private fun hideProgress() {
        try {
            if (loadingDialog != null) {
                Log.d("Hide progress", "Hide Progress")
                loadingDialog?.dismiss()
                loadingDialog = null
            }
            if (dialog != null) {
                dialog?.dismiss()
                dialog = null
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private var mCursor: Cursor? = null
    private var mStatusIndex = 0
    private var mReasonIndex = 0
    private var mBytesSoFarIndex = 0
    private var mTotalBytesIndex = 0
    private fun setupCursor() {

        val downloadManager = con.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        mCursor = downloadManager.query(DownloadManager.Query().setFilterById(enqueue))
        mCursor?.let { c ->
            if (mCursor?.isClosed == false && mCursor?.count == 1) {
                mCursor?.moveToFirst()
                mStatusIndex = c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                mReasonIndex = c.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)
                mBytesSoFarIndex =
                    c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                mTotalBytesIndex = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                c.getInt(mStatusIndex)
                // Log.i(TAG, "download status=" + getDMStatusText(status));
            } else {

            }
        }


    }

    private var mDownloadObserver: DownloadContentObserver? = null
    private fun startObserver() {
        mDownloadObserver = DownloadContentObserver()
        mCursor?.registerContentObserver(mDownloadObserver)
    }


    private fun showDownloadProgress(message: String) {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(con, R.style.MyAlertDialogStyle2)
            loadingDialog?.setMessage(message)
            loadingDialog?.setButton(con.getString(R.string.cancel)) { dialog1: DialogInterface?, which: Int ->
                cancelDownload()

            }
            loadingDialog?.setProgressStyle(R.style.MyAlertDialogStyle2)
            loadingDialog?.isIndeterminate = false
            loadingDialog?.max = 100
            loadingDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            loadingDialog?.setCancelable(false)
            loadingDialog?.setCanceledOnTouchOutside(false)
            loadingDialog?.setOnCancelListener { dialog: DialogInterface? -> }
            loadingDialog?.show()
        } else {
            loadingDialog?.setMessage(message)
        }
    }

    private fun updateProgress() {
        // this logs WAY too much... // Log.i(TAG, "updateProgress");
        mCursor?.let { myCursor ->

            if (myCursor.requery() && myCursor.count == 1) {
                myCursor.moveToFirst()
                val status = myCursor.getInt(mStatusIndex)
                val totalBytes = myCursor.getLong(mTotalBytesIndex)
                loadingDialog?.setMessage(
                    """${con.resources.getString(R.string.downloading_downloading)} $title
${convertKBToMB(totalBytes.toFloat())}"""
                )
                if (status == DownloadManager.STATUS_RUNNING) {
                    val bytesSoFar = myCursor.getLong(mBytesSoFarIndex)
                    updateStatusTextAndProgressViews(bytesSoFar, totalBytes)
                } else {
                    // not running so where are we?
                    val statusText = StringBuilder()
                    statusText.append(getDMStatusText(status))
                    // if (null == lastErrorMsg) {
                    // known status, deal with reason
                    val reason = myCursor.getInt(mReasonIndex)
                    when (status) {
                        DownloadManager.STATUS_FAILED ->                             // append reason text
                            statusText.append(getDMReasonText(reason))
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            //long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                            val dm =
                                con.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            storeDownload(dm, enqueue)
                            hideProgress()
                            shutdownCursor()
                            statusText.append("! ")
                            statusText.append(' ')
                            statusText.append(totalBytes)
                            Toast.makeText(
                                con,
                                con.resources.getString(R.string.downloading_complete),
                                Toast.LENGTH_SHORT
                            ).show()
                            FileDownloaded(currentDownloadedFilePath)
                        }
                        else -> statusText.append(getDMReasonText(reason))
                    }
//                } else {
//                    // unknown status, shutdown cursor anyway
//                    shutdownCursor()
//                }
                }
            } else {
            }
        }

    }

    private fun updateStatusTextAndProgressViews(bytesSoFar: Long, totalBytes: Long) {
        try {
            val progress = (if (bytesSoFar >= 0) bytesSoFar else 0).toInt()
            val max = (if (totalBytes >= 0) totalBytes else 0).toInt()
            val percentage = (bytesSoFar * 100 / totalBytes).toInt()
            val sb = progress.toString() +
                    " / " +
                    max +
                    " bytes"

            // mDownloadStatusText.setText(sb.toString());
            updateProgressView(percentage)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun updateProgressView(progress: Int) {
        loadingDialog?.progress = progress
    }


    @SuppressLint("WrongConstant", "ShowToast")
    private fun storeDownload(dm: DownloadManager, downloadId: Long) {
        try {
            val st = DataBaseFile.getFilePath(
                "MuslimGuidePro$reciter",
                setSurah() + ".mp3",
                con
            ) // Internal Memory Path....
            val file = dm.openDownloadedFile(downloadId)
            val fileStream: InputStream = FileInputStream(file.fileDescriptor)
            val output: OutputStream = FileOutputStream(st)
            val buffer = ByteArray(1024)
            var length: Int
            while (fileStream.read(buffer).also { length = it } != -1) {
                output.write(buffer, 0, length)
            }
            output.flush()
            fileStream.close()
            output.close()
            dm.remove(downloadId)
            enqueue = 0
        } catch (ioe: IOException) {
            //dialog.dismiss();
            val fi =
                File(DataBaseFile.getFilePath("MuslimGuidePro$reciter", setSurah() + ".mp3", con))
            fi.delete()
            Toast.makeText(
                con,
                """
                ${con.resources.getString(R.string.downloading_failed)}:
                $ioe
                """.trimIndent(), 1
            ).show()
        }
    }

    private fun getDMStatusText(status: Int): String? {
        var statusText = ""
        when (status) {
            DownloadManager.STATUS_RUNNING -> statusText = "Running"
            DownloadManager.STATUS_FAILED -> statusText = "Failed"
            DownloadManager.STATUS_PAUSED -> statusText = "Paused"
            DownloadManager.STATUS_PENDING -> statusText = "Pending"
            DownloadManager.STATUS_SUCCESSFUL -> statusText = "Successful"
            else -> {
            }
        }
        return statusText
    }

    private fun getDMReasonText(reason: Int): String? {
        val reasonText = StringBuilder(" reason=")
        when (reason) {
            DownloadManager.ERROR_UNKNOWN -> reasonText.append("ERROR_UNKNOWN")
            DownloadManager.ERROR_FILE_ALREADY_EXISTS -> reasonText.append("ERROR_FILE_ALREADY_EXISTS")
            DownloadManager.ERROR_DEVICE_NOT_FOUND -> reasonText.append("ERROR_DEVICE_NOT_FOUND")
            DownloadManager.ERROR_INSUFFICIENT_SPACE -> reasonText.append("ERROR_INSUFFICIENT_SPACE")
            DownloadManager.ERROR_HTTP_DATA_ERROR -> reasonText.append("ERROR_HTTP_DATA_ERROR")
            DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> reasonText.append("ERROR_UNHANDLED_HTTP_CODE")
            DownloadManager.PAUSED_WAITING_FOR_NETWORK -> reasonText.append("PAUSED_WAITING_FOR_NETWORK")
            DownloadManager.PAUSED_QUEUED_FOR_WIFI -> reasonText.append("PAUSED_QUEUED_FOR_WIFI")
            DownloadManager.PAUSED_WAITING_TO_RETRY -> reasonText.append("PAUSED_WAITING_TO_RETRY")
            DownloadManager.PAUSED_UNKNOWN -> reasonText.append("PAUSED_UNKNOWN")
            DownloadManager.ERROR_CANNOT_RESUME -> reasonText.append("ERROR_CANNOT_RESUME")
            404 -> reasonText.append("HTTP 404 Error Not Found!")
            else -> Log.e("gg", "Unknown reason code=$reason")
        }
        return reasonText.toString()
    }

    private fun cancelDownload() {

        val downloadManager = con.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.remove(enqueue)
        shutdownCursor()
    }

    private fun shutdownCursor() {
        if (null != mCursor) {
            mCursor?.unregisterContentObserver(mDownloadObserver)
            mCursor?.close()
            mCursor = null
        }
    }

    private fun setSurah(): String {
        return if (currentSurah > 99) "" + currentSurah else if (currentSurah > 9) "0$currentSurah" else "00$currentSurah"
    }

    private fun convertKBToMB(value: Float): String? {
        return if (value > 0) {
            if (value / 1024 > 1024) formatString(value / 1024 / 1024) + con.resources.getString(R.string.downloading_mb) else formatString(
                value / 1024
            ) + con.resources.getString(R.string.downloading_kb)
        } else {
            con.getString(R.string.connecting)
        }
    }

    private fun formatString(value: Float): String? {
        val form = DecimalFormat("0.00")
        return form.format(value.toDouble())
    }

    inner class DownloadContentObserver() :
        ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            updateProgress()
        }
    }

}