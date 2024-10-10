package com.ihyas.soharamkarubar.utils;

import android.content.Context;

public class NumbersLocal {

    public static String convertNumberType(Context context, String number) {

        try {
            if (context.getResources().getConfiguration().locale.getDisplayLanguage().equals("العربية"))
                return number.replaceAll("0", "٠").replaceAll("1", "١")
                        .replaceAll("2", "٢").replaceAll("3", "٣")
                        .replaceAll("4", "٤").replaceAll("5", "٥")
                        .replaceAll("6", "٦").replaceAll("7", "٧")
                        .replaceAll("8", "٨").replaceAll("9", "٩");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }
}
