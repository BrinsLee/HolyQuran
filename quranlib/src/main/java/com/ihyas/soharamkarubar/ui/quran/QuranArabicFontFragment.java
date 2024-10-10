package com.ihyas.soharamkarubar.ui.quran;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.FragmentArabicFontBinding;
import com.ihyas.soharamkarubar.utils.DataBaseFile;
import com.ihyas.soharamkarubar.utils.Utils;

public class QuranArabicFontFragment extends Fragment {

    FragmentArabicFontBinding binding;
    Context mContext;
    NavController navController;
    DataBaseFile dataBaseFile;
    private Typeface dtf, ctf1, ctf2, ctf3, ctf4, ctf5, ctf6, ctf7, ctf8;

    public QuranArabicFontFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArabicFontBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUtils(view);
        setTypeface();
        loadAds();
        setRadioButtonText();
        setRadioButtonListeners();

        binding.back.setOnClickListener(v ->
        {
            Utils.preventTwoClick(v);
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void loadAds() {
//        AdView mAdView = binding.adView;
//        if (dataBaseFile.getBooleanData(DataBaseFile.purchaseKey, false)) {
//            mAdView.setVisibility(View.GONE);
//        } else {
//            mAdView.setVisibility(View.VISIBLE);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView.loadAd(adRequest);
//        }
    }

    private void setRadioButtonListeners() {

        int fontKey = dataBaseFile.getIntData(DataBaseFile.fontIndexKey, 0);

        if (fontKey == 0) {
            binding.rdoDefaultFont.setChecked(true);
            binding.demoText.setTypeface(dtf);
        } else if (fontKey == 1) {
            binding.rdoFont1.setChecked(true);
            binding.demoText.setTypeface(ctf1);
        } else if (fontKey == 2) {
            binding.rdoFont2.setChecked(true);
            binding.demoText.setTypeface(ctf2);
        } else if (fontKey == 3) {
            binding.rdoFont3.setChecked(true);
            binding.demoText.setTypeface(ctf3);
        } else if (fontKey == 4) {
            binding.rdoFont4.setChecked(true);
            binding.demoText.setTypeface(ctf4);
        } else if (fontKey == 5) {
            binding.rdoFont5.setChecked(true);
            binding.demoText.setTypeface(ctf5);
        } else if (fontKey == 6) {
            binding.rdoFont6.setChecked(true);
            binding.demoText.setTypeface(ctf6);
        } else if (fontKey == 7) {
            binding.rdoFont7.setChecked(true);
            binding.demoText.setTypeface(ctf7);
        } else {
            binding.rdoFont8.setChecked(true);
            binding.demoText.setTypeface(ctf8);
        }

        binding.rdgArabicFont.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.rdoDefaultFont.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 0);
                binding.demoText.setTypeface(dtf);
            } else if (checkedId == binding.rdoFont1.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 1);
                binding.demoText.setTypeface(ctf1);
            } else if (checkedId == binding.rdoFont2.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 2);
                binding.demoText.setTypeface(ctf2);
            } else if (checkedId == binding.rdoFont3.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 3);
                binding.demoText.setTypeface(ctf3);
            } else if (checkedId == binding.rdoFont4.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 4);
                binding.demoText.setTypeface(ctf4);
            } else if (checkedId == binding.rdoFont5.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 5);
                binding.demoText.setTypeface(ctf5);
            } else if (checkedId == binding.rdoFont6.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 6);
                binding.demoText.setTypeface(ctf6);
            } else if (checkedId == binding.rdoFont7.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 7);
                binding.demoText.setTypeface(ctf7);
            } else {
                dataBaseFile.saveIntData(DataBaseFile.fontIndexKey, 8);
                binding.demoText.setTypeface(ctf8);
            }
        });
    }

    private void setTypeface() {
        dtf = Typeface.createFromAsset(mContext.getAssets(), "Font/anotherarabic.otf");
        ctf1 = Typeface.createFromAsset(mContext.getAssets(), "Font/font1.ttf");
        ctf2 = Typeface.createFromAsset(mContext.getAssets(), "Font/font2.ttf");
        ctf3 = Typeface.createFromAsset(mContext.getAssets(), "Font/font3.ttf");
        ctf4 = Typeface.createFromAsset(mContext.getAssets(), "Font/font4.ttf");
        ctf5 = Typeface.createFromAsset(mContext.getAssets(), "Font/font5.ttf");
        ctf6 = Typeface.createFromAsset(mContext.getAssets(), "Font/font6.ttf");
        ctf7 = Typeface.createFromAsset(mContext.getAssets(), "Font/font7.otf");
        ctf8 = Typeface.createFromAsset(mContext.getAssets(), "Font/arabic2.ttf");
    }

    @SuppressLint("SetTextI18n")
    private void setRadioButtonText() {
        binding.rdoFont1.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 1");
        binding.rdoFont2.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 2");
        binding.rdoFont3.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 3");
        binding.rdoFont4.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 4");
        binding.rdoFont5.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 5");
        binding.rdoFont6.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 6");
        binding.rdoFont7.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 7");
        binding.rdoFont8.setText(mContext.getResources().getString(R.string.quransp_custom_font) + " 8");
    }

    private void initUtils(View view) {
        mContext = getContext();
        navController = Navigation.findNavController(view);
        dataBaseFile = new DataBaseFile(mContext);
    }
}