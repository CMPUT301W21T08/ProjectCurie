package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements SearchExperimentFragment.SearchExperimentFragmentInteractionListener {

    TextView username;
    Button search_exp_btn;
    Button view_exp_btn;
    Button new_exp_btn;
    Button view_map_btn;
    Button search_user_btn;
    Button barcode_btn;
    Button view_profile_btn;
    Button view_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_exp_btn = findViewById(R.id.searchExperiments_btn);
        view_exp_btn = findViewById(R.id.viewExperiments_btn);
        new_exp_btn = findViewById(R.id.addExperiment_btn);
        view_map_btn = findViewById(R.id.viewGeoLocations_btn);
        search_user_btn = findViewById(R.id.searchUsers_btn);
        barcode_btn = findViewById(R.id.scanBarcode_btn);
        view_profile_btn = findViewById(R.id.view_profile_btn);
        username = findViewById(R.id.username_textview);


        /* Set Username */
        username.setText(App.getUser().getUsername());

        /* Search Experiments On Click Listener */
        search_exp_btn.setOnClickListener((View v) ->{
            new SearchExperimentFragment().show(getSupportFragmentManager(), "SEARCH EXPERIMENT FRAGMENT");
        });



        /* View Experiments On Click Listener */
        view_exp_btn.setOnClickListener((View v) -> {

            /* Grab Experiment From Database. Open Experiment List View On Callback Return */
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            ArrayList<Experiment> experiments = new ArrayList<>();
            db.collection("experiments")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            /* Iterate Over Experiments */
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                experiments.add(document.toObject(Experiment.class));
                            }

                            /* Start Experiment List Activity */
                            try {
                                Intent intent = new Intent(getApplicationContext(), ExperimentListActivity.class);
                                intent.putExtra("experiments", ObjectSerializer.serialize(experiments));
                                startActivity(intent);
                            } catch (IOException e) {
                                Log.e("Error", "Error Serializing Experiments!");
                            }

                        } else {
                            Log.e("Error", "Error Fetching Experiments!");
                        }
                    });
        });

        /* Create New Experiment On Click Listener */
        new_exp_btn.setOnClickListener((View v) -> {
            Intent intent = new Intent(getApplicationContext(), NewExperimentActivity.class);
            startActivity(intent);
        });

        /* View User Profile On Click Listener */
        view_profile_btn.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("user", App.getUser());
            startActivity(intent);
        });

        view_map_btn.setOnClickListener((View v) -> {

        });

        search_user_btn.setOnClickListener((View v) -> {
            searchUsers();
        });

        barcode_btn.setOnClickListener((View v) -> {
            scanBarcode();
        });
    }

    public void searchExperiments() {
        ///setContentView(R.layout...);
    }

    public void addExperiment() {
        ///setContentView(R.layout...);
    }
    public void viewGeoLocations() {
        ///setContentView(R.layout...);
    }

    public void searchUsers() {
        ///setContentView(R.layout...);
    }

    public void scanBarcode() {
        ///setContentView(R.layout...);
    }

    public void openSearchUserFragment() {
        ///setContentView(R.layout...);
    }


    @Override
    public void goSearchExperiment(String keywords) {
        /* Tokenize Keywords */
        ArrayList<String> keywordsArrayList = new ArrayList<>();
        Collections.addAll(keywordsArrayList, keywords.split("\\W+"));

        /* Grab Experiments From Database */
        ArrayList<Experiment> experiments = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .whereArrayContainsAny("tokens", keywordsArrayList)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        /* Iterate Over Experiments */
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            experiments.add(document.toObject(Experiment.class));
                        }

                        /* Start Experiment List Activity */
                        try {
                            Intent intent = new Intent(getApplicationContext(), ExperimentListActivity.class);
                            intent.putExtra("experiments", ObjectSerializer.serialize(experiments));
                            startActivity(intent);
                        } catch (IOException e) {
                            Log.e("Error", "Error Serializing Experiments!");
                        }

                    } else {
                        Log.i("Info", "Error Fetching Experiments!");
                    }
                });

    }
}