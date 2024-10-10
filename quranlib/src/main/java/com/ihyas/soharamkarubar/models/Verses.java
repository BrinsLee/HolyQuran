package com.ihyas.soharamkarubar.models;

public class Verses {

    String surahEnglishName;
    private String surahUrduName;
    int totalVerse;

    public Verses(String surahEnglishName, String surahUrduName, int totalVerse) {
        this.surahEnglishName = surahEnglishName;
        this.surahUrduName = surahUrduName;
        this.totalVerse = totalVerse;
    }

    public String getSurahEnglishName() {
        return surahEnglishName;
    }


    public String getSurahUrduName() {
        return surahUrduName;
    }

    public void setSurahUrduName(String surahUrduName) {
        this.surahUrduName = surahUrduName;
    }

    public int getTotalVerse() {
        return totalVerse;
    }


}
