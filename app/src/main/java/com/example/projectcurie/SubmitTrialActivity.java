package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

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
        BinomialTrial binomialTrial;
        if (experiment.isGeolocationRequired()) {
            GeoLocation location = new GeoLocation(this);
            binomialTrial = new BinomialTrial(this.experiment.getTitle(), App.getUser().getUsername(), location.getLocation(), value);
        } else {
            binomialTrial = new BinomialTrial(this.experiment.getTitle(), App.getUser().getUsername(), value);
        }
        uploadTrial(experiment.getTitle(), binomialTrial);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addBinomialBarcode(String barcodeString, boolean value) {

    }

    @Override
    public void uploadIntegerCountTrial(int value) {
        IntegerCountTrial integerCountTrial;
        if (experiment.isGeolocationRequired()) {
            GeoLocation location = new GeoLocation(this);
            integerCountTrial = new IntegerCountTrial(this.experiment.getTitle(), App.getUser().getUsername(), location.getLocation(), value);
        } else {
            integerCountTrial = new IntegerCountTrial(this.experiment.getTitle(), App.getUser().getUsername(), value);
        }
        uploadTrial(experiment.getTitle(), integerCountTrial);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addIntCountBarcode(String barcodeString, int value) {

    }

    @Override
    public void uploadCountTrial() {
        CountTrial countTrial;
        if (experiment.isGeolocationRequired()) {
            GeoLocation location = new GeoLocation(this);
            countTrial = new CountTrial(this.experiment.getTitle(), App.getUser().getUsername(), location.getLocation());
        } else {
            countTrial = new CountTrial(this.experiment.getTitle(), App.getUser().getUsername());
        }
        uploadTrial(experiment.getTitle(), countTrial);
        Toast.makeText(this, "Trial Submitted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addCountBarcode(String barcodeString) {

    }

    @Override
    public void uploadMeasurementTrial(double value) {
        MeasurementTrial measurementTrial;
        if (experiment.isGeolocationRequired()) {
            GeoLocation location = new GeoLocation(this);
            measurementTrial = new MeasurementTrial(this.experiment.getTitle(), App.getUser().getUsername(), location.getLocation(), value);
        } else {
            measurementTrial = new MeasurementTrial(this.experiment.getTitle(), App.getUser().getUsername(), value);
        }
        uploadTrial(experiment.getTitle(), measurementTrial);
        Toast.makeText(this, "Trial Submitted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void addMeasurementBarcode(String barcodeString, double value) {

    }

    private void uploadTrial(String experiment, Trial trial) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .document(experiment)
                .collection("trials")
                .document()
                .set(trial);

    }
}