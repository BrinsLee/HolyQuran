package com.ihyas.soharamkarubar.utils;

import static com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.HANFI_CONSTANT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ihyas.soharamkarubar.utils.language.LanguageConfig;

public class SharedPrefMethods {

    private static final String APP_SHARED_PREFS = "Location";
    private final SharedPreferences appSharedPrefs;
    private final Editor prefsEditor;

    public static final String LAST_AD_TIME_KEY = "lastAdTime";

    @SuppressLint("CommitPrefEdits")
    public SharedPrefMethods(Context context) {
        appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
                Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public void saveCountryName(String key, String countryName) {
        prefsEditor.putString(key, countryName);
        prefsEditor.commit();
    }

    public void saveCityName(String key, String cityName) {
        prefsEditor.putString(key, cityName);
        prefsEditor.commit();
    }

    public String getCountryName(String key) {
        return appSharedPrefs.getString(key, "Country");
    }

    public String getCityName(String key) {
        return appSharedPrefs.getString(key, "City");
    }

    public void saveCheckBoxSate(String key, Boolean state) {
        prefsEditor.putBoolean(key, state);
        prefsEditor.commit();
    }

    public Boolean getCheckBoxSate(String key) {
        return appSharedPrefs.getBoolean(key, false);
    }

    public void saveJuristicMethod(String key, String juristicMethod) {
        prefsEditor.putString(key, juristicMethod);
        prefsEditor.commit();
    }

    public String getJuristicMethod(String key) {
        return appSharedPrefs.getString(key, HANFI_CONSTANT);
    }

    public void saveHighLatitude(String key, int highLati) {
        prefsEditor.putInt(key, highLati);
        prefsEditor.commit();
    }

    public int getHighLatitude(String key) {
        return appSharedPrefs.getInt(key, 0);
    }

    public void saveDaylight(String key, int option) {
        prefsEditor.putInt(key, option);
        prefsEditor.commit();
    }

    public int getDaylight(String key) {
        return appSharedPrefs.getInt(key, 0);
    }

    public void saveHijriCorrection(String key, int option) {
        prefsEditor.putInt(key, option);
        prefsEditor.commit();
    }

    public int getHijriCorrection(String key) {
        return appSharedPrefs.getInt(key, 3);
    }

    public void saveConventionType(String array) {
        prefsEditor.putString("Convention", array);
        prefsEditor.commit();
    }

    public String getConventionType() {
        String defaultV = "1,18.0,18.0";
        return appSharedPrefs.getString("Convention", defaultV);
    }

    public void saveManualFajr(String array) {
        prefsEditor.putString("ManualCorrFajr", array);
        prefsEditor.commit();
    }

    public String getManualFajr(String key) {
        return appSharedPrefs.getString(key, "0");
    }

    public void saveManualSunrise(String array) {
        prefsEditor.putString("ManualCorrSunrise", array);
        prefsEditor.commit();
    }

    public String getManualSunrise(String key) {
        return appSharedPrefs.getString(key, "0");
    }

    public void saveManualDuhr(String array) {
        prefsEditor.putString("ManualCorrDuhr", array);
        prefsEditor.commit();
    }

    public String getManualDuhr(String key) {
        return appSharedPrefs.getString(key, "0");
    }

    public void saveManualAsr(String array) {
        prefsEditor.putString("ManualCorrAsr", array);
        prefsEditor.commit();
    }

    public String getManualAsr(String key) {
        return appSharedPrefs.getString(key, "0");
    }

    public void saveManualMaghrib(String array) {
        prefsEditor.putString("ManualCorrMaghrib", array);
        prefsEditor.commit();
    }

    public String getManualMaghrib(String key) {
        return appSharedPrefs.getString(key, "0");
    }

    public void saveManualIsha(String array) {
        prefsEditor.putString("ManualCorrIsha", array);
        prefsEditor.commit();
    }

    public String getManualIsha(String key) {
        return appSharedPrefs.getString(key, "0");
    }


    public void saveVolumeCheckBoxState(String key, Boolean state) {
        prefsEditor.putBoolean(key, state);
        prefsEditor.commit();
    }

    public boolean getVolumeCheckBoxState(String key) {
        return appSharedPrefs.getBoolean(key, true);
    }

    public void saveAdhanVolumeState(String key, String state) {
        prefsEditor.putString(key, state);
        prefsEditor.commit();
    }

    public void savePreAdhanTime(String key, String time) {
        prefsEditor.putString(key, time);
        prefsEditor.commit();
    }

    public String getPreAdhanTime(String key) {
        return appSharedPrefs.getString(key, "0");
    }

    public Integer getCurrentChannelId() {
        return appSharedPrefs.getInt("prayerChanneld", 0);
    }

    public void saveCurrentChannlId(Integer id) {
        prefsEditor.putInt("prayerChanneld", id);
        prefsEditor.commit();
    }

    public void saveLEDColor(String key, String color) {
        prefsEditor.putString(key, color);
        prefsEditor.commit();
    }

    public void saveDistanceUnit(String key, String unittag) {
        prefsEditor.putString(key, unittag);
        prefsEditor.commit();
    }

    public String getCurrentLanguage(String key) {
        return appSharedPrefs.getString(key, LanguageConfig.LANGUAGE_AR);
    }

    public void saveCurrentLanguage(String key, String lang) {
        prefsEditor.putString(key, lang);
        prefsEditor.commit();
    }

    public void saveLastAdTime(String key, long time) {
        prefsEditor.putLong(key, time);
        prefsEditor.apply();
    }

    public long getLastAdTime(String key) {
        return appSharedPrefs.getLong(key, 0);
    }

    public String getDistanceUnit(String key) {
        return appSharedPrefs.getString(key, "KM");
    }




}
