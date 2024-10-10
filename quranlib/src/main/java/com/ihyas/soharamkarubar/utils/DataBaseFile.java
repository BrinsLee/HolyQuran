package com.ihyas.soharamkarubar.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class DataBaseFile {

    private final Context con;

    public static String engFontSizeKey = "engFontSize";
    public static String arabicFontSizeKey = "arabicFontSize";

    public static String rengFontSizeKey = "rengFontSize";
    public static String rarabicFontSizeKey = "rarabicFontSize";

    public static String prayerTimeConvention = "ptConvention";

    // For Language Setting Keys.............
    public static String quranLanguageKey = "Language";
    public static String quranContainLanguageKey = "contain_language";
    public static String fontIndexKey = "font_index";
    public static String arabicStyleKey = "arabicStyle";

    // For Reciter Setting keys...............
    public static String autoScrollKey = "autoScroll";
    public static String screenOnKey = "screenOn";
    public static String nextSurahStartKey = "nextSurah";
    public static String repeatVerseKey = "repeatVerse";
    public static String recitorAudioKey = "recitor";
    public static String purchaseKey = "purchase";

    public static String latitudeKey = "latitude";
    public static String LongitudeKey = "longitude";
    public static String qiblaDKey = "qiblaDirection";
    public static String LocationKey = "location_method";
    public static String timeZoneKey = "timeZone";

    public static String runOnce = "run";
    public static String tasbiCounter = "tasbiCounter";
    public static String looperCounter = "looperCounter";
    public static String tasbiCounterMaximum = "tasbiCounterMaximum";
    public static String transliterationKey = "transliteration";
    public static String tasbiSoundKey = "tasbeeSound";
    public static String zakatNisabKey = "nisaab_zakat";
    public static String zakatTotalKey = "total_key";
    public static String zakatDetectionKey = "detection_key";
    public static String zakatLanguateKey = "zakat_language";
    public static String ramadanDayAdjKey = "day_adjustment";

    public static String autoSilentEnableKey = "auto_silent_enable";
    public static String autoSilentBeforeKey = "auto_silent_before";
    public static String autoSilentAfterKey = "auto_silent_after";
    public static String isFajarPrayerNotificationEnabledKey = "isFajarPrayerNotificationEnabledKey";
    public static String isSunrisePrayerNotificationEnabledKey = "isSunrisePrayerNotificationEnabledKey";
    public static String isDuhrPrayerNotificationEnabledKey = "isDuhrPrayerNotificationEnabledKey";
    public static String isAsarPrayerNotificationEnabledKey = "isAsarPrayerNotificationEnabledKey";
    public static String isMaghribPrayerNotificationEnabledKey = "isMaghribPrayerNotificationEnabledKey";
    public static String isIshaPrayerNotificationEnabledKey = "isIshaPrayerNotificationEnabledKey";
    public static String isTasbeehZikrSaved = "isTasbeehZikrSaved";
    public static String userToken = "userToken";
    public static String userTempToken = "userTempToken";

    public static String languageStatus = "languageStatus";
    public static String applicationThemeMode = "applicationMode";
    public static String systemThemeMode = " systemThemeMode";
    public static String userName = "userName";
    public static String userEmail = "userEmail";
    public static String signedInnedBy = "signedInnedBy";
    public static String profilePicturePath = "profilePicturePath";

    public static String isBookViewEnabled = "isBookViewEnabled";



    //  Alarm Type Ids..............
    public static int NAMAZ_ALARM_ID = 999;
    public static int PRE_NAMAZ_ALARM_ID = 111;
    public static int AUTO_SILENT_ID_BEFORE = 666;
    public static int AUTO_SILENT_ID_AFTER = 333;

    // Tafseer information
    public static String tafseerAuthor = "Tafseer";
    public static int selectedQiblaFrame = 0;
    public static int selectedTasbeehDaana = 0;


    public DataBaseFile(Context con) {
        this.con = con;
    }


    public void deleteData(String key) {
        try {
            if (this.con != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.con);
                Editor edit = sp.edit();
                edit.remove(key);
                edit.apply();
            }
        } catch (Exception ignored) {

        }
    }


    public void saveStringData(String key, String value) {
        try {
            if (this.con != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.con);
                Editor edit = sp.edit();
                edit.putString(key, value);
                edit.apply();
            }
        } catch (Exception ignored) {

        }
    }

    public void saveIntData(String key, int value) {
        try {
            if (this.con != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.con);
                Editor edit = sp.edit();
                edit.putInt(key, value);
                edit.apply();
            }
        } catch (Exception ignored) {

        }
    }


    public void saveLongData(String key, long value) {
        try {
            if (this.con != null) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.con);
                Editor edit = sp.edit();
                edit.putLong(key, value);

                edit.apply();
            }
        } catch (Exception ignored) {
        }
    }


    public void saveBooleanData(String key, Boolean value) {
        try {
            if (this.con != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.con);
                Editor edit = sp.edit();
                edit.putBoolean(key, value);
                edit.apply();
            }
        } catch (Exception ignored) {
        }
    }

    public String getStringData(String key, String defaultValue) {
        SharedPreferences sp = null;

        try {
            if (this.con != null) {

                sp = PreferenceManager
                        .getDefaultSharedPreferences(this.con);
            }
        } catch (Exception ignored) {

        }
        if (sp != null)
            return sp.getString(key, defaultValue);
        else
            return defaultValue;
    }


    public int getIntData(String key, int defaultValue) {
        SharedPreferences sp = null;

        try {
            if (this.con != null) {

                sp = PreferenceManager
                        .getDefaultSharedPreferences(this.con);

            }
        } catch (Exception ignored) {

        }
        if (sp != null)
            return sp.getInt(key, defaultValue);
        else
            return defaultValue;
    }

    public long getLongData(String key, long defaultValue) {
        SharedPreferences sp = null;
        try {
            if (this.con != null) {

                sp = PreferenceManager
                        .getDefaultSharedPreferences(this.con);

            }
        } catch (Exception ignored) {

        }
        if (sp != null)
            return sp.getLong(key, defaultValue);
        else
            return defaultValue;
    }


    public boolean getBooleanData(String key, boolean defaultValue) {
        SharedPreferences sp = null;
        try {
            if (this.con != null) {

                sp = PreferenceManager
                        .getDefaultSharedPreferences(this.con);

            }
        } catch (Exception ignored) {
            Log.d("error" , ignored.toString());
        }
        if (sp != null)
            return sp.getBoolean(key, defaultValue);
        else
            return defaultValue;
    }


    public static String LoadData(String inFile, Context con) {
        String tContents = "";

        try {
            InputStream stream = con.getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;
    }

    public static String removeCharacter(String str) {
        str = str.replace("\r", "");
        return str;
    }


    public static String getFilePath(String directory, String filename, Context mActivity) {
        File mydir = mActivity.getDir(directory, Context.MODE_PRIVATE);
        File file = new File(mydir, filename);
        return file.getPath();
    }


    @SuppressWarnings("unused")
    public static void saveFile(String filename, String data) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filename);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String readFile(String filename) {
        StringBuilder stringBuffer = new StringBuilder();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
            String inputString;

            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String ExtractZip(File EbbArchive, String desPath) {

        String retTmpOutPut;
        try {
            new Message();
            int BUFFER = 512;

            BufferedOutputStream dest;
            ZipInputStream zis;

            ZipEntry entry;
            int count;

            FileOutputStream fos;

            String start_path = desPath + "/" + EbbArchive.getName();

            retTmpOutPut = start_path.substring(0,
                    start_path.lastIndexOf("."));

            if (start_path.contains(".")) start_path = start_path.substring(0,
                    start_path.lastIndexOf("."));

            if (!new File(start_path).exists())
                //noinspection ResultOfMethodCallIgnored
                new File(start_path).mkdirs();
            new File("");
            File file;

            InputStream fis = new FileInputStream(EbbArchive);
            byte[] data = new byte[BUFFER];
            zis = new ZipInputStream(new BufferedInputStream(fis,
                    BUFFER));
            while ((entry = zis.getNextEntry()) != null) {
                // 不安全文件创建方式
                file = new File(start_path + "/" + entry.getName());
                // 验证解压路径是否在目标文件夹中，防止路径遍历
                String destDirPath = desPath;
                String newFilePath = file.getAbsolutePath();
                if (!newFilePath.startsWith(destDirPath + File.separator)) {
                    throw new ZipException("Illegal name: " + entry.getName());
                }
                if (file.exists()) {
                    if (entry.getSize() == file.length()) {
                        continue;
                    }
                }

                if (entry.isDirectory()) {
                    if (!file.exists())
                        file.mkdirs();
                }

                if (!entry.isDirectory()) {

                    if (new File(start_path + "/" + entry.getName())
                            .exists())
                        continue;
                    {
                        File fff = new File(start_path + "/"
                                + entry.getName()).getParentFile();
                        while (fff != null) {
                            if (!fff.exists())
                                //noinspection ResultOfMethodCallIgnored
                                fff.mkdirs();
                            fff = fff.getParentFile();
                        }
                    }

                    fos = new FileOutputStream(start_path + "/"
                            + entry.getName());
                    dest = new BufferedOutputStream(fos, BUFFER);

                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }

                    dest.flush();
                    dest.close();
                }

            }
            zis.close();

        } catch (Exception e) {
            Log.d("myTag", e.toString());
            e.printStackTrace();
            return "";
        }

        return retTmpOutPut;
    }
}

