package com.ihyas.soharamkarubar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ihyas.soharamkarubar.models.AyahBookMark;
import com.ihyas.soharamkarubar.models.AyahNote;
import com.ihyas.soharamkarubar.models.KhatamData;
import com.ihyas.soharamkarubar.models.PrayerTrackerModel;

@Database(entities = {AyahBookMark.class, AyahNote.class, PrayerTrackerModel.class, KhatamData.class}, version = 3, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;


    public abstract AyahBookMarkDao ayahBookMarkDao();

    public abstract AyahNotesDao ayahNotesDao();


    public abstract QuranKhatamDao khatamDao();

    public abstract PrayerTrackerDao prayerTrackerDao();

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'PrayerTrackerModel' ('date' TEXT NOT NULL,'fajar' INTEGER NOT NULL, 'duhur' INTEGER NOT NULL, 'asar' INTEGER NOT NULL, 'maghrib' INTEGER NOT NULL, 'isha' INTEGER NOT NULL, PRIMARY KEY('date'))");

            database.execSQL("CREATE TABLE IF NOT EXISTS `KhatamData` (`id` INTEGER , 'khatamName' Text , 'surahNumber' INTEGER ," +
                    "'surahTotalAyat'INTEGER , 'surahCurrentProgress' INTEGER ,'readStatus' TEXT, PRIMARY KEY(`id`))");

        }
    };


    public static AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_db")
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return appDatabase;
    }


}
