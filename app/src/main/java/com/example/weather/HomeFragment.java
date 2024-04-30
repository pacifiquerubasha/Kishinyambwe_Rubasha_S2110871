package com.example.weather;

import static com.example.weather.Constants.ACTION_FETCH_WEATHER_DATA;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private ListView rssListView;
    private GridView rssListView;

    private ArrayList<String> weatherDataList;
    private ArrayAdapter<String> adapter;
    private ProgressBar progressBar;
    private Boolean isConnected = true;
    private ImageView toggleDarkMode;

    private final String[] locations = {
            "Glasgow, GB",
            "London, GB",
            "New York, US",
            "Oman, RU",
            "Mauritius, MU",
            "Bangladesh, BD",
    };

    private final String[] locationUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(Boolean isConnected) {
        if(isConnected != null)
            this.isConnected = isConnected;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rssListView = view.findViewById(R.id.listRss);
        weatherDataList = new ArrayList<>();

        adapter = new WeatherAdapter(getContext(), new ArrayList<>());
        rssListView.setAdapter(adapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rssListView.setNumColumns(2);
        } else {
            rssListView.setNumColumns(1);
        }

        rssListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String locationUrl = locationUrls[position];

                // Create a new LocationItem instance using Parcelable constructor
                LocationItem selectedItem = new LocationItem();
                selectedItem.setLocationUrl(locationUrl);
                selectedItem.setLocationName(locations[position]);

                // Replace current fragment with Details Fragment and pass data
                Fragment detailsFragment = new LocationDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedLocation", selectedItem);
                detailsFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        progressBar = view.findViewById(R.id.progressBar);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ImageView alarmMenu = view.findViewById(R.id.alarm_menu);
        alarmMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScheduleDialog();
            }
        });

        if(isConnected){
            new FetchWeatherDataTask().execute();

            EditText searchEditText = view.findViewById(R.id.searchEditText);
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    adapter.getFilter().filter(s.toString());
                }
            });
        }
        else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }

        toggleDarkMode = view.findViewById(R.id.ic_light_dark_toggle);
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            toggleDarkMode.setImageResource(R.drawable.baseline_brightness_7_24);
        } else {
            toggleDarkMode.setImageResource(R.drawable.baseline_bedtime_24);
        }

        toggleDarkMode.setOnClickListener(v -> toggleDarkMode(v));

        return view;
    }

    private void toggleDarkMode(View v) {
        // Check the current night mode
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                Log.d("DarkMode", "Turning off dark mode");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                ((ImageView) v).setImageResource(R.drawable.baseline_bedtime_24);

                break;
            case Configuration.UI_MODE_NIGHT_NO:
                Log.d("DarkMode", "Turning on dark mode");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                ((ImageView) v).setImageResource(R.drawable.baseline_brightness_7_24);
                break;
        }
    }

    private void showScheduleDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_menu);

        Button btnMorning = dialog.findViewById(R.id.btnMorning);
        Button btnEvening = dialog.findViewById(R.id.btnEvening);

        btnMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });

        btnEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });

        dialog.show();
    }


    private class FetchWeatherDataTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            rssListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> weatherDataList = new ArrayList<>();
            for (int i = 0; i < locations.length; i++) {
                String location = locations[i];
                String locationUrl = locationUrls[i];
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

                    Log.d("LOCATION", location);
                    String todayWeather = parseTodayWeather(parser);

                    if (todayWeather != null) {
                        weatherDataList.add(location + ": " + todayWeather);
                    }

                    inputStream.close();
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            return weatherDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            progressBar.setVisibility(View.GONE);
            rssListView.setVisibility(View.VISIBLE);
            adapter.clear();
            adapter.addAll(result);
            adapter.notifyDataSetChanged();
        }

        private String parseTodayWeather(XmlPullParser parser) throws IOException, XmlPullParserException {
            String todayWeather = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("item"))) {
                        if (eventType == XmlPullParser.START_TAG && parser.getName().equals("title")) {
                            String title = parser.nextText();

                            Log.d("Weather", "Title: " + title);

                            if (title.contains("Today") || title.contains("Tonight")) {
                                todayWeather = title.replace("Today: ", "").replace("Tonight: ", "");
                                break;
                            }
                        }
                        eventType = parser.next();
                    }
                    if (todayWeather != null) {
                        break;
                    }
                }
                eventType = parser.next();
            }
            return todayWeather;
        }
    }

    private void showTimePickerDialog(boolean isMorning) {
        // Use the current time as the default values for the pickers
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return the hour and minute
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Store the selected time
                        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        if (isMorning) {
                            editor.putInt("morningUpdateTime", hourOfDay);
                        } else {
                            editor.putInt("eveningUpdateTime", hourOfDay);
                        }
                        editor.apply();

                        // Schedule the update
                        scheduleUpdate(hourOfDay, minute);
                    }
                }, hour, minute, true);

        // Set the title based on whether it's for morning or evening
        if (isMorning) {
            timePickerDialog.setTitle("Select Morning Update Time");
        } else {
            timePickerDialog.setTitle("Select Evening Update Time");
        }

        timePickerDialog.show();
    }


    private void scheduleUpdate(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm for the selected time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private BroadcastReceiver weatherDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_FETCH_WEATHER_DATA.equals(intent.getAction())) {
                new FetchWeatherDataTask().execute();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(weatherDataReceiver, new IntentFilter(ACTION_FETCH_WEATHER_DATA));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(weatherDataReceiver);
    }


}