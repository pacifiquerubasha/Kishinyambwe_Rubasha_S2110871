package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class Onboarding extends AppCompatActivity {

    private static final int NUM_PAGES = 3;
    private Fragment[] fragments = new Fragment[NUM_PAGES];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize the fragments
        fragments[0] = new Onboarding1();
        fragments[1] = new Onboarding2();
        fragments[2] = new Onboarding3();

        // Show the first fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragments[0]);
        fragmentTransaction.commit();
    }

    public void showNextFragment(int currentIndex) {
        if (currentIndex < NUM_PAGES - 1) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragments[currentIndex + 1]);
            fragmentTransaction.commit();
        } else {
            // Launch MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}