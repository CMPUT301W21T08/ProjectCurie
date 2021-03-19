package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity allows the user to submit a trial results to a given experiment. The UI which is
 * presented to the user depends on the type of experiment (Binomial, Count, Integer Count, Measurement).
 */
public class SubmitTrialActivity extends AppCompatActivity implements
        BinomialTrialFragment.BinomialTrialFragmentInteractionListener,
        IntegerCountTrialFragment.IntegerCountTrialFragmentInteractionListener,
        CountTrialFragment.CountTrialFragmentInteractionListener,
        MeasurementTrialFragment.MeasurementTrialFragmentInteractionListener {

    private FrameLayout fragmentLayout;
    private Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trial);
        fragmentLayout = findViewById(R.id.trialFragmentLayout);

        /* Grab Data From Intent */
        this.experiment = (Experiment) getIntent().getSerializableExtra("experiment");

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
    }

    @Override
    public void addMeasurementBarcode(String barcodeString, double value) {

    }

    /* Upload A Trial To The Database */
    private void uploadTrial(String experiment, Trial trial) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .document(experiment)
                .collection("trials")
                .document()
                .set(trial)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Trial Submitted", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener( e -> {
                    Log.e("Error", e.getLocalizedMessage());
                });
    }
}