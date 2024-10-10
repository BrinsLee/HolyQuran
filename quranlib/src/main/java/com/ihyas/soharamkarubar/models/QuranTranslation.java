package com.ihyas.soharamkarubar.models;

public class QuranTranslation {

    private String quranTranslation;
    private String quranTranslationAuthor;
    private int countryFlag;
    private String downloadKey;

    public QuranTranslation(String quranTranslation, String quranTranslationAuthor, int countryFlag, String downloadKey) {
        this.quranTranslation = quranTranslation;
        this.quranTranslationAuthor = quranTranslationAuthor;
        this.countryFlag = countryFlag;
        this.downloadKey = downloadKey;
    }

    public QuranTranslation() {
    }

    public String getQuranTranslation() {
        return quranTranslation;
    }

    public void setQuranTranslation(String quranTranslation) {
        this.quranTranslation = quranTranslation;
    }

    public String getQuranTranslationAuthor() {
        return quranTranslationAuthor;
    }

    public void setQuranTranslationAuthor(String quranTranslationAuthor) {
        this.quranTranslationAuthor = quranTranslationAuthor;
    }

    public String getDownloadKey() {
        return downloadKey;
    }

    public void setDownloadKey(String downloadKey) {
        this.downloadKey = downloadKey;
    }

    public int getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(int countryFlag) {
        this.countryFlag = countryFlag;
    }
}
