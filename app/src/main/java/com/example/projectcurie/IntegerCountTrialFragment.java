package com.example.projectcurie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntegerCountTrialFragment extends Fragment {

    public IntegerCountTrialFragment() {
    }

    public static IntegerCountTrialFragment newInstance(String param1, String param2) {
        IntegerCountTrialFragment fragment = new IntegerCountTrialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_integer_count_trial, container, false);
    }
}