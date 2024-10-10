package com.ihyas.soharamkarubar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDuaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDua(dua: FavoriteDua)

    @Query("select * from favoriteduatable where title = :title limit 1")
    suspend fun isDuaSaved(title: String): FavoriteDua

    @Query("delete from favoriteduatable WHERE title = :title")
    suspend fun deleteDua(title: String)

    @Query("select * from FavoriteDuaTable where category = :category")
    suspend fun getAllFavDuaByCategory(category: String): List<FavoriteDua>
}
