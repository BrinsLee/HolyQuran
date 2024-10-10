package com.ihyas.soharamkarubar.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class AyahNote {

    @PrimaryKey
    @NonNull
    public String id;

    String surahEnglishName;
    String surahUrduName;
    int surahNumber;
    int verseNumber;
    String noteText;

    public AyahNote(String surahEnglishName, String surahUrduName, int surahNumber, int verseNumber, String noteText) {
        id = UUID.randomUUID().toString();
        this.surahEnglishName = surahEnglishName;
        this.surahUrduName = surahUrduName;
        this.surahNumber = surahNumber;
        this.verseNumber = verseNumber;
        this.noteText = noteText;
    }

    public String getSurahEnglishName() {
        return surahEnglishName;
    }

    public void setSurahEnglishName(String surahEnglishName) {
        this.surahEnglishName = surahEnglishName;
    }

    public String getSurahUrduName() {
        return surahUrduName;
    }

    public void setSurahUrduName(String surahUrduName) {
        this.surahUrduName = surahUrduName;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public void setSurahNumber(int surahNumber) {
        this.surahNumber = surahNumber;
    }

    public int getVerseNumber() {
        return verseNumber;
    }

    public void setVerseNumber(int verseNumber) {
        this.verseNumber = verseNumber;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
