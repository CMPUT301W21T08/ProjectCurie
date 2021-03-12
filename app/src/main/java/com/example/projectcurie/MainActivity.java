package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_exp_btn = findViewById(R.id.searchExperiments_btn);
        Button view_exp_btn = findViewById(R.id.viewExperiments_btn);
        Button new_exp_btn = findViewById(R.id.addExperiment_btn);
        Button view_map_btn = findViewById(R.id.viewGeoLocations_btn);
        Button search_user_btn = findViewById(R.id.searchUsers_btn);
        Button barcode_btn = findViewById(R.id.scanBarcode_btn);
        Button view_profile_btn = findViewById(R.id.view_profile_btn);

//test1
        search_exp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchExperiments();
            }
        });
        view_exp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewExperiments();
            }
        });
        new_exp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExperiment();
            }
        });
        view_map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGeoLocations();
            }
        });
        search_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();
            }
        });
        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });
        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });

    }
    public void searchExperiments() {
        ///setContentView(R.layout...);
    }
    public void viewExperiments() {
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





}