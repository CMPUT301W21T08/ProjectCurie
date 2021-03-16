package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import java.io.IOException;

public class SubmitTrialActivity extends AppCompatActivity {

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
                fragmentTransaction.replace(R.id.trialFragmentLayout, new CountTrialFragment());
                break;
            case (1):
                fragmentTransaction.replace(R.id.trialFragmentLayout, new IntegerCountTrialFragment());
                break;
            case (2):
                fragmentTransaction.replace(R.id.trialFragmentLayout, new MeasurementTrialFragment());
                break;
        }
        fragmentTransaction.commit();
    }
}