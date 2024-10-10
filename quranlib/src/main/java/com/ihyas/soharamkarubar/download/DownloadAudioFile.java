package com.ihyas.soharamkarubar.download;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper;
import com.ihyas.soharamkarubar.ui.quran.QuranDetailFragment;
import com.ihyas.soharamkarubar.utils.DataBaseFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class DownloadAudioFile {

    private static final String TAG = "DownloadAudioFile";

    private String lastErrorMsg = null;

    private final Activity con;
    private final MediaPlayer mPlayer = new MediaPlayer();
    private Dialog dialog;
    private ProgressDialog loadingDialog;

    private final String reciter;
    private long enqueue = -1;

    @SuppressWarnings("unused")
    private BroadcastReceiver receiver;
    private final int currentSurah;
    private final DataBaseFile file;
    static float downloadedSize = 0, totalSize = 0;

    private final QuranDetailFragment displayObj;
    private List<String> surahList;

    @SuppressWarnings("unused")
    private String downloadedMb;
    private String title;

    private Cursor mCursor = null;
    private DownloadContentObserver mDownloadObserver = null;
    private int mStatusIndex = 0;
    private int mReasonIndex = 0;
    private int mBytesSoFarIndex = 0;
    private int mTotalBytesIndex = 0;


    public DownloadAudioFile(Activity con, QuranDetailFragment obj) {
        this.con = con;
        file = new DataBaseFile(con);
        totalSize = 0;
        downloadedSize = 0;
        currentSurah = LastSurahAndAyahHelper.getSelectedSurah(con) + 1;
        reciter = file.getStringData(DataBaseFile.recitorAudioKey, "sudais");
        getSurahFromFile();
        displayObj = obj;
    }

    private String convertKBToMB(float value) {
        if (value > 0) {
            if ((value / 1024) > 1024)
                return formatString((value / 1024) / 1024) + con.getResources().getString(R.string.downloading_mb);
            else
                return formatString((value / 1024)) + con.getResources().getString(R.string.downloading_kb);
        } else {
            return con.getString(R.string.connecting);
        }
    }


    private String formatString(float value) {
        DecimalFormat form = new DecimalFormat("0.00");
        return form.format(value);
    }

    private void getSurahFromFile() {
        String[] text = DataBaseFile.removeCharacter(DataBaseFile.LoadData("Quran/surahInformation.txt", con)).split("\n");
        surahList = Arrays.asList(text);
    }

    private void playAudio(Context context) {
        File f = new File(DataBaseFile.getFilePath("MuslimGuidePro" + reciter, setSurah() + ".mp3", con));

        try {
            if (!mPlayer.isPlaying()) {

                mPlayer.setDataSource(f.getAbsolutePath());
                mPlayer.setOnCompletionListener(mp -> {

                    if (file.getIntData(DataBaseFile.nextSurahStartKey, 0) == 1) {
                        displayObj.playAgainSurah(context);
                    } else if (file.getIntData(DataBaseFile.nextSurahStartKey, 0) == 2) {
                        displayObj.showNextSurah(context, true);
                    } else {
                        displayObj.releasePlayer();
                    }

                });


                mPlayer.prepare();
                mPlayer.start();
                if (mInde >= 0)
                    mPlayer.seekTo(Integer.parseInt(QuranDetailFragment.namePointer.get(mInde)));

                QuranDetailFragment.binding.include28.audioSeekBar.setMax(mPlayer.getDuration());
                QuranDetailFragment.binding.include28.playAudio.setImageResource(R.drawable.ic_pause);
            }
        } catch (Exception e) {
            f.delete();
            e.printStackTrace();
        }
    }


    int mInde = 0;

    public MediaPlayer download(Context context, int mIndex) {
        mInde = mIndex;
        File f = new File(DataBaseFile.getFilePath("MuslimGuidePro" + reciter, setSurah() + ".mp3", con));
        if (f.exists()) {
            if (!mPlayer.isPlaying()) {
                displayObj.startTimer();
                playAudio(context);
            } else

                mPlayer.pause();
        } else {
            if (isNetworkAvailable()) {
                downloadAudioFile();
            } else {
                return null;
            }

        }
        return mPlayer;
    }


    @SuppressLint("LongLogTag")
    private void downloadAudioFile() {
        String server_path = con.getResources().getString(R.string.server_link);
        String url = server_path + reciter + "/" + setSurah() + ".mp3";
        if (url.contains("Sudais")) {
            url = url.replace("Sudais", "sudais");
        }
        else if (url.contains("Alfasy")) {
            url = url.replace("Alfasy", "alfasy");
        }
        else if (url.contains("AlGhamdi")) {
            url = url.replace("AlGhamdi", "alghamidi");
        }
        Log.d("checkfurl", url);
        title = "Surah " + surahList.get(currentSurah - 1).split(",")[2];
        Log.d("gg", title + " " + enqueue);
        try {
            if (enqueue <= 0) {//
                DownloadManager dManager = (DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);

                Request request = new Request(Uri.parse(url));
                request.setTitle(title);
                request.setDescription(con.getResources().getString(R.string.downloading_downloading));
                request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
                enqueue = dManager.enqueue(request);
                hideProgress();
                // now setup cursor so we can get the download
                // status in updateProgress via the observer.
                setupCursor();
                if (null == lastErrorMsg) {
                    startObserver();
                } else {
                    cancelDownload();
                }
                showDownloadProgress(this.con.getResources().getString(R.string.downloading_downloading) + " " + title);
            }

        } catch (Exception e) {
            Log.d("Exception in direct RubotoCore download: ", e.toString());
        }
    }

    private void showDownloadProgress(String message) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(con, R.style.MyAlertDialogStyle2);
            loadingDialog.setMessage(message);
            loadingDialog.setButton(con.getString(R.string.cancel), (dialog1, which) -> {
                cancelDownload();
                displayObj.player = null;
            });
            loadingDialog.setProgressStyle(R.style.MyAlertDialogStyle2);

            loadingDialog.setIndeterminate(false);
            loadingDialog.setMax(100);
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setOnCancelListener(dialog -> {

            });

            loadingDialog.show();

            loadingDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setTextColor(con.getResources().getColor(R.color.onPrimary1));
            loadingDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setTextColor(con.getResources().getColor(R.color.onPrimary1));

        } else {
            loadingDialog.setMessage(message);
        }
    }

    private void hideProgress() {
        try {
            if (loadingDialog != null) {
                Log.d("Hide progress", "Hide Progress");
                loadingDialog.dismiss();
                loadingDialog = null;
            }

            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @SuppressLint({"WrongConstant", "ShowToast"})
    private void storeDownload(DownloadManager dm, long downloadId) {
        try {
            String st = DataBaseFile.getFilePath("MuslimGuidePro" + reciter, setSurah() + ".mp3", con);// Internal Memory Path....
            android.os.ParcelFileDescriptor file = dm.openDownloadedFile(downloadId);
            java.io.InputStream fileStream = new java.io.FileInputStream(file.getFileDescriptor());
            OutputStream output = new FileOutputStream(st);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.flush();
            fileStream.close();
            output.close();
            dm.remove(downloadId);
            enqueue = 0;
        } catch (java.io.IOException ioe) {
            Log.e(TAG, ioe.toString());
            //dialog.dismiss();
            File fi = new File(DataBaseFile.getFilePath("MuslimGuidePro" + reciter, setSurah() + ".mp3", con));
            //noinspection ResultOfMethodCallIgnored
            fi.delete();
            Toast.makeText(DownloadAudioFile.this.con, this.con.getResources().getString(R.string.downloading_failed) + ":\n" + ioe.toString(), 1).show();
        }
    }

    private String setSurah() {
        if (currentSurah > 99)
            return "" + currentSurah;
        else if (currentSurah > 9)
            return "0" + currentSurah;
        else
            return "00" + currentSurah;
    }

    private void setupCursor() {
        Log.i(TAG, "setupCursor");
        lastErrorMsg = null;

        DownloadManager downloadManager = (DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);

        mCursor = downloadManager.query(new DownloadManager.Query().setFilterById(enqueue));

        if (mCursor != null && !mCursor.isClosed() && mCursor.getCount() == 1) {
            mCursor.moveToFirst();

            mStatusIndex = mCursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
            mReasonIndex = mCursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON);
            mBytesSoFarIndex = mCursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            mTotalBytesIndex = mCursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

            mCursor.getInt(mStatusIndex);
            // Log.i(TAG, "download status=" + getDMStatusText(status));
        } else {
            lastErrorMsg = "Cursor is in an unexpected state.";
        }

        if (null != lastErrorMsg) {
            Log.w(TAG, lastErrorMsg);
        }
    }

    private void startObserver() {
        Log.i(TAG, "setupObserver");
        mDownloadObserver = new DownloadContentObserver();
        mCursor.registerContentObserver(mDownloadObserver);
    }

    private void cancelDownload() {
        displayObj.downloadCanceled();
        DownloadManager downloadManager = (DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.remove(enqueue);
        shutdownCursor();
    }


    private void shutdownCursor() {
        Log.i(TAG, "shutdownCursor");
        if (null != mCursor) {
            mCursor.unregisterContentObserver(mDownloadObserver);
            mCursor.close();
            mCursor = null;
        }
    }

    @SuppressLint({"WrongConstant", "ShowToast"})
    private void updateProgress() {
        // this logs WAY too much... // Log.i(TAG, "updateProgress");
        lastErrorMsg = null;

        if (mCursor == null)
            return;

        if (mCursor.requery() && mCursor.getCount() == 1) {
            mCursor.moveToFirst();

            int status = mCursor.getInt(mStatusIndex);
            long totalBytes = mCursor.getLong(mTotalBytesIndex);
            loadingDialog.setMessage(this.con.getResources().getString(R.string.downloading_downloading) + " " + title + "\n" + convertKBToMB(totalBytes));

            if (status == DownloadManager.STATUS_RUNNING) {
                long bytesSoFar = mCursor.getLong(mBytesSoFarIndex);
                updateStatusTextAndProgressViews(bytesSoFar, totalBytes);
            } else {
                // not running so where are we?
                StringBuilder statusText = new StringBuilder();

                statusText.append(getDMStatusText(status));

                if (null == lastErrorMsg) {
                    // known status, deal with reason
                    int reason = mCursor.getInt(mReasonIndex);

                    // updates for view
                    switch (status) {
                        case DownloadManager.STATUS_FAILED:
                            //  mCurrentState = State.DELETE;
                            // mDownloadUriButton.setText(getStateString());
                            // fall through
                        default:
                            // append reason text
                            statusText.append(getDMReasonText(reason));
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            //long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                            DownloadManager dm = (DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);
                            storeDownload(dm, enqueue);
                            hideProgress();
                            shutdownCursor();
                            if (!QuranDetailFragment.isStop)
                                playAudio(con);
                            displayObj.startTimer();
                            statusText.append("! ");
                            statusText.append(' ');
                            statusText.append(totalBytes);
                            Toast.makeText(DownloadAudioFile.this.con, this.con.getResources().getString(R.string.downloading_complete), 1).show();
                            break;
                    }

                    Log.i(TAG, statusText.toString());
                } else {
                    // unknown status, shutdown cursor anyway
                    shutdownCursor();
                }
            }
        } else {
            lastErrorMsg = "Cursor is closed.";
        }

        if (null != lastErrorMsg) {
            Log.w(TAG, lastErrorMsg);
        }

    }

    private void updateStatusTextAndProgressViews(long bytesSoFar, long totalBytes) {
        try {
            int progress = (int) (bytesSoFar >= 0 ? bytesSoFar : 0);
            int max = (int) (totalBytes >= 0 ? totalBytes : 0);

            int percentage = (int) ((bytesSoFar * 100) / totalBytes);

            String sb = String.valueOf(progress) +
                    " / " +
                    max +
                    " bytes";
            Log.i(TAG, sb);
            // mDownloadStatusText.setText(sb.toString());

            updateProgressView(percentage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void updateProgressView(int progress) {
        loadingDialog.setProgress(progress);
    }

    private String getDMStatusText(int status) {
        String statusText = "";

        switch (status) {
            case DownloadManager.STATUS_RUNNING:
                statusText = "Running";
                break;
            case DownloadManager.STATUS_FAILED:
                statusText = "Failed";
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "Paused";
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "Pending";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "Successful";
                break;
            default:
                lastErrorMsg = "Unexpected download status=" + status;
                break;
        }

        return (statusText);
    }

    private String getDMReasonText(int reason) {
        StringBuilder reasonText = new StringBuilder(" reason=");

        switch (reason) {
            case DownloadManager.ERROR_UNKNOWN:
                reasonText.append("ERROR_UNKNOWN");
                break;
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                reasonText.append("ERROR_FILE_ALREADY_EXISTS");
                break;
            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                reasonText.append("ERROR_DEVICE_NOT_FOUND");
                break;
            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                reasonText.append("ERROR_INSUFFICIENT_SPACE");
                break;
            case DownloadManager.ERROR_HTTP_DATA_ERROR:
                reasonText.append("ERROR_HTTP_DATA_ERROR");
                break;
            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                reasonText.append("ERROR_UNHANDLED_HTTP_CODE");
                break;
            case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                reasonText.append("PAUSED_WAITING_FOR_NETWORK");
                break;
            case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                reasonText.append("PAUSED_QUEUED_FOR_WIFI");
                break;
            case DownloadManager.PAUSED_WAITING_TO_RETRY:
                reasonText.append("PAUSED_WAITING_TO_RETRY");
                break;
            case DownloadManager.PAUSED_UNKNOWN:
                reasonText.append("PAUSED_UNKNOWN");
                break;
            case DownloadManager.ERROR_CANNOT_RESUME:
                reasonText.append("ERROR_CANNOT_RESUME");
                break;
            case 404:
                reasonText.append("HTTP 404 Error Not Found!");
                break;
            default:
                Log.e(TAG, "Unknown reason code=" + reason);
                break;
        }

        return (reasonText.toString());
    }

    /**
     * Observe download status changes and updates.
     */
    private class DownloadContentObserver extends ContentObserver {
        DownloadContentObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        public void onChange(boolean selfChange) {
            updateProgress();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
