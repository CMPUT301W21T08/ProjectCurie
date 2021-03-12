package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_exp_btn = findViewById(R.id.search_exp_btn);
        Button search_person_btn = findViewById(R.id.search_person_btn);
        Button new_exp_btn = findViewById(R.id.new_exp_btn);
        Button view_exp_btn = findViewById(R.id.view_exp_btn);
        Button view_map_btn = findViewById(R.id.view_map_btn);
        Button barcode_btn = findViewById(R.id.barcode_btn);
        
    }

    public void viewExperiments() {
        ///setContentView(R.layout...);
    }
    public void viewGeoLocations() {
        ///setContentView(R.layout...);
    }
    public void searchExperiments() {
        ///setContentView(R.layout...);
    }
    public void searchUsers() {
        ///setContentView(R.layout...);
    }
    public void addExperiment() {
        ///setContentView(R.layout...);
    }

    public void scanBarcode() {
        ///setContentView(R.layout...);
    }
    public void openSearchUserFragment() {
        ///setContentView(R.layout...);
    }





}