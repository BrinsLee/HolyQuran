package com.ihyas.soharamkarubar.ui.quran;

import static com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE;
import static com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.URDU_TRANSLATION_KEY;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.FragmentQuranTranslationsBinding;
import com.ihyas.soharamkarubar.download.DownloadZipFile;
import com.ihyas.soharamkarubar.models.QuranTranslation;
import com.ihyas.soharamkarubar.ui.quran.adapters.QuranTranslationAdapter;
import com.ihyas.soharamkarubar.utils.DataBaseFile;
import com.ihyas.soharamkarubar.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuranTranslationsFragment extends Fragment {

    FragmentQuranTranslationsBinding binding;
    DataBaseFile dataBaseFile;
    Context mContext;
    @SuppressLint("StaticFieldLeak")
    public static QuranTranslationAdapter quranTranslationAdapter;
    public static String lastSelectedName = "";

    public QuranTranslationsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuranTranslationsBinding.inflate(inflater);
        binding.include13.tvTitle.setText(getString(R.string.text_text_and_translations));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUtils();
        loadAds();
        
        
        onClickEvents();
        makeQuranTranslationList();
    }

    private void initUtils() {
        mContext = getContext();
        dataBaseFile = new DataBaseFile(mContext);
    }

    private void loadAds() {
//        AdView mAdView = binding.adView;
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }


    private void onClickEvents() {
        binding.include13.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.preventTwoClick(v);

                Navigation.findNavController(v).popBackStack();
            }
        });
    }

    private void setQuranTranslationRV(List<QuranTranslation> quranTranslationList) {
        quranTranslationAdapter = new QuranTranslationAdapter(getContext(), quranTranslationList, (parent, view, position, id) -> {
            downloadData(quranTranslationList.get(position).getQuranTranslation(), quranTranslationList.get(position).getDownloadKey());

        }, (parent, deleteView, position, id) -> {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle2);
            builder.setTitle(R.string.delete_translation_message);
            builder.setCancelable(true);

            builder.setPositiveButton(getString(R.string.text_ok), (dialog, id12) -> {
                String mPath = DataBaseFile.getFilePath("MuslimGuidePro", "", mContext);
                File zipFile = new File(DataBaseFile.getFilePath("MuslimGuidePro", quranTranslationList.get(position).getDownloadKey() + ".zip", mContext));
                File mFile = new File(mPath + "/" + dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english"));
                mFile.delete();

                deleteRecursive(zipFile);
                deleteLanguage(dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english"), position);
            });
            builder.setNegativeButton(getString(R.string.cancel), (dialog, id1) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(p0 -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.onPrimary1));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.onPrimary1));
            });
            dialog.show();

        }, (parent, checkedView, position, id) -> {
        });

        binding.rvTranslations.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.rvTranslations.setAdapter(quranTranslationAdapter);
    }


    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
        Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_SHORT).show();

    }

    private void deleteLanguage(String lang, int position) {
        String val = dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "");
        if (!val.equals("")) {
            String[] data = val.split(",");
            List<String> list = new LinkedList<>(Arrays.asList(data));
            list.remove(lang);
            StringBuilder te = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (te.toString().equals(""))
                    te = new StringBuilder(list.get(i));
                else
                    te.append(",").append(list.get(i));
            }
            if (QuranTranslationAdapter.CurrentSelectedTranslation == position) {
                Log.d("gg", QuranTranslationAdapter.CurrentSelectedTranslation + " " + position);
                dataBaseFile.saveStringData(DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    quranTranslationAdapter.notifyItemChanged(position);
                }
            }, 450);

        }
    }

    private void makeQuranTranslationList() {
        List<QuranTranslation> quranTranslations = new ArrayList<>();

        quranTranslations.add(new QuranTranslation("Netherlands", getResources().getString(R.string.qurantp_translation_1), R.drawable.netherlands, "dutch"));
        quranTranslations.add(new QuranTranslation("Dhivehi", getResources().getString(R.string.qurantp_translation_2), R.drawable.dhivehi, "divehi"));
        quranTranslations.add(new QuranTranslation("Czech", getResources().getString(R.string.qurantp_translation_3), R.drawable.czech, "czech"));
        quranTranslations.add(new QuranTranslation("Bahasa Indonesia", getResources().getString(R.string.qurantp_translation_4), R.drawable.indonesian, "Indonesian"));
        quranTranslations.add(new QuranTranslation("Bahasa Melayu", getResources().getString(R.string.qurantp_translation_5), R.drawable.malay, "malay"));
        quranTranslations.add(new QuranTranslation("Bengali", getResources().getString(R.string.qurantp_translation_6), R.drawable.bengali, "bengali"));
        quranTranslations.add(new QuranTranslation("Berber/ Amazigh", getResources().getString(R.string.qurantp_translation_7), R.drawable.amazigh, "amazigh"));
        quranTranslations.add(new QuranTranslation("Bosnian", getResources().getString(R.string.qurantp_translation_8), R.drawable.bosnian, "bosnian"));
        quranTranslations.add(new QuranTranslation("English", getResources().getString(R.string.qurantp_translation_9), R.drawable.english, "english"));
        quranTranslations.add(new QuranTranslation("Español", getResources().getString(R.string.qurantp_translation_10), R.drawable.spain, "german"));
        quranTranslations.add(new QuranTranslation("Francias", getResources().getString(R.string.qurantp_translation_11), R.drawable.french, "french"));
        quranTranslations.add(new QuranTranslation("Hausa", getResources().getString(R.string.qurantp_translation_12), R.drawable.hausa, "hausa"));
        quranTranslations.add(new QuranTranslation("Hindi", getResources().getString(R.string.qurantp_translation_13), R.drawable.hindi, "Hindi"));
        quranTranslations.add(new QuranTranslation("Italiano", getResources().getString(R.string.qurantp_translation_14), R.drawable.italian, "italian"));
        quranTranslations.add(new QuranTranslation("Kurdish", getResources().getString(R.string.qurantp_translation_15), R.drawable.kurdish, "kurdish"));
        quranTranslations.add(new QuranTranslation("Malayalam", getResources().getString(R.string.qurantp_translation_16), R.drawable.hindi, "malayalam"));
        quranTranslations.add(new QuranTranslation("Polish", getResources().getString(R.string.qurantp_translation_17), R.drawable.polish, "polish"));
        quranTranslations.add(new QuranTranslation("Norwegian", getResources().getString(R.string.qurantp_translation_18), R.drawable.norwegian, "norwegian"));
        quranTranslations.add(new QuranTranslation("Persian/Farsi", getResources().getString(R.string.qurantp_translation_19), R.drawable.persian, "persian"));
        quranTranslations.add(new QuranTranslation("Portuges", getResources().getString(R.string.qurantp_translation_20), R.drawable.portuguese, "Portuguese"));
        quranTranslations.add(new QuranTranslation("Pусский", getResources().getString(R.string.qurantp_translation_21), R.drawable.russian, "Russian"));
        quranTranslations.add(new QuranTranslation("Albanian", getResources().getString(R.string.qurantp_translation_22), R.drawable.albanian, "albanian"));
        quranTranslations.add(new QuranTranslation("Sindhi", getResources().getString(R.string.qurantp_translation_23), R.drawable.urdu, "sindhi"));
        quranTranslations.add(new QuranTranslation("Swedish", getResources().getString(R.string.qurantp_translation_24), R.drawable.swedish, "swedish"));
        quranTranslations.add(new QuranTranslation("Swahili", getResources().getString(R.string.qurantp_translation_25), R.drawable.swahili, "swahili"));
        quranTranslations.add(new QuranTranslation("Tajik", getResources().getString(R.string.qurantp_translation_26), R.drawable.tajik, "Tajik"));
        quranTranslations.add(new QuranTranslation("Tamil", getResources().getString(R.string.qurantp_translation_27), R.drawable.hindi, "tamil"));
        quranTranslations.add(new QuranTranslation("Tatar", getResources().getString(R.string.qurantp_translation_28), R.drawable.russian, "tatar"));
        quranTranslations.add(new QuranTranslation("Türkçe", getResources().getString(R.string.qurantp_translation_29), R.drawable.turkish, "turkish"));
        quranTranslations.add(new QuranTranslation("Uzbek", getResources().getString(R.string.qurantp_translation_30), R.drawable.uzbek, "uzbek"));
        quranTranslations.add(new QuranTranslation("български", getResources().getString(R.string.qurantp_translation_31), R.drawable.bulgarian, "bulgarian"));
        quranTranslations.add(new QuranTranslation("Urdu", getResources().getString(R.string.qurantp_translation_32), R.drawable.urdu, "urdu"));
        quranTranslations.add(new QuranTranslation("ภาษาไทย", getResources().getString(R.string.qurantp_translation_33), R.drawable.thai, "thai"));
        quranTranslations.add(new QuranTranslation("日本語", getResources().getString(R.string.qurantp_translation_34), R.drawable.japanese, "japanese"));
        quranTranslations.add(new QuranTranslation("中国", getResources().getString(R.string.qurantp_translation_35), R.drawable.chinese, "chinese"));
        quranTranslations.add(new QuranTranslation("한국어", getResources().getString(R.string.qurantp_translation_36), R.drawable.korean, "korean"));
        quranTranslations.add(new QuranTranslation("Urdu", getResources().getString(R.string.qurantp_translation_37), R.drawable.urdu, URDU_TRANSLATION_KEY));


        setQuranTranslationRV(quranTranslations);
    }


    private void downloadDialog(final String Key_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle2);
        builder.setTitle(getString(R.string.downloading_downloading))
                .setMessage(getContext().getResources().getString(R.string.qurantp_download_phonetic_transliteration))
                .setPositiveButton(getContext().getResources().getString(R.string.qurantp_download), (dialog, which) -> {
                    DownloadZipFile mFile = new DownloadZipFile(QuranTranslationsFragment.this, Key_id);
                    mFile.download();
                })
                .setNegativeButton(getContext().getResources().getString(R.string.text_cancel), (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(p0 -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.onPrimary1));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.onPrimary1));
        });
        dialog.show();
    }

    private void downloadData(String language, String key_id) {
        File mFile = new File(DataBaseFile.getFilePath("MuslimGuidePro", key_id + ".zip", mContext));
        if (!mFile.exists() && !key_id.equals(URDU_TRANSLATION_KEY)) {
            downloadDialog(key_id);
        } else {
            dataBaseFile.saveStringData(DataBaseFile.quranLanguageKey, key_id);
            lastSelectedName = language;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    quranTranslationAdapter.notifyDataSetChanged();

                }
            }, 450);

        }
    }

    @Override
    public void onDetach() {
//        if (lastSelectedName.equals("")) {
//            dataBaseFile.saveStringData(DataBaseFile.quranLanguageKey, "english");
//        }
        super.onDetach();
    }
}