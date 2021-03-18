package com.example.projectcurie;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.type.LatLng;

/**
 * This Fragment provides a UI interface for submitting an Integer Count trial to an experiment
 * which requires it.
 * @author Joshua Billson
 * @author Paul Cleofas
 */
public class IntegerCountTrialFragment extends Fragment {

    private int counter = 0;
    private IntegerCountTrialFragmentInteractionListener listener;

    /**
     * Implemented by this Fragment's parent Activity to enable submitting new trials to the
     * FireStore database and registering bar codes.
     */
    public interface IntegerCountTrialFragmentInteractionListener {
        void uploadIntegerCountTrial(int value);
        void uploadIntegerCountTrial(int value, LatLng location);
        void addIntCountBarcode(String barcodeString, int value);
        void addIntCountBarcode(String barcodeString, LatLng location, int value);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IntegerCountTrialFragmentInteractionListener){
            listener = (IntegerCountTrialFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_integer_count_trial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /* Grab Widgets */
        Button incrementButton = view.findViewById(R.id.integerCountIncrementButton);
        Button decrementButton = view.findViewById(R.id.integerCountDecrementButton);
        Button generateQRButton = view.findViewById(R.id.binomialTrialGenerateQRButton);
        Button submitButton = view.findViewById(R.id.binomialTrialSubmitButton);
        Button addBarcodeButton = view.findViewById(R.id.binomialTrialSubmitBarcodeButton);
        TextView result = view.findViewById(R.id.integerCountTrialCounterTextView);
        EditText barcodeInput = view.findViewById(R.id.binomialTrialBarcodeEditText);


        /* Increment The UI Counter When Pressing The Positive Count Button */
        incrementButton.setOnClickListener(v -> {
            counter++;
            if (counter < 0) {
                counter = 0;
            }
            result.setText(String.valueOf(counter));
        });

        /* Decrement The UI Counter When Pressing The Negative Count Button */
        decrementButton.setOnClickListener(v -> {
            counter--;
            if (counter < 0) {
                counter = 0;
            }
            result.setText(String.valueOf(counter));
        });

        /* On Clicking Submit, Upload An Integer Count Trial To The FireStore Database */
        submitButton.setOnClickListener(v -> listener.uploadIntegerCountTrial(counter));

        /* Register A Barcode With A Specific Trial Result For This Experiment */
        addBarcodeButton.setOnClickListener(v -> {
            String barcode = barcodeInput.toString();
            listener.addIntCountBarcode(barcode, counter);
        });

    }
}
