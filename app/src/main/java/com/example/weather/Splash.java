package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.weather.databinding.ActivityMainBinding;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class Splash extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("onboarding", MODE_PRIVATE);
                boolean isFirstTime = prefs.getBoolean("isFirstTime", true);

                Intent intent;
                if (isFirstTime) {
                    intent = new Intent(Splash.this, Onboarding.class);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isFirstTime", false);
                    editor.apply();
                } else {
                    intent = new Intent(Splash.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);


    }

}