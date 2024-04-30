package com.example.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class Onboarding3 extends OnboardingMain {
    public Onboarding3() {
        super(R.drawable.onb2, R.string.onboarding_text_3);
    }

    @Override
    public int getFragmentIndex() {
        return 2;
    }
}