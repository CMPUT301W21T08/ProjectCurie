package com.example.projectcurie;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.app.Fragment;

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
        void uploadTrial(String resultString);
        void addBarcode(String barcodeString);

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
        generateQRButton = (Button) view.findViewById(R.id.generate_qr_button);
        submitButton = (Button) view.findViewById(R.id.submit_button);
        addBarcodeButton = (Button) view.findViewById(R.id.submit_button);
        EditText barcodeInput = (EditText) view.findViewById(R.id.barcode_input_editText);
        result = (TextView) view.findViewById(R.id.binomial_result);


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

                listener.uploadTrial(binomialResult);
            }
        });

        addBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = barcodeInput.toString();
                listener.addBarcode(barcode);
            }
        });

    }
}
