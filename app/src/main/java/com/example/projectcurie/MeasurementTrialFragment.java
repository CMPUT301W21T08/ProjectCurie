package com.example.projectcurie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MeasurementTrialFragment extends Fragment {
    public MeasurementTrialFragment() {
        // Required empty public constructor
    }

    public static MeasurementTrialFragment newInstance(String param1, String param2) {
        MeasurementTrialFragment fragment = new MeasurementTrialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measurement_trial, container, false);
    }
}