package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
/**
 * This class implements an activity for presenting the user profile.
 * The user profile can be edited by clicking the edit profile button.
 * The user's experiment can be locked or deleted by holding on the experiment.
 * @author Klyde Pausang
 */
public class UserProfileActivity extends AppCompatActivity implements EditUserDialogFragment.EditUserCallbackListener {

    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView informationTextView;
    private TextView userDateJoin;
    private Button editProfileButton;
    private ListView experimentListView;
    private User user;
    private String lockString;


    private ArrayAdapter<Experiment> experimentArrayAdapter;
    private ArrayList<Experiment> experiments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        usernameTextView = findViewById(R.id.username_text);
        emailTextView = findViewById(R.id.user_email);
        informationTextView = findViewById(R.id.information_text);
        experimentListView = findViewById(R.id.user_experiment_list);
        userDateJoin = findViewById(R.id.user_join_date);
        editProfileButton = findViewById(R.id.edit_profile_button);

        /* Initialize Experiment List View */
        experiments = new ArrayList<>();
        experimentArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, experiments);
        experimentListView.setAdapter(experimentArrayAdapter);

        /* Setup List Item On Click Listener */
        experimentListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Experiment experiment = this.experiments.get(position);
            lockString = experiment.isLocked() ? "Unlock" : "Lock";
            new AlertDialog.Builder(this)
                    .setTitle("Lock/Delete Experiment")
                    .setMessage("Do You Want To Lock Or Delete This Experiment?")
                    .setNegativeButton(lockString, (dialog, which) -> lockExperiment(position))
                    .setNeutralButton("Back", null)
                    .setPositiveButton("Delete", (dialog, which) -> deleteExperiment(position))
                    .create()
                    .show();
            return false;
        });

        user = (User) getIntent().getSerializableExtra("user");

        showUserInfo();

        editProfileButton.setOnClickListener(v -> {
            EditUserDialogFragment.newInstance(user).show(getSupportFragmentManager(), "EDIT PROFILE FRAGMENT");
        });

        /* Showing experiments by user */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .whereEqualTo("owner", user.getUsername())
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot document : task.getResult()) {
                        experiments.add(document.toObject(Experiment.class));
                    }
                    experimentArrayAdapter.notifyDataSetChanged();
                });



    }

    private void showUserInfo() {
        /* Showing the username */
        usernameTextView.setText(user.getUsername());

        /* Showing the user email */
        emailTextView.setText(user.getEmail());

        /* Showing the Contact information */
        informationTextView.setText(user.getAbout());

        /* Showing the User join date */
        Date joinDate = user.getDateJoined();
        userDateJoin.setText(String.format("%02d-%02d-%d", joinDate.getDate(), joinDate.getMonth()+1, joinDate.getYear()+1900));
    }

    private void deleteExperiment(int position) {
        Experiment experiment = this.experiments.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference experimentRef = db.collection("experiments").document(experiment.getTitle());
        DocumentReference commentsRef = db.collection("comments").document(experiment.getTitle());
        DocumentReference trialsRef = db.collection("trials").document(experiment.getTitle());
        WriteBatch batch = db.batch();
        batch.delete(experimentRef);
        batch.delete(commentsRef);
        batch.delete(trialsRef);
        batch.commit()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this.getApplicationContext(), "Deleted Experiment!", Toast.LENGTH_SHORT).show();
                    this.experiments.remove(position);
                    this.experimentArrayAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Error", "Error Deleting Experiment!"));
    }

    private void lockExperiment(int position) {
        Experiment experiment = this.experiments.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference experimentRef = db.collection("experiments").document(experiment.getTitle());
        if (! experiment.isLocked()) {
            experimentRef.update("locked", true)
                    .addOnSuccessListener(e -> {
                        Toast.makeText(getApplicationContext(), "Experiment Is Now Locked!", Toast.LENGTH_SHORT).show();
                        experiment.setLocked(true);
                        lockString= "Unlock";
                    })
                    .addOnFailureListener(e -> Log.e("Error", "Error: Couldn't Lock The Experiment!"));
        } else {
            experimentRef.update("locked", false)
                    .addOnSuccessListener(e -> {
                        Toast.makeText(getApplicationContext(), "Experiment Is Now Unlocked!", Toast.LENGTH_SHORT).show();
                        experiment.setLocked(false);
                        lockString= "Lock";
                    })
                    .addOnFailureListener(e -> Log.e("Error", "Error: Couldn't Unlock The Experiment!"));
        }
    }

    @Override
    public void refreshProfile() {
        showUserInfo();
        Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
    }
}