package com.ihyas.soharamkarubar.ui.quran;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.FragmentAudioSettingsBinding;
import com.ihyas.soharamkarubar.utils.DataBaseFile;

public class QuranAudioSettingsFragment extends Fragment {

    FragmentAudioSettingsBinding binding;
    Context mContext;
    NavController navController;
    DataBaseFile dataBaseFile;

    public QuranAudioSettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAudioSettingsBinding.inflate(getLayoutInflater());
        binding.include15.tvTitle.setText(getString(R.string.text_audio_recitations));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUtils(view);
        loadAds();
        onClickEvents();
        makeSpinner();
        manageReciters();
        manageOtherSettings();
        manageAtTheEndOfSurah();
    }


    private void onClickEvents() {
        binding.include15.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.popBackStack();
            }
        });
    }

    private void manageAtTheEndOfSurah() {
        int nextSurahStartKey = dataBaseFile.getIntData(DataBaseFile.nextSurahStartKey, 0);
        if (nextSurahStartKey == 0) {
            binding.rdoStopPlaying.setChecked(true);
        } else if (nextSurahStartKey == 1) {
            binding.rdoRepeatTheSurah.setChecked(true);
        } else if (nextSurahStartKey == 2) {
            binding.rdoTheNextSurah.setChecked(true);
        }

        binding.rdoAtTheEndOfSurah.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.rdoStopPlaying.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.nextSurahStartKey, 0);
            } else if (checkedId == binding.rdoRepeatTheSurah.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.nextSurahStartKey, 1);
            } else if (checkedId == binding.rdoTheNextSurah.getId()) {
                dataBaseFile.saveIntData(DataBaseFile.nextSurahStartKey, 2);
            }
        });
    }

    private void manageOtherSettings() {
        Log.d("screen","gg");
        boolean check = dataBaseFile.getBooleanData(DataBaseFile.autoScrollKey, true);
        boolean check2 = dataBaseFile.getBooleanData(DataBaseFile.screenOnKey, false);
        Log.d("screen", String.valueOf(check2));

        if (check) {
            binding.autoScrollSwitch.setChecked(true);
        }

        if (check2) {
            binding.screenOnSwitch.setChecked(true);
        }
        binding.screenOnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> dataBaseFile.saveBooleanData(DataBaseFile.screenOnKey, isChecked));
        binding.autoScrollSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> dataBaseFile.saveBooleanData(DataBaseFile.autoScrollKey, isChecked));
    }

    private void manageReciters() {

        if (dataBaseFile.getStringData(DataBaseFile.recitorAudioKey, "sudais").equals("sudais")) {
            binding.radioButton.setChecked(true);
        } else if (dataBaseFile.getStringData(DataBaseFile.recitorAudioKey, "alfasy").equals("alfasy")) {
            binding.radioButton2.setChecked(true);
        } else {
            binding.radioButton3.setChecked(true);
        }

        binding.rdoReciters.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.radioButton.getId()) {
                dataBaseFile.saveStringData(DataBaseFile.recitorAudioKey, "sudais");
            } else if (checkedId == binding.radioButton2.getId()) {
                dataBaseFile.saveStringData(DataBaseFile.recitorAudioKey, "alfasy");
            } else {
                dataBaseFile.saveStringData(DataBaseFile.recitorAudioKey, "alghamidi");
            }
        });
    }

    private void makeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.repeat_ayah_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRepeatAyahTimes.setAdapter(adapter);
        int repeatVerseKey = dataBaseFile.getIntData(DataBaseFile.repeatVerseKey, 0);
        switch (repeatVerseKey) {
            case 0:
                binding.spinnerRepeatAyahTimes.setSelection(0);
                break;
            case 1:
                binding.spinnerRepeatAyahTimes.setSelection(1);
                break;
            case 2:
                binding.spinnerRepeatAyahTimes.setSelection(2);
                break;
            case 3:
                binding.spinnerRepeatAyahTimes.setSelection(3);
                break;
            case 4:
                binding.spinnerRepeatAyahTimes.setSelection(4);
                break;
            case 5:
                binding.spinnerRepeatAyahTimes.setSelection(5);
                break;
            default:
                binding.spinnerRepeatAyahTimes.setSelection(6);
                break;
        }

        binding.spinnerRepeatAyahTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 5) {
                    dataBaseFile.saveIntData(DataBaseFile.repeatVerseKey, 100);
                } else {
                    dataBaseFile.saveIntData(DataBaseFile.repeatVerseKey, position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initUtils(View view) {
        mContext = getContext();
        navController = Navigation.findNavController(view);
        dataBaseFile = new DataBaseFile(mContext);

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
}