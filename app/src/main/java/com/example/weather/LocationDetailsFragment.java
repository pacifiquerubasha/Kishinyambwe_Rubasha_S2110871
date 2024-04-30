package com.example.weather;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class LocationDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView detailLocationName;
    private TextView detailTemp;
    private TextView detailWeather;
    private TextView detailDate;
    private TextView detailWind;
    private TextView detailPressure;
    private TextView detailHumidity;
    private TextView temperatureMin;
    private TextView temperatureMax;

    private TextView detailAirQualityIndex;
    private ImageView forecastImage;

    private ScrollView detailsMainContainer;

    private String locationUrl = "";
    private RelativeLayout progressBar;

    private WeatherDetails weatherDetails;

    private LinearLayout forecastDetailsLayout;
    private LinearLayout airQualityDetailsLayout;
    private TextView forecastToggle;
    private TextView airQualityToggle;

    private TextView sunrise;
    private TextView sunset;


    public LocationDetailsFragment() {
        // Required empty public constructor
    }
    public static LocationDetailsFragment newInstance(String param1, String param2) {
        LocationDetailsFragment fragment = new LocationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_details, container, false);

        detailLocationName = view.findViewById(R.id.detailLocationName);
        detailTemp = view.findViewById(R.id.detailTemp);
        detailWeather = view.findViewById(R.id.detailWeather);
        detailDate = view.findViewById(R.id.detailDate);
        detailWind = view.findViewById(R.id.detailWind);
        detailPressure = view.findViewById(R.id.detailPressure);
        detailHumidity = view.findViewById(R.id.detailHumidity);
        temperatureMin = view.findViewById(R.id.temperatureMin);
        temperatureMax = view.findViewById(R.id.temperatureMax);
        detailAirQualityIndex = view.findViewById(R.id.detailAirQualityIndex);
        forecastImage = view.findViewById(R.id.forecastImage);
        sunrise = view.findViewById(R.id.sunrise);
        sunset = view.findViewById(R.id.sunset);

        detailsMainContainer = view.findViewById(R.id.details_fragment_container);


        progressBar = view.findViewById(R.id.progressBar);
        weatherDetails = new WeatherDetails();

        ImageView backImageView = view.findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        forecastDetailsLayout = view.findViewById(R.id.forecastDetailsLayout);
        airQualityDetailsLayout = view.findViewById(R.id.airQualityDetailsLayout);
        forecastToggle = view.findViewById(R.id.forecastToggle);
        airQualityToggle = view.findViewById(R.id.airQualityToggle);

        // Set click listeners
        forecastToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDetails(true);
            }
        });

        airQualityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDetails(false);
            }
        });

        // Initially show forecast details
        toggleDetails(true);

        // Retrieve passed data
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("selectedLocation")) {
            LocationItem selectedLocation = bundle.getParcelable("selectedLocation");
            locationUrl = selectedLocation.getLocationUrl();
            detailLocationName.setText(selectedLocation.getLocationName());
        }

        FetchWeatherDataTask task = new FetchWeatherDataTask();
        task.execute();

        TextView viewForecastText = view.findViewById(R.id.viewForecastText);
        //add click listener to view forecast text
        viewForecastText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationItem selectedItem = new LocationItem();
                selectedItem.setLocationUrl(locationUrl);
                selectedItem.setLocationName(detailLocationName.getText().toString());

                // Replace current fragment with Details Fragment and pass data
                Fragment forecastFragment = new ForecastFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedLocation", selectedItem);
                forecastFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, forecastFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        return view;
    }

    private void toggleDetails(boolean showForecast) {
        if (showForecast) {
            forecastDetailsLayout.setVisibility(View.VISIBLE);
            airQualityDetailsLayout.setVisibility(View.GONE);
            forecastToggle.setBackgroundResource(R.drawable.selected_left_toggle_bg);
            airQualityToggle.setBackgroundResource(android.R.color.transparent);
        } else {
            forecastDetailsLayout.setVisibility(View.GONE);
            airQualityDetailsLayout.setVisibility(View.VISIBLE);
            forecastToggle.setBackgroundResource(android.R.color.transparent);
            airQualityToggle.setBackgroundResource(R.drawable.selected_right_toggle_bg);
        }
    }


    private class FetchWeatherDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            detailsMainContainer.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(locationUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                // Parse XML data
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, null);

                weatherDetails = parseTodayWeather(parser);

                inputStream.close();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (weatherDetails != null) {
                // Populate TextViews with weather data
                detailTemp.setText(weatherDetails.getDetailTemp());
                detailWeather.setText(weatherDetails.getDetailWeather());
                detailDate.setText(weatherDetails.getDetailDate());
                detailWind.setText(weatherDetails.getDetailWind());
                detailPressure.setText(weatherDetails.getDetailPressure());
                detailHumidity.setText(weatherDetails.getDetailHumidity());
                temperatureMin.setText(weatherDetails.getTemperatureMin());
                temperatureMax.setText(weatherDetails.getTemperatureMax());
                detailAirQualityIndex.setText(weatherDetails.getAirQualityIndex());
                sunrise.setText(weatherDetails.getSunrise());
                sunset.setText(weatherDetails.getSunset());

                int iconResId = getIconResId(weatherDetails.getDetailWeather());
                forecastImage.setImageResource(iconResId);
                detailsMainContainer.setVisibility(View.VISIBLE);

            } else {
                // Handle null case or show error message
                Log.e("WeatherDetails", "Weather details are null");
            }
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

        private WeatherDetails parseTodayWeather(XmlPullParser parser) throws IOException, XmlPullParserException {
            WeatherDetails weatherDetails = new WeatherDetails(); // Create a WeatherDetails object to hold parsed data
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("item"))) {
                        if (eventType == XmlPullParser.START_TAG && parser.getName().equals("title")) {
                            String title = parser.nextText();

                            // Split the title to extract specific weather details
                            String[] parts = title.split(",");
                            for (String part : parts) {
                                Log.d("Weather DETAILS==", "Part: " + part);
                                if (part.contains("Minimum Temperature")) {
                                    weatherDetails.setTemperatureMin(part.split(" ")[3].trim());
                                    weatherDetails.setDetailTemp(part.split(" ")[3].trim());

                                    if (part.contains("Maximum Temperature")){
                                        weatherDetails.setTemperatureMax(part.split(" ")[7].trim());
                                    }
                                }
                                else {
                                    weatherDetails.setDetailWeather(part.split(":")[1].trim());
                                }
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
                            Date date = new Date();
                            String formattedDate = sdf.format(date);
                            weatherDetails.setDetailDate(formattedDate);

                        } else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("description")) {
                            // Parse description to extract additional weather details
                            String description = parser.nextText();
                            String[] descParts = description.split(",");
                            for (String part : descParts) {
                                if (part.contains("Wind Speed")) {
                                    weatherDetails.setDetailWind(part.trim().split(":")[1]);
                                } else if (part.contains("Pressure")) {
                                    weatherDetails.setDetailPressure(part.trim().split(":")[1]);
                                } else if (part.contains("Humidity")) {
                                    weatherDetails.setDetailHumidity(part.trim().split(":")[1]);
                                } else if (part.contains("Pollution")) {
                                    weatherDetails.setAirQualityIndex(part.trim().split(":")[1]);
                                } else if (part.contains("Sunrise")) {
                                    weatherDetails.setSunrise(part.split(" ")[2]);
                                } else if (part.contains("Sunset")) {
                                    weatherDetails.setSunset(part.split(" ")[2]);
                                }
                            }
                        }
                        eventType = parser.next();
                    }
                    break;
                }
                eventType = parser.next();
            }
            return weatherDetails;
        }

    }
}