package com.example.projectcurie;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.type.LatLng;

/**
 * This Fragment provides a UI interface for submitting a count trial to an experiment which requires it.
 * @author Joshua Billson
 * @author Paul Cleofas
 */
public class CountTrialFragment extends Fragment {
    private CountTrialFragment.CountTrialFragmentInteractionListener listener;

    /**
     * Implemented by this Fragment's parent Activity to enable submitting new trials to the
     * FireStore database and registering bar codes.
     */
    public interface CountTrialFragmentInteractionListener {
        void uploadCountTrial();
        void uploadCountTrial(LatLng location);
        void addCountBarcode(String barcodeString);
        void addCountBarcode(String barcodeString, LatLng location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CountTrialFragment.CountTrialFragmentInteractionListener){
            listener = (CountTrialFragment.CountTrialFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_count_trial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /* Grab Widgets */
        Button countButton = view.findViewById(R.id.countTrialButton);
        Button generateQRButton = view.findViewById(R.id.countTrialGenerateQRButton);
        Button addBarcodeButton = view.findViewById(R.id.countTrialSubmitBarcodeButton);
        EditText barcodeInput = view.findViewById(R.id.binomialTrialBarcodeEditText);

        /* On Clicking Submit, Upload A Count Trial To The FireStore Database */
        countButton.setOnClickListener(v -> listener.uploadCountTrial());

        /* Register A Barcode With A Specific Trial Result For This Experiment */
        addBarcodeButton.setOnClickListener(v -> {
            String barcode = barcodeInput.getText().toString();
            listener.addCountBarcode(barcode);
        });
    }
}