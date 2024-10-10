package com.ihyas.soharamkarubar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ihyas.soharamkarubar.models.AyahNote

@Dao
interface AyahNotesDao {
    @get:Query("SELECT * FROM AyahNote")
    val allNotes: List<AyahNote?>?

    @Insert
    fun saveAyahNote(ayahNote: AyahNote)

    @Query("DELETE FROM AyahNote WHERE surahNumber = :surahNumber AND verseNumber= :verseNumber")
    fun deleteAyahNote(surahNumber: Int, verseNumber: Int)

    @Query("SELECT * FROM AyahNote WHERE surahNumber = :surahNumber AND verseNumber= :verseNumber")
    fun isNoteExists(surahNumber: Int, verseNumber: Int): AyahNote?

    @Query("UPDATE AyahNote SET noteText= :note WHERE  surahNumber = :surahNumber AND verseNumber= :verseNumber")
    fun updateAyahNote(note: String, surahNumber: Int, verseNumber: Int)
}