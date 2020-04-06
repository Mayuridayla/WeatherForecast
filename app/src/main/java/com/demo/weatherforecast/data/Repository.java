package com.demo.weatherforecast.data;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class Repository {

    private static final String LOG_TAG = "WeatherRepository";

    public void makeRequest(@NonNull RepositoryCallback repositoryCallback) {
        final WeakReference<RepositoryCallback> callbackReference
                = new WeakReference<>(repositoryCallback);
        OkHttpClient client = new OkHttpClient();
        String url = constructURL();
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                RepositoryCallback repositoryCallback = callbackReference.get();
                if (repositoryCallback != null) {
                    repositoryCallback.onDataFailed();
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response)
                    throws IOException {
                String result = response.body().string();
                RepositoryCallback repositoryCallback = callbackReference.get();
                if (repositoryCallback != null) {
                    try {
                        List<WeatherItem> weatherItems = getWeatherDataFromJson(result);
                        repositoryCallback.onDataLoaded(weatherItems);
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, e.getMessage(), e);
                        repositoryCallback.onDataFailed();
                    }
                }
            }
        });
    }

    private String constructURL() {
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?";
        final String ID_PARAM = "q";
        final String UNITS_PARAM = "units";
        final String CNT_PARAM = "cnt";
        final String APPID_PARAM = "APPID";
        //working URL: http://api.openweathermap.org/data/2.5/forecast?q=bangalore,karnataka,IN&cnt=5&appid=8473a1f2f1d65bc16f86885a1dd9efee
        final String units = "metric";
        final String apikey = "8473a1f2f1d65bc16f86885a1dd9efee";
        final int numDays = 48;

        /*numday count should e 6 for 6 days weather update  but  api gives every 3 hour data
         * so thats why i had put 48 for fetch 6 days data
         * and in last i had divided this by 8
         * */
        final String cityName = "Bangalore,karnataka,IN"; // Banglore


        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(ID_PARAM, cityName)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(CNT_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APPID_PARAM, apikey)
                .build();
        Log.e("uri", "" + uri);

        return uri.toString();
    }

    private List<WeatherItem> getWeatherDataFromJson(String response) throws JSONException {
        final List<WeatherItem> weatherItems = new ArrayList<>();

        final String JSON_LIST = "list";
        final String JSON_DT = "dt";
        final String JSON_WEATHER = "weather";
        final String JSON_DESCRIPTION = "main";


        JSONObject forecastJson = new JSONObject(response);
        Log.e("forecastJson", "" + forecastJson);

        JSONArray weatherArray = forecastJson.getJSONArray(JSON_LIST);
        Log.e("weatherArray", "" + weatherArray);

        for (int i = 8; i < weatherArray.length(); i++) {

            JSONObject dayForecast = weatherArray.getJSONObject(i);
            long timeStamp = dayForecast.getLong(JSON_DT);
            Log.e("timeStamp", "" + timeStamp);

            JSONObject weather = dayForecast.getJSONArray(JSON_WEATHER).getJSONObject(0);
            Log.e("weather", "" + weather);

            String description = weather.getString(JSON_DESCRIPTION);
            Log.e("description", "" + description);
            Log.e("dayForecast", "" + dayForecast);

            JSONObject main1 = dayForecast.getJSONObject("main");
            String temperature = main1.getString("temp");
            Log.e("temperature", "" + temperature);

            String high = main1.getString("temp_max");
            Log.e("high", "" + high);

            String low = main1.getString("temp_min");
            Log.e("low", "" + low);

            WeatherItem weatherItem = new WeatherItem(timeStamp, description, temperature);
//            WeatherItem weatherItem = new WeatherItem(timeStamp, description, high, low);
            Log.e("weatherItem", "" + weatherItem);


            if (i % 8 == 0) {
                weatherItems.add(weatherItem);
            }

        }

        return weatherItems;
    }

}
