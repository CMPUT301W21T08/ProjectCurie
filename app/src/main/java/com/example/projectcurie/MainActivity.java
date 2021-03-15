package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the homescreen where the user can select 7 different buttons, the each one having a different attribute
 * @author Mitch Labrecque
 */
public class MainActivity extends AppCompatActivity implements SearchExperimentFragment.SearchExperimentFragmentInteractionListener {
//.
    TextView username;
    Button search_exp_btn;
    Button view_exp_btn;
    Button new_exp_btn;
    Button view_map_btn;
    Button search_user_btn;
    Button barcode_btn;
    Button view_profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //* Initializing buttons
        search_exp_btn = findViewById(R.id.searchExperiments_btn);
        view_exp_btn = findViewById(R.id.viewExperiments_btn);
        new_exp_btn = findViewById(R.id.addExperiment_btn);
        view_map_btn = findViewById(R.id.viewGeoLocations_btn);
        search_user_btn = findViewById(R.id.searchUsers_btn);
        barcode_btn = findViewById(R.id.scanBarcode_btn);
        view_profile_btn = findViewById(R.id.view_profile_btn);
        username = findViewById(R.id.username_textview);

        /* Set Username */
        username.setText(App.getUser());

        /* Search Experiments On Click Listener */
        search_exp_btn.setOnClickListener((View v) ->{
            new SearchExperimentFragment().show(getSupportFragmentManager(), "SEARCH EXPERIMENT FRAGMENT");
        });

        /* View Experiments On Click Listener */
        view_exp_btn.setOnClickListener((View v) -> {
            Intent intent = new Intent(getApplicationContext(), ExperimentListActivity.class);
            intent.putStringArrayListExtra("keywords", null);
            startActivity(intent);
        });

        /* Create New Experiment On Click Listener */
        new_exp_btn.setOnClickListener((View v) -> {
            Intent intent = new Intent(getApplicationContext(), NewExperimentActivity.class);
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
        Intent intent = new Intent(getApplicationContext(), ExperimentListActivity.class);
        ArrayList<String> keywordsArrayList = new ArrayList<>();
        Collections.addAll(keywordsArrayList, keywords.split("\\W+"));
        intent.putStringArrayListExtra("keywords", keywordsArrayList);
        startActivity(intent);
    }
}