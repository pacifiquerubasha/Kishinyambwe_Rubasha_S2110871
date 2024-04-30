package com.example.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public abstract class OnboardingMain extends Fragment {

    private int imageResId;
    private int textResId;

    public OnboardingMain(int imageResId, int textResId) {
        this.imageResId = imageResId;
        this.textResId = textResId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_main, container, false);

        ImageView imageView = view.findViewById(R.id.image_view);
        TextView textView = view.findViewById(R.id.text_view);
        Button nextButton = view.findViewById(R.id.next_button);

        imageView.setImageResource(imageResId);
        textView.setText(textResId);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Onboarding) getActivity()).showNextFragment(getFragmentIndex());
            }
        });

        return view;
    }


    public abstract int getFragmentIndex();
}