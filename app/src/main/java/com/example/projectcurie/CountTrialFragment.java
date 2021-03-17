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

public class CountTrialFragment extends Fragment {
    private String countResult;
    private TextView result;
    private Button countButton;
    private Button addBarcodeButton;
    private Button generateQRButton;
    private CountTrialFragment.CountTrialFragmentInteractionListener listener;

    public interface CountTrialFragmentInteractionListener {
        void uploadCountTrial();
        void addCountBarcode(String barcodeString);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CountTrialFragment.CountTrialFragmentInteractionListener){
            listener = (CountTrialFragment.CountTrialFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_count_trial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        countButton = view.findViewById(R.id.countTrialButton);
        generateQRButton = view.findViewById(R.id.countTrialGenerateQRButton);
        addBarcodeButton = view.findViewById(R.id.countTrialSubmitBarcodeButton);
        EditText barcodeInput = view.findViewById(R.id.binomialTrialBarcodeEditText);


        countButton.setOnClickListener(v -> listener.uploadCountTrial());


        addBarcodeButton.setOnClickListener(v -> {
            String barcode = barcodeInput.getText().toString();
            listener.addCountBarcode(barcode);
        });

    }
}