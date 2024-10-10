package com.ihyas.soharamkarubar.Helper;

import android.content.Context;
import android.preference.PreferenceManager;

public class LastSurahAndAyahHelper {

    public static String sLastSurah = "lastSurah";
    public static String sLastAyah = "lastAyah";
    public static String sSelectedSurah = "selectedSurah";
    public static String sSelectedAyah = "selectedAyah";
    public static String sQurankhatamtime = "qurankhatamtime";

    public static void storeLastSurah(Context context, int lastSurah) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(sLastSurah, lastSurah).apply();
    }

    public static void storeLastAyah(Context context, int lastAyah) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(sLastAyah, lastAyah).apply();
    }

    public static void storeSelectedSurah(Context context, int selectedSurah) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(sSelectedSurah, selectedSurah).apply();
    }

    public static void storeSelectedAyah(Context context, int selectedAyah) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(sSelectedAyah, selectedAyah).apply();
    }

    public static void storeKhatamMilliSecond(Context context, Long selectedAyah) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(sQurankhatamtime, selectedAyah).apply();
    }

    public static int getLastSurah(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(sLastSurah, 0);
    }

    public static int getLastAyah(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(sLastAyah, 0);
    }

    public static int getSelectedSurah(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(sSelectedSurah, 0);
    }

    public static int getSelectedAyah(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(sSelectedAyah, 0);
    }

    public static Long getKhatamTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(sQurankhatamtime, 0);
    }




}
