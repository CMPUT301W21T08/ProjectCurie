package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Locale;

/**
 * This class implements the activity for creating a new experiment. If successful, the experiment
 * is uploaded to the database.
 *
 * @author Joshua Billson
 */
public class NewExperimentActivity extends AppCompatActivity {
    Button submitButton;
    Spinner typeSpinner;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText regionEditText;
    EditText minTrialEditText;
    SwitchCompat geolocationSwitch;
    TextView geolocationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        /* Grab Screen Widgets */
        submitButton = findViewById(R.id.createExperimentButton);
        typeSpinner = findViewById(R.id.experimentTypeSpinner);
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        regionEditText = findViewById(R.id.regionEditText);
        minTrialEditText = findViewById(R.id.minTrialsEditText);
        geolocationSwitch = findViewById(R.id.geolocationSwitch);
        geolocationTextView = findViewById(R.id.geolocationTextView);

        /* Setup Experiment Type Spinner */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.experiment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        /* On Change Listener For Geolocation Switch */
        geolocationSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            geolocationTextView.setText(isChecked ? "Geolocation: ON " : "Geolocation: OFF");
        });

        /* On Click Listener For Creating An Experiment */
        submitButton.setOnClickListener((View v) -> {

            /* Grab Experiment Values From User Input */
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String region = regionEditText.getText().toString();
            String experimentType = typeSpinner.getSelectedItem().toString();
            String minTrialsStr = minTrialEditText.getText().toString();
            boolean geolocationRequired = geolocationSwitch.isChecked();


            /* Try To Construct Experiment From The Given Fields; Throw Error If A Field Is Empty */
            try {

                /* Construct Experiment Of The Correct Type */
                Experiment experiment;
                int minTrials = Integer.parseInt(minTrialsStr);
                switch (experimentType) {
                    case "Integer Count":
                        experiment = new Experiment(title, description, region, minTrials, geolocationRequired, App.getUser().getUsername(), ExperimentType.INTEGER_COUNT);
                            break;
                        case "Measurement":
                            experiment = new Experiment(title, description, region, minTrials, geolocationRequired, App.getUser().getUsername(), ExperimentType.MEASUREMENT);
                            break;
                        case "Count":
                            experiment = new Experiment(title, description, region, minTrials, geolocationRequired, App.getUser().getUsername(), ExperimentType.COUNT);
                            break;
                        default:
                            experiment = new Experiment(title, description, region, minTrials, geolocationRequired, App.getUser().getUsername(), ExperimentType.BINOMIAL);
                            break;
                }

                /* Save Experiment To Database */
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference reference = db.collection("experiments").document(title);
                db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentSnapshot experimentSnapshot = transaction.get(reference);
                    if (experimentSnapshot.exists()) {
                        throw new FirebaseFirestoreException(String.format(Locale.CANADA, "Experiment With Title \"%s\" Already Exists!", title), FirebaseFirestoreException.Code.ALREADY_EXISTS);
                    } else {
                        transaction.set(reference, experiment);
                    }
                    return null;
                })
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Experiment Created!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                /* Print Error Message If Any Fields Are Left Empty */
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Error: Must Fill In All Fields!", Toast.LENGTH_SHORT).show();
            }

        });
    }

}