package com.ihyas.soharamkarubar.SharedData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedClass {

    public static double lat = 0.0;
    public static double lng = 0.0;
    static SharedPreferences preferences;

    public static int getCounter(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("counter", 0);
    }

    public static int getTasbeenSound(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("tasbeensound", 1);
    }

}
