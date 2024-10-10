package com.ihyas.soharamkarubar.download;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkarubar.utils.Constants;
import com.ihyas.soharamkarubar.utils.DataBaseFile;

public class
DownloadKalmaAudio {

    private final Activity con;

    private ProgressBar pb;
    private Dialog dialog;
    private float downloadedSize = 0;
    private float totalSize = 0;
    private TextView cur_val;
    private final int kalmaIndex;
    private final Handler mHandler;


    public DownloadKalmaAudio(Activity con, int category, Handler handler) {
        this.con = con;
        // DataBaseFile file = new DataBaseFile(con);
        this.kalmaIndex = category;
        mHandler = handler;
    }

    private String convertKBToMB(float value) {
        if ((value / 1024) > 1024)
            return formateString((value / 1024) / 1024) + con.getResources().getString(R.string.downloading_mb);
        else
            return formateString((value / 1024)) + con.getResources().getString(R.string.downloading_kb);
    }


    private String formateString(float value) {
        DecimalFormat form = new DecimalFormat("0.00");
        return form.format(value);
    }


    public void download() {
        showProgress();
        downloadFileFromServer();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void showProgress() {
        dialog = new Dialog(this.con , R.style.MyAlertDialogStyle2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle(con.getResources().getString(R.string.downloading_download_progress));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView text = dialog.findViewById(R.id.tv1);
        String dd = (con.getResources().getString(R.string.downloading_downloading)) + " " + (kalmaIndex) + " " + con.getResources().getString(R.string.kalma_audio) + "... ";
        text.setText(dd);
        cur_val = dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText(con.getResources().getString(R.string.downloading_start_downloading));
        dialog.show();

        pb = dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(this.con.getResources().getDrawable(R.drawable.green_progress));
    }


    @SuppressLint("SetTextI18n")
    public void downloadFileFromServer() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        final String[] message2 = {""};

        executor.execute(() -> {
            //Background work here
            int count;
            try {
                URL downloadUrl = new URL(con.getResources().getString(R.string.kalma_server_link) + kalmaIndex + ".mp3");
                URLConnection urlConnection = downloadUrl.openConnection();
                urlConnection.connect();
                InputStream input = new BufferedInputStream(downloadUrl.openStream());
                OutputStream output = new FileOutputStream(DataBaseFile.getFilePath("IslamicGuideProKalma", "kalma_" + kalmaIndex + ".mp3", con));
                totalSize = urlConnection.getContentLength();
                byte[] data = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                    downloadedSize += count;
                    // update the progressbar //
                    DownloadKalmaAudio.this.con.runOnUiThread(() -> {
                        pb.setProgress((int) downloadedSize);
                        pb.setMax((int) totalSize);
                        float per = (downloadedSize / totalSize) * 100;
                        String ff = (con.getResources().getString(R.string.qurantp_download)) + " " + convertKBToMB(downloadedSize) + "/ " + convertKBToMB(totalSize) + "(" + (int) per + "%)";
                        cur_val.setText(ff);
                    });
                }

                output.flush();
                output.close();
                input.close();
                message2[0] = Constants.NetworkConstants.DONE;
            } catch (Exception e) {
                message2[0] = Constants.NetworkConstants.NETWORK_EXCEPTION;
            }

            handler.post(() -> {
                //UI Thread work here
                if (message2[0].equals(Constants.NetworkConstants.DONE) && mHandler.getLooper().getThread().isAlive()) {
                    dialog.dismiss();
                    Message message = mHandler.obtainMessage();
                    message.what = 0;
                    mHandler.sendMessage(message);
                    Toast.makeText(DownloadKalmaAudio.this.con, (con.getResources().getString(R.string.downloading_complete)) + "", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    File fi = new File(DataBaseFile.getFilePath("IslamicGuideProKalma", "kalma_" + kalmaIndex + ".mp3", con));
                    //noinspection ResultOfMethodCallIgnored
                    fi.delete();
                    Toast.makeText(DownloadKalmaAudio.this.con, con.getResources().getString(R.string.downloading_failed) + ":\n" + message2[0], Toast.LENGTH_LONG).show();
                }
            });
        });
    }

}
