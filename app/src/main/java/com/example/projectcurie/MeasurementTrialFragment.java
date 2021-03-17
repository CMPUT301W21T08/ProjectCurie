package com.example.projectcurie;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MeasurementTrialFragment extends Fragment {

    private MeasurementTrialFragmentInteractionListener listener;
    private EditText measurementEditText;
    private EditText barcodeEditText;
    private Button submitTrialButton;
    private Button addBarcodeButton;

    public interface MeasurementTrialFragmentInteractionListener {
        void uploadMeasurementTrial(double value);
        void addMeasurementBarcode(String barcodeString, double value);
    }

    public MeasurementTrialFragment() {
        // Required empty public constructor
    }

    public static MeasurementTrialFragment newInstance(String param1, String param2) {
        MeasurementTrialFragment fragment = new MeasurementTrialFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MeasurementTrialFragmentInteractionListener){
            listener = (MeasurementTrialFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MeasurementTrialFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measurement_trial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        measurementEditText = view.findViewById(R.id.measurementTrialEditText);
        barcodeEditText = view.findViewById(R.id.measurementTrialBarcodeEditText);
        submitTrialButton = view.findViewById(R.id.measurementTrialSubmitButton);
        addBarcodeButton = view.findViewById(R.id.measurementTrialSubmitBarcodeButton);

        submitTrialButton.setOnClickListener(v -> {
            double value = Double.parseDouble(measurementEditText.getText().toString());
            listener.uploadMeasurementTrial(value);
        });

        addBarcodeButton.setOnClickListener(v -> {
            double value = Double.parseDouble(measurementEditText.getText().toString());
            listener.addMeasurementBarcode(barcodeEditText.getText().toString(), value);
        });
    }
}