package com.demo.weatherforecast.data;


public class WeatherItem {

    private final long mTimestamp;
    private final String mForecast;
    private String mTemperature;



    public WeatherItem(long timeStamp, String description, String temperature) {
        mTimestamp = timeStamp;
        mForecast = description;
        mTemperature = temperature;

    }
    public String getmTemperature() {
        return mTemperature;
    }

    public void setmTemperature(String mTemperature) {
        this.mTemperature = mTemperature;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public String getForecast() {
        return mForecast;
    }

}
