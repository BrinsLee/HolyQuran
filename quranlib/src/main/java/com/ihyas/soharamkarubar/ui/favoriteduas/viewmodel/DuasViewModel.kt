package com.ihyas.soharamkarubar.ui.favoriteduas.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ihyas.soharamkarubar.database.*
import com.ihyas.soharamkarubar.module.MyModule
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DuasViewModel @Inject constructor(
    @ApplicationContext var context: Context,
    @MyModule.room var favoriteDuaDao: FavoriteDuaDao,
) : ViewModel() {

    suspend fun addDua(dua: FavoriteDua) {
        favoriteDuaDao.insertDua(dua)
    }

    suspend fun getDuaSaved(title: String): FavoriteDua? {
        return favoriteDuaDao.isDuaSaved(title)
    }

    suspend fun deleteDua(dua: FavoriteDua) {
        return favoriteDuaDao.deleteDua(dua.title)
    }

    suspend fun getAllRabbanaDuas(): List<FavoriteDua>{
        return favoriteDuaDao.getAllFavDuaByCategory("Rabbana Duas")
    }

    suspend fun getAllHisnulMuslimDuas(): List<FavoriteDua>{
        return favoriteDuaDao.getAllFavDuaByCategory("Hisnul Muslim Duas")
    }

    suspend fun getAllRamadanDuas(): List<FavoriteDua>{
        return favoriteDuaDao.getAllFavDuaByCategory("Ramadan Duas")
    }
}