package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class implements an activity for presenting a list view of experiments. Experiments
 * can be selected to view their details and to submit trials.
 * @author Joshua Billson
 */
public class ExperimentListActivity extends AppCompatActivity {

    private ArrayList<Experiment> experiments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_list);

        /* Grab Experiments From Intent */
        try {
            experiments = (ArrayList<Experiment>) ObjectSerializer.deserialize(getIntent().getStringExtra("experiments"));
        } catch (IOException e) {
            Log.e("Error", "Error: Could Not Deserialize Experiments!");
        }

        /* Initialize list View */
        ListView experimentListView = findViewById(R.id.experimentListView);
        ExperimentArrayAdapter experimentArrayAdapter = new ExperimentArrayAdapter(this, experiments);
        experimentListView.setAdapter(experimentArrayAdapter);

        /* Upon Clicking On An Experiment, Open The Experiment Overview Activity */
        experimentListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Experiment experiment = this.experiments.get(position);
            startExperimentOverviewActivity(experiment);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    /* Grab All Trials Associated With The Given Experiment */
    private void startExperimentOverviewActivity(Experiment experiment) {
        /* Query The Database For All Trials Related To this.experiment */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trials")
                .document(experiment.getTitle())
                .get()

                /* Query Completion Callback */
                .addOnSuccessListener(documentSnapshot -> {
                    ExperimentStatistics statistics;
                    if (documentSnapshot.exists()) {
                        statistics = documentSnapshot.toObject(ExperimentStatistics.class);
                    } else {
                        statistics = new ExperimentStatistics(experiment.getTitle(), experiment.getType());
                    }
                    Intent intent = new Intent(getApplicationContext(), ExperimentOverviewActivity.class);
                    intent.putExtra("experiment", experiment);
                    intent.putExtra("trials", statistics);
                    startActivity(intent);
                })

                /* Handle The Case Where The Query Was Unsuccessful */
                .addOnFailureListener(e -> Log.e("Error", "Error Fetching Trials From Database!"));
    }
}