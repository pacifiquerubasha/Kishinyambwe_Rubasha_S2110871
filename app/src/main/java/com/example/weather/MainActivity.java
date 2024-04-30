package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;


import com.example.weather.databinding.ActivityMainBinding;

import java.util.Calendar;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean isConnected = true;
    private RelativeLayout noInternetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View noInternetView = getLayoutInflater().inflate(R.layout.no_internet, null, false);
        noInternetLayout = noInternetView.findViewById(R.id.no_internet_layout);

        Button retryButton = noInternetView.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(v -> checkInternetConnection());

        checkInternetConnection();
        NotificationHelper.createNotificationChannel(this);


        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        if (!preferences.contains("morningUpdateTime") || !preferences.contains("eveningUpdateTime")) {
            // Set default values if not set
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("morningUpdateTime", 8); // Default to 08:00
            editor.putInt("eveningUpdateTime", 20); // Default to 20:00
            editor.apply();
        }

        // Schedule updates based on the user's preferences or defaults
        scheduleUpdates();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home_icon:
                    replaceFragment(new HomeFragment(isConnected));
                    break;
                case R.id.location_icon:
                    replaceFragment(new LocationFragment(isConnected));
                    break;
                case R.id.about_icon:
                    replaceFragment(new AboutFragment());
                    break;
            }

            return true;
        });
    }

    private void scheduleUpdates() {
        // Retrieve the user's preferred update times or use defaults
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int morningUpdateTime = preferences.getInt("morningUpdateTime", 8);
        int eveningUpdateTime = preferences.getInt("eveningUpdateTime", 20);

        // Schedule updates for both times
        scheduleUpdate(morningUpdateTime, 0);
        scheduleUpdate(eveningUpdateTime, 0);
    }

    private void scheduleUpdate(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm for the selected time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Set the alarm to repeat daily
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkInternetConnection() {
        if (isNetworkAvailable()) {
            binding.frameLayout.removeView(noInternetLayout);
            replaceFragment(new HomeFragment(isConnected));
            isConnected = true;
        } else {
            Log.d("INTERNET", "NO INTERNET");
            if(noInternetLayout.getParent() != null) {
                ((ViewGroup)noInternetLayout.getParent()).removeView(noInternetLayout);
            }
            binding.frameLayout.addView(noInternetLayout);
            isConnected = false;
        }
    }


}