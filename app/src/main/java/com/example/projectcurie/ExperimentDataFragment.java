package com.example.projectcurie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class currently doesn't do anything interesting.
 * This class will implement experiment data and statistics in phase 4.
 */
public class ExperimentDataFragment extends Fragment {
    public ExperimentDataFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_experiment_data, container, false);
    }
}