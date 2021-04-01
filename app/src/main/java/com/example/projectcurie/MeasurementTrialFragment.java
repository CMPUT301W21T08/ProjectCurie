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

import org.jetbrains.annotations.NotNull;

/**
 * This Fragment provides a UI interface for submitting a measurement trial to an experiment which
 * requires it.
 * @author Joshua Billson
 * @author Paul Cleofas
 */
public class MeasurementTrialFragment extends Fragment {

    private SubmitTrialActivityInterface listener;

    /** Obligatory Empty Constructor */
    public MeasurementTrialFragment() {
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof SubmitTrialActivityInterface){
            listener = (SubmitTrialActivityInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SubmitTrialActivityInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measurement_trial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /* Grab Widgets */
        EditText measurementEditText = view.findViewById(R.id.measurementTrialEditText);
        EditText barcodeEditText = view.findViewById(R.id.measurementTrialBarcodeEditText);
        Button submitTrialButton = view.findViewById(R.id.measurementTrialSubmitButton);
        Button addBarcodeButton = view.findViewById(R.id.measurementTrialSubmitBarcodeButton);
        Button generateQRButton = view.findViewById(R.id.measurementTrialGenerateQRButton);

        /* On Clicking Submit, Upload A Measurement Trial To The FireStore Database */
        submitTrialButton.setOnClickListener(v -> {
            double value = Double.parseDouble(measurementEditText.getText().toString());
            listener.uploadTrial(value);
        });

        /* Register A Barcode With A Specific Trial Result For This Experiment */
        addBarcodeButton.setOnClickListener(v -> {
            double value = Double.parseDouble(measurementEditText.getText().toString());
            listener.addBarcode(barcodeEditText.getText().toString(), value);
        });

        generateQRButton.setOnClickListener(v -> listener.addQR(Double.parseDouble(measurementEditText.getText().toString())));
    }
}