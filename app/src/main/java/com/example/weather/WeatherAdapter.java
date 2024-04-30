package com.example.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weather.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class WeatherAdapter extends ArrayAdapter<String> implements Filterable {
    private final Context context;
    private final ArrayList<String> values;
    private Filter filter;
    private ArrayList<String> filteredValues;

    public WeatherAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.locations_list, values);
        this.context = context;
        this.values = values;
        this.filteredValues = values;

    }

    @Override
    public int getCount() {
        return filteredValues.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position >= filteredValues.size()) {
            Log.d("OUT OF BOUNDS", "getView: position=" + position + ", size=" + filteredValues.size());
            return null;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.locations_list, parent, false);

        TextView temperatureTextView = rowView.findViewById(R.id.temperatureTextView);
        TextView weatherTextView = rowView.findViewById(R.id.weatherTextView);
        TextView locationTextView = rowView.findViewById(R.id.locationTextView);
        ImageView weatherIconImageView = rowView.findViewById(R.id.weatherIcon);


        String weatherInfo = filteredValues.get(position);
        Log.d("ADAPK", "getView: position=" + position + ", size=" + filteredValues.size());

        Pattern pattern = Pattern.compile("^(.*?),\\s(.*?),\\sMinimum Temperature: (\\d+°C \\(\\d+°F\\))(?:.*?Maximum Temperature: (\\d+°C \\(\\d+°F\\)))?");

        Matcher matcher = pattern.matcher(weatherInfo);
        String weather = "";
        if (matcher.find()) {
            Log.d("WeatherAdapter", "getView: " + matcher.group(0));
            String location = matcher.group(1).trim();
            weather = matcher.group(2).trim().split(":")[1].trim();
            String minTemp = matcher.group(3).trim();
            String maxTemp = matcher.group(4) != null ? matcher.group(4).trim() : "";

            temperatureTextView.setText(minTemp);
            weatherTextView.setText(weather);
            locationTextView.setText(location);
        }

        int iconResId = getIconResId(weather);
        weatherIconImageView.setImageResource(iconResId);

        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    Log.d("ADAPTER SEARCH", "START");
                    FilterResults results = new FilterResults();
                    if (constraint == null || constraint.length() == 0) {
                        results.values = values;
                        results.count = values.size();
                    } else {
                        List<String> filteredList = new ArrayList<>();
                        for (String item : values) {
                            if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                filteredList.add(item);
                            }
                        }
                        results.values = filteredList;
                        results.count = filteredList.size();
                    }
                    Log.d("ADAPTER SEARCH", "END: " + results.count);
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredValues = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
        return filter;
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
