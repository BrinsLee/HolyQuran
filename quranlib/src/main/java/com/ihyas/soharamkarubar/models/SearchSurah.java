package com.ihyas.soharamkarubar.models;

public class SearchSurah {

    private int surahNumber;
    private int ayahNumber;
    private String ayahRoman;
    private String ayahArabic;
    private String ayahTranslation;

    public SearchSurah() {
    }

    public SearchSurah(int surahNumber, int ayahNumber, String ayahArabic) {
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.ayahArabic = ayahArabic;
    }

    public SearchSurah(int surahNumber, int ayahNumber, String ayahRoman, String ayahArabic, String ayahTranslation) {
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.ayahRoman = ayahRoman;
        this.ayahArabic = ayahArabic;
        this.ayahTranslation = ayahTranslation;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public void setSurahNumber(int surahNumber) {
        this.surahNumber = surahNumber;
    }

    public int getAyahNumber() {
        return ayahNumber;
    }

    public void setAyahNumber(int ayahNumber) {
        this.ayahNumber = ayahNumber;
    }

    public String getAyahRoman() {
        return ayahRoman;
    }

    public void setAyahRoman(String ayahRoman) {
        this.ayahRoman = ayahRoman;
    }

    public String getAyahArabic() {
        return ayahArabic;
    }

    public void setAyahArabic(String ayahArabic) {
        this.ayahArabic = ayahArabic;
    }

    public String getAyahTranslation() {
        return ayahTranslation;
    }

    public void setAyahTranslation(String ayahTranslation) {
        this.ayahTranslation = ayahTranslation;
    }
}
