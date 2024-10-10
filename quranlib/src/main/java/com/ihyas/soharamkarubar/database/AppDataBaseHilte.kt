package com.ihyas.soharamkarubar.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteDua::class],
    version = 2, exportSchema = false
)
abstract class AppDataBaseHilte : RoomDatabase() {
    abstract fun favDuaDao(): FavoriteDuaDao
}