package com.ihyas.soharamkarubar.ui.quran;

import static com.ihyas.adlib.ADIdProviderKt.BANNER_AD_TYPE_QURAN;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.FragmentSearchBinding;
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper;
import com.ihyas.soharamkarubar.models.SearchSurah;
import com.ihyas.soharamkarubar.ui.quran.adapters.SearchAdapter;
import com.ihyas.soharamkarubar.utils.DataBaseFile;
import com.ihyas.soharamkarubar.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QuranSearchFragment extends Fragment {

    FragmentSearchBinding binding;
    Context mContext;
    List<SearchSurah> searchSurahList = new ArrayList<>();
    int choice = 1;
    public static final int DIFFERENCE_OF_AYAH = 1;
    NavController navController;
    DataBaseFile dataBaseFile;

    public QuranSearchFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater);
        binding.toolbar.tvTitle.setText(getString(R.string.searchinquran));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.toolbar.adViewContainer.refreshAd(BANNER_AD_TYPE_QURAN);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        initUtils();


        checkChipsId();
        binding.toolbar.backBtn.setOnClickListener(v ->
        {
            Utils.preventTwoClick(v);

            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            navController.navigateUp();
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (choice == 1) {
                    if (s.toString().matches("[a-zA-Z ].*")) {
                        Toast.makeText(mContext, getString(R.string.arabic_text_error_message), Toast.LENGTH_SHORT).show();
                    }
                }

                if (s.toString().length() == 0) {
                    List<SearchSurah> searchSurahList = new ArrayList<>();
                    setRV(searchSurahList);
                } else {
                    searchInQuran();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkChipsId() {
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.chip4.getId()) {
                choice = 1;
                binding.etSearch.setText("");
                binding.etSearch.clearFocus();
                View view = binding.getRoot().getRootView();
                try {
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (checkedId == binding.chip5.getId()) {
                choice = 2;
                binding.etSearch.setText("");
                binding.etSearch.clearFocus();
                View view = binding.getRoot().getRootView();
                try {
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                choice = 3;
                binding.etSearch.setText("");
                binding.etSearch.clearFocus();
                View view = binding.getRoot().getRootView();
                try {
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void searchInQuran() {
        List<SearchSurah> searchSurahList = searchAllFiles(Objects.requireNonNull(binding.etSearch.getText()).toString(), choice);
        setRV(searchSurahList);
    }

    private void setRV(List<SearchSurah> searchAllFiles) {
        SearchAdapter searchAdapter = new SearchAdapter(searchAllFiles, (parent, view, position, id) -> {
            LastSurahAndAyahHelper.storeSelectedSurah(mContext, searchSurahList.get(position).getSurahNumber() - DIFFERENCE_OF_AYAH);
            LastSurahAndAyahHelper.storeSelectedAyah(mContext, searchSurahList.get(position).getAyahNumber() - DIFFERENCE_OF_AYAH);
            navController.navigate(Uri.parse("https://qurandetail.com/"));
        });
        binding.rvSearchList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.rvSearchList.setAdapter(searchAdapter);
    }


    private List<SearchSurah> searchAllFiles(String searchText, int choice) {
        List<SearchSurah> filteredLIst = new ArrayList<>();
        List<List<String>> allSurahData = getAllQuranArabicFiles(choice);
        for (int i = 0; i < allSurahData.size(); i++) {
            List<String> strings = allSurahData.get(i);
            for (int j = 0; j < strings.size(); j++) {
                if (strings.get(j).toLowerCase().contains(searchText)) {
                    filteredLIst.add(new SearchSurah(i, j + 1, strings.get(j)));
                }
            }
        }
        searchSurahList = filteredLIst;
        return filteredLIst;
    }

    public List<List<String>> getAllQuranArabicFiles(int choice) {
        List<List<String>> allSurahData = new ArrayList<>();
        String initialDirectory;
        if (choice == 1) {
            initialDirectory = "Quran/Quran Data/simple arabic/";
        } else if (choice == 2) {
            initialDirectory = "Quran/Quran Data/arabic/";
        } else {
            initialDirectory = "Quran/Quran Data/english/";
        }
        for (int i = 0; i < 114; i++) {
            List<String> singleSurahData = Arrays.asList(removeCharacter(LoadDataOfFile(initialDirectory + getFileNumbers(i) + ".txt")).split("\n"));
            allSurahData.add(singleSurahData);
        }
        return allSurahData;
    }

    public String LoadDataOfFile(String inFile) {
        String tContents = "";
        try {
            InputStream stream = mContext.getAssets().open(inFile);
            int size = stream.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tContents;
    }


    public String getFileNumbers(int index) {
        if (index < 10)
            return "00" + index;
        else if (index < 100)
            return "0" + index;
        else
            return "" + index;
    }

    public static String removeCharacter(String str) {
        str = str.replace("\r", "");
        return str;
    }

    private void initUtils() {
        mContext = getContext();
        dataBaseFile = new DataBaseFile(mContext);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}