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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This activity provides the main menu by which the user will interact with the app. From this
 * activity, the user can opt to search or browse experiments, view their own profile, search
 * for other users, submit a new experiment, or scan a barcode to submit an experiment trial.
 * @author Mitchell Labrecque
 */
public class MainActivity extends AppCompatActivity implements DatabaseListener {
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
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        search_exp_btn = findViewById(R.id.searchExperiments_btn);
        view_exp_btn = findViewById(R.id.viewExperiments_btn);
        new_exp_btn = findViewById(R.id.addExperiment_btn);
        view_map_btn = findViewById(R.id.viewSubscriptions_btn);
        search_user_btn = findViewById(R.id.viewBarcodesButton);
        barcode_btn = findViewById(R.id.scanBarcode_btn);
        view_profile_btn = findViewById(R.id.view_profile_btn);
        username = findViewById(R.id.username_textview);

        /* Set Username */
        username.setText(App.getUser().getUsername());

        /* Search Experiments On Click Listener */
        search_exp_btn.setOnClickListener((View v) -> {
            new SearchExperimentFragment().show(getSupportFragmentManager(), "SEARCH EXPERIMENT FRAGMENT");
        });

        /* View Experiments On Click Listener */
        view_exp_btn.setOnClickListener((View v) -> {
            DatabaseController.getInstance().fetchExperiments(this);
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

        /* Browse Subscriptions */
        view_map_btn.setOnClickListener((View v) -> {
            DatabaseController.getInstance().getSubscriptions(this);
        });

        /* Search User on Click Listener */
        search_user_btn.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, ScannableListActivity.class);
            startActivity(intent);
        });

        barcode_btn.setOnClickListener((View v) -> {
            IntentIntegrator intent = new IntentIntegrator(this);
            intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intent.setBarcodeImageEnabled(true);
            intent.setOrientationLocked(false);
            intent.initiateScan();
            Log.i("Info", "Barcode Button Pressed");
        });

    }

    @Override
    public void notifyDataChanged(QuerySnapshot data, int returnCode) {
        if (returnCode == 0) {
            ArrayList<Experiment> experiments = new ArrayList<>();
            for (QueryDocumentSnapshot document : data) {
                experiments.add(document.toObject(Experiment.class));
            }
            Intent intent = new Intent(getApplicationContext(), ExperimentListActivity.class);
            intent.putExtra("experiments", experiments);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Scannable scannable = App.getScannable(result.getContents());
                if (scannable != null) {
                    scannable.submitTrial(this);
                } else {
                    Toast.makeText(this, "Could Not Locate Scannable!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}