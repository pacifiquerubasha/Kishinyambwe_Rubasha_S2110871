package com.example.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class ForecastAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private final List<String[]> forecastData;

    public ForecastAdapter(Context context, List<String[]> forecastData) {
        super(context, R.layout.forecast_day, forecastData);
        this.context = context;
        this.forecastData = forecastData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("TEST ADA", String.valueOf(convertView));
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.forecast_day, parent, false);
        }

        TextView dayTextView = convertView.findViewById(R.id.dayTextView);
        ImageView weatherIconImageView = convertView.findViewById(R.id.weatherIcon);
        TextView tempTextView = convertView.findViewById(R.id.temperatureTextView);

        String[] dayData = forecastData.get(position);
        if (dayData != null && dayData.length >= 3) {
            Log.d("TEST ADA", "Finally here");
            dayTextView.setText(dayData[0]);
            tempTextView.setText(dayData[2]);

            int iconResId = getIconResId(dayData[1]);
            weatherIconImageView.setImageResource(iconResId);
        }

        return convertView;
    }

    public int getIconResId(String weather) {
        int iconResId;
        if (weather.toLowerCase().contains("light rain")) {
            iconResId = R.drawable.day_rain;
        }
        else if (weather.toLowerCase().contains("heavy rain")) {
            iconResId = R.drawable.tornado;
        }
        else if (weather.toLowerCase().contains("rain")) {
            iconResId = R.drawable.rain;
        }
        else if (weather.toLowerCase().contains("partly cloud")) {
            iconResId = R.drawable.day_partial_cloud;
        }
        else if (weather.toLowerCase().contains("cloud")) {
            iconResId = R.drawable.cloudy;
        }
        else if (weather.toLowerCase().contains("snow")) {
            iconResId = R.drawable.snow;
        }
        else if (weather.toLowerCase().contains("thundery")) {
            iconResId = R.drawable.rain_thunder;
        }
        else {
            iconResId = R.drawable.day_clear;
        }
        return iconResId;
    }
}