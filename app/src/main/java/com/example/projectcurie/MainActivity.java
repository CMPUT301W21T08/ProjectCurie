package com.example.projectcurie;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This activity provides the main menu by which the user will interact with the app. From this
 * activity, the user can opt to search or browse experiments, view their own profile, search
 * for other users, submit a new experiment, or scan a barcode to submit an experiment trial.
 * @author Mitchell Labrecque
 */
public class MainActivity extends AppCompatActivity implements SearchExperimentFragment.SearchExperimentFragmentInteractionListener,
                                                                SearchUserFragment.SearchUserFragmentInteractionListener{

    TextView username;
    Button search_exp_btn;
    Button view_exp_btn;
    Button new_exp_btn;
    Button view_map_btn;
    Button search_user_btn;
    Button barcode_btn;
    Button view_profile_btn;
    Button view_loc;
    GeoLocation geo;

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
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
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        });

        /* Search User on Click Listener */
        search_user_btn.setOnClickListener((View v) -> {
            //searchUsers();
            new SearchUserFragment().show(getSupportFragmentManager(), "SEARCH USER FRAGMENT");
        });

        barcode_btn.setOnClickListener((View v) -> {
                geo = new GeoLocation(MainActivity.this);
                if(geo.canGetLocation()){
                    double latitude = geo.getLatitude();
                    username.setText(String.valueOf(latitude));
                }else{
                    geo.showSettingsAlert();
                }

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

                        /* Iterate Over Experiments  */
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
    @Override
    public void goSearchUser(@NotNull String keywords) {
        /* Grab Experiments From Database */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("users").document(keywords);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshotData: " + document.getData());
                        User tempUser = new User(document.getString("username"));
                        tempUser.setAbout(document.getString("about"));
                        tempUser.setEmail(document.getString("email"));
                        tempUser.setDateJoined(document.getDate("dateJoined"));
                        Intent intent = new Intent(getApplicationContext(), UserSearchProfileActivity.class);
                        intent.putExtra("user", tempUser);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
                        Log.d("Info", "No document", task.getException());
                    }
                } else {
                    Log.d("Failed", "Get failed with");
                }
            }
        });

    }
}