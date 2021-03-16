package com.example.projectcurie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CountTrialFragment extends Fragment {
    public CountTrialFragment() {
        // Required empty public constructor
    }
    public static CountTrialFragment newInstance(String param1, String param2) {
        CountTrialFragment fragment = new CountTrialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_count_trial, container, false);
    }
}