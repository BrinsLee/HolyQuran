package com.ihyas.soharamkarubar.download;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkarubar.ui.quran.QuranTranslationsFragment;
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

public class DownloadZipFile {

    private final QuranTranslationsFragment con;

    private ProgressBar pb;
    private Dialog dialog;
    DataBaseFile file;
    private float downloadedSize = 0;
    private float totalSize = 0;
    private final String text;
    private TextView cur_val;

    public DownloadZipFile(QuranTranslationsFragment con, String value) {
        this.con = con;
        file = new DataBaseFile(con.getContext());
        text = value;
    }

    private String convertKBToMB(float value) {
        if ((value / 1024) > 1024)
            return formatString((value / 1024) / 1024) + con.getResources().getString(R.string.downloading_mb);
        else
            return formatString((value / 1024)) + con.getResources().getString(R.string.downloading_kb);
    }


    private String formatString(float value) {
        DecimalFormat form = new DecimalFormat("0.00");
        return form.format(value);
    }


    public void download() {
        showProgress();
        downloadFileFromServer();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void showProgress() {
        dialog = new Dialog(con.getActivity());
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle(con.getResources().getString(R.string.downloading_download_progress));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView text1 = dialog.findViewById(R.id.tv1);
        String ttt = con.getResources().getString(R.string.downloading_downloading) + " " + text+ " " + con.getResources().getString(R.string.qurantp_translations);
        text1.setText(ttt);
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

                String server_path = con.getString(R.string.translation_server_link);
                URL urle = new URL(server_path + "" + text + ".zip");
                URLConnection connection = urle.openConnection();
                connection.connect();
                InputStream input = new BufferedInputStream(urle.openStream());
                String st = DataBaseFile.getFilePath("MuslimGuidePro", text + ".zip", con.getContext());
                OutputStream output = new FileOutputStream(DataBaseFile.getFilePath("MuslimGuidePro", text + ".zip", con.getContext()));
                totalSize = connection.getContentLength();
                byte[] data = new byte[1024];

                while ((count = input.read(data)) != -1) {

                    output.write(data, 0, count);
                    downloadedSize += count;
                    // update the progressbar //
                    con.getActivity().runOnUiThread(() -> {
                        pb.setProgress((int) downloadedSize);
                        pb.setMax((int) totalSize);
                        float per = (downloadedSize / totalSize) * 100;
                        cur_val.setText("Downloaded " + convertKBToMB(downloadedSize) + "/ " + convertKBToMB(totalSize) + "(" + (int) per + "%)");
                    });
                }
                message2[0] = "done";
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                message2[0] = "Server not found please check your network.";
            }


            try {
                handler.post(() -> {
                    //UI Thread work here
                    if (message2[0].equals("done")) {
                        if (dialog != null) dialog.dismiss();
                        QuranTranslationsFragment.lastSelectedName = text;
                        setLanguageInDB();

                    } else {
                        if (dialog != null) dialog.dismiss();
                        File fi = new File(DataBaseFile.getFilePath("MuslimGuidePro", file.getStringData(DataBaseFile.quranLanguageKey, "english") + ".zip", con.getContext()));
                        //noinspection ResultOfMethodCallIgnored
                        fi.delete();
                        file.getStringData(DataBaseFile.quranLanguageKey, QuranTranslationsFragment.lastSelectedName);
                        Toast.makeText(con.getContext(), con.getResources().getString(R.string.downloading_failed) + ":\n" + message2[0], Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Log.d("error", e.getMessage());
            }
        });
    }

    private void setLanguageInDB() {
        String lang = file.getStringData(DataBaseFile.quranContainLanguageKey, "");
//        if (lang.contains(text))
//            return;
//        if (lang.equals(""))
//            lang = text;
//        else
//            lang += "," + text;
        file.saveStringData(DataBaseFile.quranLanguageKey, text);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(con.getContext(), con.getResources().getString(R.string.downloading_complete), Toast.LENGTH_LONG).show();
                QuranTranslationsFragment.quranTranslationAdapter.notifyDataSetChanged();
            }
        },450);

    }

}
