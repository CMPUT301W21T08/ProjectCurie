package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
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

        /* Initialize list View */
        experimentListView = findViewById(R.id.experimentListView);
        experiments = new ArrayList<>();
        experimentArrayAdapter = new ExperimentArrayAdapter(this, experiments);
        experimentListView.setAdapter(experimentArrayAdapter);

        /* Upon Clicking On An Experiment, Open The Experiment Overview Activity */
        experimentListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            try {
                Experiment experiment = this.experiments.get(position);
                Intent intent = new Intent(getApplicationContext(), ExperimentOverviewActivity.class);
                intent.putExtra("experiment", ObjectSerializer.serialize(experiment));
                startActivity(intent);
            } catch (IOException e) {
                Log.e("Error", "Error Serializing Experiment!");
            }
        });


        /* Grab Experiments From Database */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> keywords = getIntent().getStringArrayListExtra("keywords");
        if (keywords == null) {
            db.collection("experiments")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            experiments.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                experiments.add(document.toObject(Experiment.class));
                            }
                            experimentArrayAdapter.notifyDataSetChanged();
                        } else {
                            Log.i("Info", "Error Fetching Experiments!");
                        }
                    });
        } else {
            db.collection("experiments")
                    .whereArrayContainsAny("tokens", keywords)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            experiments.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                experiments.add(document.toObject(Experiment.class));
                            }
                            experimentArrayAdapter.notifyDataSetChanged();
                        } else {
                            Log.i("Info", "Error Fetching Experiments!");
                        }
                    });
        }
    }
}