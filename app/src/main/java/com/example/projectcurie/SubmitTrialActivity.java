package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.LatLng;


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
        DocumentReference ref = db.collection("trials").document(this.experiment.getTitle());
        db.runTransaction(transaction -> {
            ExperimentStatistics trials = transaction.get(ref).toObject(ExperimentStatistics.class);
            trials.addTrial(new BinomialTrial(this.experiment.getTitle(), App.getUser().getUsername(), value));
            transaction.set(ref, trials, SetOptions.merge());
            return trials;
        }).addOnSuccessListener(statistics -> {
            Toast.makeText(getApplicationContext(), "Trial Submitted!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void uploadBinomialTrial(boolean value, LatLng location) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("trials").document(this.experiment.getTitle());
        db.runTransaction(transaction -> {
            ExperimentStatistics trials = transaction.get(ref).toObject(ExperimentStatistics.class);
            trials.addTrial(new BinomialTrial(this.experiment.getTitle(), App.getUser().getUsername(), location, value));
            transaction.set(ref, trials, SetOptions.merge());
            return trials;
        }).addOnSuccessListener(statistics -> {
            Toast.makeText(getApplicationContext(), "Trial Submitted!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void addBinomialBarcode(String barcodeString, boolean value) {

    }

    @Override
    public void addBinomialBarcode(String barcodeString, LatLng location, boolean value) {

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
    public void uploadIntegerCountTrial(int value, LatLng location) {

    }

    @Override
    public void addIntCountBarcode(String barcodeString, int value) {

    }

    @Override
    public void addIntCountBarcode(String barcodeString, LatLng location, int value) {

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
    public void uploadCountTrial(LatLng location) {

    }

    @Override
    public void addCountBarcode(String barcodeString) {

    }

    @Override
    public void addCountBarcode(String barcodeString, LatLng location) {

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
    public void uploadMeasurementTrial(double value, LatLng location) {

    }

    @Override
    public void addMeasurementBarcode(String barcodeString, double value) {

    }

    @Override
    public void addMeasurementBarcode(String barcodeString, LatLng location, double value) {

    }
}