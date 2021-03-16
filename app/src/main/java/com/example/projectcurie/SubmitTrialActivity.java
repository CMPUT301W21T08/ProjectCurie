package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;

public class SubmitTrialActivity extends AppCompatActivity implements BinomialTrialFragment.BinomialTrialFragmentInteractionListener,
                                                                        IntegerCountTrialFragment.IntegerCountTrialFragmentInteractionListener,
                                                                        CountTrialFragment.CountTrialFragmentInteractionListener{

    FrameLayout fragmentLayout;
    ConstraintLayout submitSuccessLayout;
    Experiment experiment;
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trial);
        fragmentLayout = findViewById(R.id.trialFragmentLayout);

        // Implement home button so the activity would switch to Main Activity
        homeButton = (Button) findViewById(R.id.submitTrialHomeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
        //TO DO: Upload result to its respective trial class
        fragmentLayout.setVisibility(View.GONE);
        submitSuccessLayout = findViewById(R.id.submitSuccessLayout);
        submitSuccessLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addBinomialBarcode(String barcodeString) {

    }

    @Override
    public void uploadIntegerCountTrial(String resultString) {
        fragmentLayout.setVisibility(View.GONE);
        submitSuccessLayout = findViewById(R.id.submitSuccessLayout);
        submitSuccessLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addIntCountBarcode(String barcodeString) {

    }

    @Override
    public void uploadCountTrial(String resultString) {
        fragmentLayout.setVisibility(View.GONE);
        submitSuccessLayout = findViewById(R.id.submitSuccessLayout);
        submitSuccessLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addCountBarcode(String barcodeString) {

    }
}