package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ExperimentListActivity extends AppCompatActivity {

    private ListView experimentListView;
    private ExperimentArrayAdapter experimentArrayAdapter;
    private ArrayList<Experiment> experiments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_list);

        experimentListView = findViewById(R.id.experimentListView);
        experiments = new ArrayList<>();
        experimentArrayAdapter = new ExperimentArrayAdapter(this, experiments);
        experimentListView.setAdapter(experimentArrayAdapter);

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