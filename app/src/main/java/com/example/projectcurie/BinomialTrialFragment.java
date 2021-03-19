package com.example.projectcurie;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.type.LatLng;

/**
 * This Fragment provides a UI interface for submitting a binomial trial to an experiment which
 * requires it.
 * @author Joshua Billson
 * @author Paul Cleofas
 */
public class BinomialTrialFragment extends Fragment {
    private BinomialTrialFragment.BinomialTrialFragmentInteractionListener listener;


    /**
     * Implemented by this Fragment's parent Activity to enable submitting new trials to the
     * FireStore database and registering bar codes.
     */
    public interface BinomialTrialFragmentInteractionListener {
        void uploadBinomialTrial(boolean value);
        void addBinomialBarcode(String barcodeString, boolean value);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BinomialTrialFragment.BinomialTrialFragmentInteractionListener){
            listener = (BinomialTrialFragment.BinomialTrialFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_binomial_trial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /* Grab Widgets */
        SwitchCompat binomialSwitch = view.findViewById(R.id.binomialTrialSwitch);
        Button generateQRButton = view.findViewById(R.id.binomialTrialGenerateQRButton);
        Button submitButton = view.findViewById(R.id.binomialTrialSubmitButton);
        Button addBarcodeButton = view.findViewById(R.id.binomialTrialSubmitBarcodeButton);
        EditText barcodeInput = view.findViewById(R.id.binomialTrialBarcodeEditText);
        TextView result = view.findViewById(R.id.binomialResult);


        /* When Switch Is Clicked, Toggle The On-Screen Prompt Between "PASS" & "FAIL" */
        binomialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> result.setText(binomialSwitch.isChecked() ? "PASS" : "FAIL"));

        /* On Clicking Submit, Upload A Binomial Trial To The FireStore Database */
        submitButton.setOnClickListener(v -> listener.uploadBinomialTrial(binomialSwitch.isChecked()));

        /* Register A Barcode With A Specific Trial Result For This Experiment */
        addBarcodeButton.setOnClickListener(v -> {
            String barcode = barcodeInput.toString();
            listener.addBinomialBarcode(barcode, binomialSwitch.isChecked());
        });

    }
}
