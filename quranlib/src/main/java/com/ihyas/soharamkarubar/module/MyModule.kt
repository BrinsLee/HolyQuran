package com.ihyas.soharamkarubar.module

import com.ihyas.soharamkarubar.database.AppDataBaseHilte
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkarubar.database.FavoriteDuaDao
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {
    @Qualifier
    annotation class room


    @Qualifier
    annotation class sretr2

    @Qualifier
    annotation class retr

    @Qualifier
    annotation class retr2

    @Qualifier
    annotation class retrH

    @Qualifier
    annotation class retrSH

    @Qualifier
    annotation class okhttp

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `TashbeehData` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `longDes` TEXT NOT NULL, `totalTasbihCounter` TEXT NOT NULL,`readTasbihCounter` TEXT NOT NULL, `tasbihLooper` TEXT NOT NULL, PRIMARY KEY(`id`))")
            database.execSQL("CREATE TABLE IF NOT EXISTS `FavoriteDuaTable` (`index` INTEGER NOT NULL, `title` TEXT NOT NULL, `category` TEXT NOT NULL, PRIMARY KEY(`index`))")
        }
    }

    val MIGRATION_2_3: Migration = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE 'PrayerTrackerModel' ('date' TEXT NOT NULL,'fajar' INTEGER NOT NULL, 'duhur' INTEGER NOT NULL, 'asar' INTEGER NOT NULL, 'maghrib' INTEGER NOT NULL, 'isha' INTEGER NOT NULL, PRIMARY KEY('date'))")
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `KhatamData` (`id` INTEGER , 'khatamName' Text , 'surahNumber' INTEGER ," +
                        "'surahTotalAyat'INTEGER , 'surahCurrentProgress' INTEGER ,'readStatus' TEXT, PRIMARY KEY(`id`))"
            )
        }
    }

    @Singleton
    @Provides
    fun getRoomInstance(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_db"
        )
            .addMigrations(MIGRATION_2_3)
            .build()
    }


    @Singleton
    @Provides
    fun getAppData(
        @ApplicationContext context: Context
    ): AppDataBaseHilte {
        val dbHilte = Room.databaseBuilder(
            context,
            AppDataBaseHilte::class.java, "database-hilte-name"
        ).addMigrations(MIGRATION_1_2)
            .build()
        return dbHilte
    }

    @room
    @Singleton
    @Provides
    fun getRabbanaDuaDao(
        appDataBaseHilte: AppDataBaseHilte
    ): FavoriteDuaDao {
        return appDataBaseHilte.favDuaDao()
    }
}