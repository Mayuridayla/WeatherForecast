package com.demo.weatherforecast.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.weatherforecast.R;
import com.demo.weatherforecast.data.Repository;
import com.demo.weatherforecast.data.WeatherItem;

import java.util.List;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {
    private Handler mHandler;
    private WeatherPresenter mWeatherPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mHandler = new Handler();

        getSupportActionBar().setElevation(0f);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorMessage = findViewById(R.id.errorMessage);


        Repository repository = new Repository();
        mWeatherPresenter = new WeatherPresenter(this, repository);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mWeatherPresenter.init();
    }

    @Override
    public void initView() {
        errorMessage.setVisibility(View.GONE);
    }

    @Override
    public void displayData(final List<WeatherItem> weatherItems) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // remove progress bar and error message
                errorMessage.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                // display data
                WeatherAdapter weatherAdapter = new WeatherAdapter(weatherItems);
                recyclerView.setAdapter(weatherAdapter);
            }
        });
    }

    @Override
    public void displayError() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }
}
