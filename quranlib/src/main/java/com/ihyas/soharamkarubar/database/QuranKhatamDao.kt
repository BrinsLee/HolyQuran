package com.ihyas.soharamkarubar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ihyas.soharamkarubar.models.KhatamData

@Dao
interface QuranKhatamDao {

    @Insert
    fun saveAyahNote(ayahNote: KhatamData)

    @Query("SELECT * FROM KhatamData WHERE surahNumber = :surahNumber")
    fun CheckIfKhatamIsStared(surahNumber: Int): List<KhatamData>?

    @Query("UPDATE KhatamData SET surahCurrentProgress= :progress WHERE surahNumber = :surahNumber")
    fun updateAyahNote(surahNumber: Int, progress: Int)

    @Query("UPDATE KhatamData SET readStatus= :status WHERE surahNumber = :surahNumber")
    fun updateKhatamStatus(surahNumber: Int, status: String)

    @Query("UPDATE KhatamData SET readStatus= :status , surahCurrentProgress= :progerss WHERE surahNumber = :surahNumber")
    fun updateKhatamStatusAndProgress(surahNumber: Int, status: String , progerss:Int)


    @Query("SELECT * from KhatamData")
    fun getAllKhatam(): List<KhatamData?>

    @Query("DELETE FROM KhatamData")
    fun deletekhatam()

}