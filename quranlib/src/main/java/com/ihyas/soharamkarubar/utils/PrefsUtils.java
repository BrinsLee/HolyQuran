package com.ihyas.soharamkarubar.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefsUtils {

    private Context context;
    public static String timeFormat = "TIME_FORMAT";
    public static String calcMethod = "CALC_METHOD";
    public static String juristic = "JURISTIC";
    public static String highLats = "HIGH_LATS";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PrefsUtils(Context context) {
        this.context = context;
        initSharedPrefs(context);
    }

    public void initSharedPrefs(Context context) {
//        Hawk.init(context).build();
        sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public <T> void saveToPrefs(String key, T value) {
//        Hawk.put(key, value);
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {

            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {

        }
        editor.apply();
    }


    public <T> T getFromPrefsWithDefault(String key, T defaultValue) {
        try {
            Object result;
            if (defaultValue instanceof String) {
                result = sharedPreferences.getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Integer) {
                result = sharedPreferences.getInt(key, (Integer) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                result = sharedPreferences.getBoolean(key, (Boolean) defaultValue);
            } else if (defaultValue instanceof Float) {
                result = sharedPreferences.getFloat(key, (Float) defaultValue);
            } else if (defaultValue instanceof Long) {
                result = sharedPreferences.getLong(key, (Long) defaultValue);
            } else if (defaultValue instanceof Double) {
                result = sharedPreferences.getFloat(key, (Float) defaultValue);
            } else {
                result = defaultValue;
            }
            return (T) result;
        } catch (Exception e) {
            return defaultValue;
        }

    }

/*    public void deleteAll() {
        Hawk.deleteAll();
    }

    public void deleteSingleValue(String key) {
        Hawk.delete(key);
    }

    public boolean checkKey(String key) {
        return Hawk.contains(key);
    }*/
}