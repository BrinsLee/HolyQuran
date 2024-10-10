package com.ihyas.soharamkarubar.models;

public class WeatherModel {

    public String dayName, temp, tempMini, tempMax, image, desc, humidity, windSpeed;

    public WeatherModel(String dayName, String temp, String tempMini, String tempMax, String image, String desc, String humidity, String windSpeed) {
        this.dayName = dayName;
        this.temp = temp;
        this.tempMini = tempMini;
        this.tempMax = tempMax;
        this.image = image;
        this.desc = desc;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public WeatherModel(String dayName, String tempMini, String tempMax, String image) {
        this.dayName = dayName;
        this.tempMini = tempMini;
        this.tempMax = tempMax;
        this.image = image;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMini() {
        return tempMini;
    }

    public void setTempMini(String tempMini) {
        this.tempMini = tempMini;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
