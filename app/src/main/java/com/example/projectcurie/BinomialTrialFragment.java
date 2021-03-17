package com.example.projectcurie;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BinomialTrialFragment extends Fragment {
    private EditText barcode;
    private String binomialResult;
    private TextView result;
    private Switch binomialSwitch;
    private Button addBarcodeButton;
    private Button generateQRButton;
    private Button submitButton;
    private BinomialTrialFragment.BinomialTrialFragmentInteractionListener listener;


    public interface BinomialTrialFragmentInteractionListener {
        void uploadBinomialTrial(String resultString);
        void addBinomialBarcode(String barcodeString);

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_binomial_trial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binomialSwitch = (Switch) view.findViewById(R.id.binomial_switch);
        generateQRButton = (Button) view.findViewById(R.id.binomialTrialGenerateQRButton);
        submitButton = (Button) view.findViewById(R.id.binomialTrialSubmitButton);
        addBarcodeButton = (Button) view.findViewById(R.id.binomialTrialSubmitBarcodeButton);
        EditText barcodeInput = (EditText) view.findViewById(R.id.binomialTrialBarcodeEditText);
        result = (TextView) view.findViewById(R.id.binomialResult);


        binomialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binomialSwitch.isChecked()) {
                    result.setText(binomialSwitch.getTextOn().toString()); // sets result to FAIL
                } else {
                    result.setText(binomialSwitch.getTextOff().toString()); // sets result to PASS
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binomialSwitch.isChecked()) {
                    binomialResult = binomialSwitch.getTextOn().toString(); // sets result to FAIL
                } else {
                    binomialResult = binomialSwitch.getTextOff().toString(); // sets result to PASS
                }

                listener.uploadBinomialTrial(binomialResult);
            }
        });

        addBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = barcodeInput.toString();
                listener.addBinomialBarcode(barcode);
            }
        });

    }
}
