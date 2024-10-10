package com.ihyas.soharamkarubar.models;

public class Juz {

    int juzNumber;
    String juzUrduName;
    String juzEnglishName;
    String surahName;
    int surahNumber;
    int verseNumber;

    public Juz(int juzNumber, String juzUrduName, String juzEnglishName, String surahName, int surahNumber, int verseNumber) {
        this.juzNumber = juzNumber;
        this.juzUrduName = juzUrduName;
        this.juzEnglishName = juzEnglishName;
        this.surahName = surahName;
        this.surahNumber = surahNumber;
        this.verseNumber = verseNumber;
    }

    public int getJuzNumber() {
        return juzNumber;
    }

    public void setJuzNumber(int juzNumber) {
        this.juzNumber = juzNumber;
    }

    public String getJuzUrduName() {
        return juzUrduName;
    }

    public void setJuzUrduName(String juzUrduName) {
        this.juzUrduName = juzUrduName;
    }

    public String getJuzEnglishName() {
        return juzEnglishName;
    }

    public void setJuzEnglishName(String juzEnglishName) {
        this.juzEnglishName = juzEnglishName;
    }

    public String getSurahName() {
        return surahName;
    }

    public void setSurahName(String surahName) {
        this.surahName = surahName;
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
}
