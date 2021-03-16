package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import java.io.IOException;

public class SubmitTrialActivity extends AppCompatActivity implements BinomialTrialFragment.BinomialTrialFragmentInteractionListener,
                                                                        IntegerCountTrialFragment.IntegerCountTrialFragmentInteractionListener{

    FrameLayout fragmentLayout;
    Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trial);
        fragmentLayout = findViewById(R.id.trialFragmentLayout);

        /* Grab Data From Intent */
        try {
            String serialString = getIntent().getStringExtra("experiment");
            this.experiment = (Experiment) ObjectSerializer.deserialize(serialString);
        } catch (IOException e) {
            Log.e("Error", "Error: Could Not Deserialize Experiment!");
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (experiment.getType().ordinal()) {
            case (0):
                fragmentTransaction.replace(fragmentLayout.getId(), new CountTrialFragment());
                break;
            case (1):
                fragmentTransaction.replace(fragmentLayout.getId(), new IntegerCountTrialFragment());
                break;
            case (2):
                fragmentTransaction.replace(fragmentLayout.getId(), new MeasurementTrialFragment());
                break;
            case (3):
                fragmentTransaction.replace(fragmentLayout.getId(), new BinomialTrialFragment());
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void uploadBinomialTrial(String resultString) {

    }

    @Override
    public void addBinomialBarcode(String barcodeString) {

    }

    @Override
    public void uploadIntegerCountTrial(String resultString) {

    }

    @Override
    public void addIntCountBarcode(String barcodeString) {

    }

}