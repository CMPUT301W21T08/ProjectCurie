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

public class IntegerCountTrialFragment extends Fragment {
    private String intCountResult;
    private TextView result;
    private Button incrementButton;
    private Button decrementButton;
    private Button addBarcodeButton;
    private Button generateQRButton;
    private Button submitButton;
    private int counter = 0;
    private IntegerCountTrialFragment.IntegerCountTrialFragmentInteractionListener listener;

    public interface IntegerCountTrialFragmentInteractionListener {
        void uploadIntegerCountTrial(int value);
        void addIntCountBarcode(String barcodeString, int value);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IntegerCountTrialFragment.IntegerCountTrialFragmentInteractionListener){
            listener = (IntegerCountTrialFragment.IntegerCountTrialFragmentInteractionListener) context;
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
        incrementButton = view.findViewById(R.id.integerCountIncrementButton);
        decrementButton = view.findViewById(R.id.integerCountDecrementButton);
        generateQRButton = view.findViewById(R.id.binomialTrialGenerateQRButton);
        submitButton = view.findViewById(R.id.binomialTrialSubmitButton);
        addBarcodeButton = view.findViewById(R.id.binomialTrialSubmitBarcodeButton);
        result = view.findViewById(R.id.integerCountTrialCounterTextView);
        EditText barcodeInput = view.findViewById(R.id.binomialTrialBarcodeEditText);


        incrementButton.setOnClickListener(v -> {
            counter++;
            if (counter < 0) {
                counter = 0;
            }
            result.setText(String.valueOf(counter));
        });

        decrementButton.setOnClickListener(v -> {
            counter--;
            if (counter < 0) {
                counter = 0;
            }
            result.setText(String.valueOf(counter));
        });

        submitButton.setOnClickListener(v -> {
            intCountResult = result.toString();
            listener.uploadIntegerCountTrial(counter);
        });

        addBarcodeButton.setOnClickListener(v -> {
            String barcode = barcodeInput.toString();
            listener.addIntCountBarcode(barcode, counter);
        });

    }
}
