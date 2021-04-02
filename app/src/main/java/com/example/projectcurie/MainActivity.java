package com.example.projectcurie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
    Button viewBarcodesButton;
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
        viewBarcodesButton = findViewById(R.id.viewBarcodesButton);
        barcode_btn = findViewById(R.id.scanBarcode_btn);
        view_profile_btn = findViewById(R.id.view_profile_btn);
        username = findViewById(R.id.username_textview);

        /* Set Username */
        username.setText(App.getUser().getUsername());

        /* Search Experiments On Click Listener */
        search_exp_btn.setOnClickListener((View v) -> searchExperiments());

        /* View Experiments On Click Listener */
        view_exp_btn.setOnClickListener((View v) -> browseExperiments());

        /* Create New Experiment On Click Listener */
        new_exp_btn.setOnClickListener((View v) -> createExperiment());

        /* View User Profile On Click Listener */
        view_profile_btn.setOnClickListener((View v) -> viewProfile());

        /* Browse Subscriptions */
        view_map_btn.setOnClickListener((View v) -> viewSubscriptions());

        /* View Barcodes on Click Listener */
        viewBarcodesButton.setOnClickListener((View v) -> viewBarcodes());

        barcode_btn.setOnClickListener((View v) -> scanBarcode());
    }

    /**
     * View user profile.
     */
    public void viewProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("user", App.getUser());
        startActivity(intent);
    }

    /**
     * Browse all experiments.
     */
    public void browseExperiments() {
        DatabaseController.getInstance().fetchExperiments(this, 0);
    }

    /**
     * View user subscribed experiments.
     */
    public void viewSubscriptions() {
        DatabaseController.getInstance().getSubscriptions(this, 0);
    }

    /**
     * Open dialog to search experiments by keyword.
     */
    public void searchExperiments() {
        new SearchExperimentFragment().show(getSupportFragmentManager(), "SEARCH EXPERIMENT FRAGMENT");
    }

    /**
     * Open activity to create a new experiment.
     */
    public void createExperiment() {
        Intent intent = new Intent(getApplicationContext(), NewExperimentActivity.class);
        startActivity(intent);
    }

    /**
     * Open activity to view user registered barcodes and QR codes.
     */
    public void viewBarcodes() {
        Intent intent = new Intent(this, ScannableListActivity.class);
        startActivity(intent);
    }

    /**
     * Start activity to scan a barcode or QR code.
     */
    public void scanBarcode() {
        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setBarcodeImageEnabled(true);
        intent.setOrientationLocked(false);
        intent.initiateScan();
    }

    /* Listener Method Attached To DatabaseController */
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

    /* Handle Results Of Scanning Barcode */
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


    /**
     * This Dialog Fragment presents the user with a simple text bar into which they can enter one or
     * more keywords by which to search for matching experiments. Upon submitting a search query, the
     * the user will be shown the results.
     */
    public static class SearchExperimentFragment extends DialogFragment {
        private EditText search_keyword;
        private DatabaseListener listener;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            if (context instanceof DatabaseListener){
                listener = (DatabaseListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement DatabaseListener");
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_experiment, null);
            search_keyword = view.findViewById(R.id.search_keyword_editText);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Search Experiments")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        String keyword = search_keyword.getText().toString().toLowerCase();
                        DatabaseController.getInstance().searchExperiments(keyword, listener, 0);
                    }).create();
        }
    }
}