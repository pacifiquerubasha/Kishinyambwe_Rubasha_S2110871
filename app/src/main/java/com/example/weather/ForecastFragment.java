package com.example.weather;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class ForecastFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String locationName;
    private String locationUrl;
    private RelativeLayout progressBar;

    private TextView forecastDateToday;
    private TextView forecastWindToday;
    private TextView forecastWindDirectionToday;
    private TextView pollutionToday;
    private TextView forecastPressureToday;
    private TextView forecastHumidityToday;
    private TextView temperatureMinToday;
    private TextView uvIndexToday;
    private TextView visibilityToday;

    private TextView airQualityIndexToday;

    private ForecastDetails forecastDetails;

    private GridView forecastListView;

    private ScrollView detailsMainContainer;

    private ForecastAdapter adapter;



    public ForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
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

        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        forecastDetails = new ForecastDetails();
        progressBar = view.findViewById(R.id.progressBar);
        detailsMainContainer = view.findViewById(R.id.detailsMainContainer);

        forecastPressureToday = view.findViewById(R.id.pressure);
        temperatureMinToday = view.findViewById(R.id.temperatureMin);
        uvIndexToday = view.findViewById(R.id.uvIndex);
        visibilityToday = view.findViewById(R.id.visibility);
        airQualityIndexToday = view.findViewById(R.id.airQuality);
        forecastHumidityToday = view.findViewById(R.id.humidity);
        forecastWindToday = view.findViewById(R.id.windSpeed);
        forecastWindDirectionToday = view.findViewById(R.id.windDirection);
        pollutionToday = view.findViewById(R.id.pollution);

        forecastListView = view.findViewById(R.id.forecastListView);
        adapter = new ForecastAdapter(getContext(), new ArrayList<>());
        forecastListView.setAdapter(adapter);
        ViewGroup.LayoutParams params = forecastListView.getLayoutParams();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            forecastListView.setNumColumns(3);
        } else {
            params.height = 680;
            forecastListView.setNumColumns(1);
        }


        ImageButton backImageView = view.findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("selectedLocation")) {
            LocationItem selectedLocation = bundle.getParcelable("selectedLocation");
            locationUrl = selectedLocation.getLocationUrl();
            locationName = selectedLocation.getLocationName();
        }

        FetchWeatherDataTask task = new FetchWeatherDataTask();
        task.execute();

        return view;
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

                forecastDetails = parseWeatherData(parser);

                inputStream.close();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (forecastDetails != null) {
                adapter.clear();
                adapter.addAll(forecastDetails.getForecastList());
                adapter.notifyDataSetChanged();

                forecastPressureToday.setText(forecastDetails.getForecastPressure());
                temperatureMinToday.setText(forecastDetails.getTemperatureMinToday());
                uvIndexToday.setText(forecastDetails.getUvRiskToday());
                visibilityToday.setText(forecastDetails.getVisibilityToday());
                airQualityIndexToday.setText(forecastDetails.getAirQualityIndex());
                forecastHumidityToday.setText(forecastDetails.getForecastHumidity());
                forecastWindToday.setText(forecastDetails.getForecastWind());
                forecastWindDirectionToday.setText(forecastDetails.getWindDirectionToday());
                pollutionToday.setText(forecastDetails.getPollutionToday());

                detailsMainContainer.setVisibility(View.VISIBLE);

            } else {
                // Handle null case or show error message
                Log.e("WeatherDetails", "Weather details are null");
            }
        }

        private ForecastDetails parseWeatherData(XmlPullParser parser) throws IOException, XmlPullParserException {
            ForecastDetails forecastDetails = new ForecastDetails();
            List<String[]> forecastList = new ArrayList<>();

            int eventType = parser.getEventType();
            String[] currentDayData = null;
            boolean isFirstItemProcessed = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                    currentDayData = new String[3];
                } else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("title") && currentDayData != null) {
                    String title = parser.nextText().trim();
                    // Extract day, weather, minTemp from title
                    if (title.contains(":")) {
                        String[] parts = title.split(":");
                        if (parts.length >= 2) {
                            String day = parts[0].trim();
                            String[] weatherTempParts = parts[1].trim().split(",");
                            String weather = weatherTempParts[0].trim();
                            String minTemp = parts[2].split(" ")[1].trim();

                            currentDayData[0] = day;
                            currentDayData[1] = weather;
                            currentDayData[2] = minTemp;
                        }
                    }
                }else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("description") && !isFirstItemProcessed && currentDayData != null) {
                    String description = parser.nextText().trim();
                    parseDescriptionData(description, forecastDetails);

                    isFirstItemProcessed = true;
                }  else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("item") && currentDayData != null) {
                    forecastList.add(currentDayData);
                    currentDayData = null;
                }
                eventType = parser.next();
            }

            List<String[]> forecastArray = new ArrayList<>(forecastList);
            forecastDetails.setForecastList(forecastArray);

            return forecastDetails;
        }

        private void parseDescriptionData(String description, ForecastDetails forecastDetails) {
            String[] descParts = description.split(",");
            for (String part : descParts) {
                String value = part.trim().split(":")[1].trim();
                switch (part.trim().split(":")[0].trim()) {
                    case "Wind Speed":
                        forecastDetails.setForecastWind(value);
                        break;
                    case "Pressure":
                        forecastDetails.setForecastPressure(value);
                        break;
                    case "Humidity":
                        forecastDetails.setForecastHumidity(value);
                        break;
                    case "Minimum Temperature":
                        forecastDetails.setTemperatureMinToday(value);
                        break;
                    case "UV Risk":
                        forecastDetails.setUvRiskToday(value);
                        break;
                    case "Pollution":
                        forecastDetails.setPollutionToday(value);
                        break;
                    case "Sunrise":
                        forecastDetails.setSunriseToday(value);
                        break;
                    case "Sunset":
                        forecastDetails.setSunsetToday(value);
                        break;
                    case "Visibility":
                        forecastDetails.setVisibilityToday(value);
                        break;
                    case "Wind Direction":
                        forecastDetails.setWindDirectionToday(value);
                        break;
                    default:
                        break;
                }
            }
        }


    }
}