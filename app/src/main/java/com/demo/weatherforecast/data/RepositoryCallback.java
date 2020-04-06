package com.demo.weatherforecast.data;


import java.util.List;

public interface RepositoryCallback {

    void onDataLoaded(List<WeatherItem> weatherItems);

    void onDataFailed();

}
