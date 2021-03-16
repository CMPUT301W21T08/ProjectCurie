package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class implements an activity for presenting a list view of experiments. Experiments
 * can be selected to view their details and to submit trials.
 * @author Joshua Billson
 */
public class ExperimentListActivity extends AppCompatActivity {

    private ListView experimentListView;
    private ExperimentArrayAdapter experimentArrayAdapter;
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
        experimentListView = findViewById(R.id.experimentListView);
        experimentArrayAdapter = new ExperimentArrayAdapter(this, experiments);
        experimentListView.setAdapter(experimentArrayAdapter);

        /* Upon Clicking On An Experiment, Open The Experiment Overview Activity */
        experimentListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Experiment experiment = this.experiments.get(position);
            StartActivityOnFinalCallback startActivityOnFinalCallback = new StartActivityOnFinalCallback(experiment, getApplicationContext());
            startActivityOnFinalCallback.goToExperimentOverview();
        });
    }

    /**
     * This class is used for issuing multiple callbacks to the database and waiting to start the
     * next activity until all such callbacks have been handled. This allows us to carry out multiple
     * database queries before starting the Experiment Overview activity.
     * @author Joshua Billson
     */
    private class StartActivityOnFinalCallback implements Serializable {
        private Intent intent;
        private Experiment experiment;
        private ArrayList<Trial> trials = null;
        private ArrayList<Question> questions = null;

        public StartActivityOnFinalCallback(Experiment experiment, Context context) {
            this.experiment = experiment;
            this.intent = new Intent(context, ExperimentOverviewActivity.class);
        }

        public void goToExperimentOverview() {
            grabTrials();
            grabComments();
        }

        /* Grab All Trials Associated With The Given Experiment */
        private void grabTrials() {
            /* Query The Database For All Trials Related To this.experiment */
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("experiments")
                    .document(this.experiment.getTitle())
                    .collection("trials")
                    .get()

                    /* Query Completion Callback */
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            trials = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                trials.add(doc.toObject(Trial.class));
                            }

                            /* In The Event That All Callbacks Have Returned */
                            if (experiment != null && trials != null && questions != null) {
                                startActivityHelper();
                            }

                            /* Handle The Case Where The Query Was Unsuccessful */
                        } else {
                            Log.e("Error", "Error Fetching Trials From Database!");
                        }
                    });
        }

        /* Grab All Comments Associated With A Given Experiment */
        private void grabComments() {
            /* Query The Database For All Trials Related To this.experiment */
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("experiments")
                    .document(this.experiment.getTitle())
                    .collection("questions")
                    .get()

                    /* Query Completion Callback */
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            questions = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                questions.add(doc.toObject(Question.class));
                            }

                            /* In The Event That All Callbacks Have Returned */
                            if (experiment != null && trials != null && questions != null) {
                                startActivityHelper();
                            }

                            /* Handle The Case Where The Query Was Unsuccessful */
                        } else {
                            Log.e("Error", "Error Fetching From Database!");
                        }
                    });
        }

        private void startActivityHelper() {
            try {
                intent.putExtra("experiment", ObjectSerializer.serialize(experiment));
                intent.putExtra("trials", ObjectSerializer.serialize(trials));
                intent.putExtra("questions", ObjectSerializer.serialize(questions));
                startActivity(intent);
            } catch (IOException e) {
                Log.e("Error", "Error Serializing Experiment!");
            }
        }
    }
}