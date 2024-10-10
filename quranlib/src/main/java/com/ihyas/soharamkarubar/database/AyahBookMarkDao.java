package com.ihyas.soharamkarubar.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ihyas.soharamkarubar.models.AyahBookMark;

import java.util.List;

@Dao
public interface AyahBookMarkDao {

    @Query("SELECT * FROM AyahBookMark")
    List<AyahBookMark> getAllBookMarks();

    @Insert
    void saveAyahBookMark(AyahBookMark ayahBookMark);

    @Query("DELETE FROM AyahBookMark WHERE surahNumber = :surahNumber AND verseNumber= :verseNumber")
    void deleteBookMark(int surahNumber, int verseNumber);

    @Query("SELECT * FROM AyahBookMark WHERE surahNumber = :surahNumber AND verseNumber= :verseNumber")
    AyahBookMark isBookMarkExists(int surahNumber, int verseNumber);

}
