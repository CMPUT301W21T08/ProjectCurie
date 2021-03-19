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

    private Experiment experiment;

    public ExperimentDataFragment() {
    }

    public static ExperimentDataFragment newInstance(Experiment experiment) {
        ExperimentDataFragment fragment = new ExperimentDataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("experiment", experiment);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.experiment = (Experiment) getArguments().getSerializable("experiment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_experiment_data, container, false);
       TrialController controller = new TrialController(experiment, view);
       controller.postStatistics();
       return view;
    }
}