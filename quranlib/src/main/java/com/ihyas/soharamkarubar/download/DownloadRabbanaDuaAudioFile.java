package com.ihyas.soharamkarubar.download;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkarubar.ui.duas.rabbanadua.RabbanaDuaFragment;
import com.ihyas.soharamkarubar.utils.Constants;
import com.ihyas.soharamkarubar.utils.DataBaseFile;
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

public class DownloadRabbanaDuaAudioFile {

    private final Activity activity;
    private ProgressBar pb;
    private Dialog dialog;
    DataBaseFile file;
    private float downloadedSize = 0;
    private float totalSize = 0;
    private TextView cur_val;
    private final RabbanaDuaFragment obj;

    private static final String TAG = "DownloadDuaAudioFile";

    public DownloadRabbanaDuaAudioFile(Activity activity, RabbanaDuaFragment rabbanaDuaFragment) {
        this.activity = activity;
        obj = rabbanaDuaFragment;
        file = new DataBaseFile(obj.getContext());
    }

    public String convertKBToMB(float value) {
        assert obj != null;
        if ((value / 1024) > 1024)
            return formateString((value / 1024) / 1024) + obj.getContext().getResources().getString(R.string.downloading_mb);
        else
            return formateString((value / 1024)) + obj.getContext().getResources().getString(R.string.downloading_kb);
    }


    public String formateString(float value) {
        DecimalFormat form = new DecimalFormat("0.00");
        return form.format(value);
    }


    public void download(String file) {
        showProgress();
        downloadFileFromServer(file);
    }


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void showProgress() {
        dialog = new Dialog(obj.getContext());
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle(obj.getContext().getResources().getString(R.string.downloading_download_progress));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView text = dialog.findViewById(R.id.tv1);
        text.setText(obj.getContext().getResources().getString(R.string.qurantp_download) + " " + (obj.currentDua + 1) + " " + obj.getContext().getResources().getString(R.string.downloading_rabbana_dua_audio));
        cur_val = dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText(obj.getContext().getResources().getString(R.string.downloading_start_downloading));
        dialog.show();

        pb = dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(obj.getContext().getResources().getDrawable(R.drawable.green_progress));
    }


    @SuppressLint("SetTextI18n")
    public void downloadFileFromServer(String downloadUrl) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        final String[] message2 = {""};

        executor.execute(() -> {
            //Background work here
            int count;

            try {
                URL url = new URL(downloadUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                InputStream input = new BufferedInputStream(url.openStream());
                String fileDirectory = "MuslimGuideProRabbana";

                File f = new File(DataBaseFile.getFilePath(fileDirectory, "rabbanaDua" + (RabbanaDuaFragment.currentDua + 3) + ".mp3", obj.getContext()));
                OutputStream output = new FileOutputStream(f.getAbsoluteFile());
                totalSize = urlConnection.getContentLength();
                byte[] data = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                    downloadedSize += count;
                    // update the progressbar //
                    activity.runOnUiThread(() -> {
                        pb.setProgress((int) downloadedSize);
                        pb.setMax((int) totalSize);
                        float per = (downloadedSize / totalSize) * 100;
                        cur_val.setText(obj.getContext().getResources().getString(R.string.qurantp_download) + " " + convertKBToMB(downloadedSize) + "/ " + convertKBToMB(totalSize) + "(" + (int) per + "%)");
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
                if (message2[0].equals(Constants.NetworkConstants.DONE)) {
                    if (dialog != null) dialog.dismiss();
                    //obj.animateToSoundSettings(true);
                    obj.playAudio();
                }
            });
        });
    }

}
