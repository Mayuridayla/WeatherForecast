package com.demo.weatherforecast.ui;


import com.demo.weatherforecast.data.WeatherItem;

import java.util.List;

public interface WeatherContract {

    interface View {

        void initView();

        void displayData(List<WeatherItem> weatherItems);

        void displayError();

    }

    interface Presenter {

        void init();

        void setData(List<WeatherItem> weatherItems);

        void setError();

    }

}
