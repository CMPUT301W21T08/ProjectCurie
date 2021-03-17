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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class SubmitTrialActivity extends AppCompatActivity implements
        BinomialTrialFragment.BinomialTrialFragmentInteractionListener,
        IntegerCountTrialFragment.IntegerCountTrialFragmentInteractionListener,
        CountTrialFragment.CountTrialFragmentInteractionListener,
        MeasurementTrialFragment.MeasurementTrialFragmentInteractionListener {

    private FrameLayout fragmentLayout;
    private Experiment experiment;
    private ExperimentStatistics results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trial);
        fragmentLayout = findViewById(R.id.trialFragmentLayout);

        /* Grab Data From Intent */
        this.experiment = (Experiment) getIntent().getSerializableExtra("experiment");
        this.results = (ExperimentStatistics) getIntent().getSerializableExtra("trials");

        /* Display Appropriate Fragment Depending On Experiment Type */
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
    public void uploadBinomialTrial(boolean value) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        results.addTrial(new BinomialTrial(this.experiment.getTitle(), App.getUser().getUsername(), value));
        db.collection("trials")
                .document(this.experiment.getTitle())
                .set(results);
        Toast.makeText(this, "Trial Submitted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addBinomialBarcode(String barcodeString, boolean value) {

    }

    @Override
    public void uploadIntegerCountTrial(int value) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        results.addTrial(new IntegerCountTrial(this.experiment.getTitle(), App.getUser().getUsername(), value));
        db.collection("trials")
                .document(this.experiment.getTitle())
                .set(results);
        Toast.makeText(this, "Trial Submitted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addIntCountBarcode(String barcodeString, int value) {

    }

    @Override
    public void uploadCountTrial() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        results.addTrial(new CountTrial(this.experiment.getTitle(), App.getUser().getUsername()));
        db.collection("trials")
                .document(this.experiment.getTitle())
                .set(results);
        Toast.makeText(this, "Trial Submitted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addCountBarcode(String barcodeString) {

    }

    @Override
    public void uploadMeasurementTrial(double value) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        results.addTrial(new MeasurementTrial(this.experiment.getTitle(), App.getUser().getUsername(), value));
        db.collection("trials")
                .document(this.experiment.getTitle())
                .set(results);
        Toast.makeText(this, "Trial Submitted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addMeasurementBarcode(String barcodeString, double value) {

    }
}