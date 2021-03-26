package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
/**
 * This class implements an activity for presenting the user profile.
 * The user profile can be edited by clicking the edit profile button.
 * The user's experiment can be locked or deleted by holding on the experiment.
 * @author Klyde Pausang
 */
public class UserProfileActivity extends AppCompatActivity implements EditUserDialogFragment.EditUserCallbackListener, DatabaseListener {

    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView informationTextView;
    private TextView userDateJoin;
    private User user;


    private ArrayAdapter<Experiment> experimentArrayAdapter;
    private ArrayList<Experiment> experiments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        usernameTextView = findViewById(R.id.username_text);
        emailTextView = findViewById(R.id.user_email);
        informationTextView = findViewById(R.id.information_text);
        userDateJoin = findViewById(R.id.user_join_date);

        /* Initialize Experiment List View */
        experiments = new ArrayList<>();
        experimentArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, experiments);
        ListView experimentListView = findViewById(R.id.user_experiment_list);
        experimentListView.setAdapter(experimentArrayAdapter);

        /* Setup List Item On Click Listener */
        experimentListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Experiment experiment = this.experiments.get(position);
            String lockString = experiment.isLocked() ? "Unlock" : "Lock";
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

        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(v -> EditUserDialogFragment.newInstance(user).show(getSupportFragmentManager(), "EDIT PROFILE FRAGMENT"));

        /* Showing experiments by user */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .whereEqualTo("owner", user.getUsername())
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot queryDocumentSnapshot = task.getResult();
                    if (queryDocumentSnapshot != null) {
                        for (DocumentSnapshot document : queryDocumentSnapshot) {
                            experiments.add(document.toObject(Experiment.class));
                        }
                        experimentArrayAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteExperiment(int position) {
        new DeleteExperimentCommand(experiments.get(position).getTitle())
                .addCallback(() -> {
                    Toast.makeText(this.getApplicationContext(), "Deleted Experiment!", Toast.LENGTH_SHORT).show();
                    this.experiments.remove(position);
                    this.experimentArrayAdapter.notifyDataSetChanged();})
                .run();
    }

    private void lockExperiment(int position) {
        Experiment experiment = this.experiments.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference experimentRef = db.collection("experiments").document(experiment.getTitle());
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot document = transaction.get(experimentRef);
            Boolean isLocked = document.getBoolean("locked");
            if (isLocked != null) {
                transaction.update(experimentRef, "locked", !isLocked);
            }
            return null;
        }).addOnSuccessListener(e -> {
            Toast.makeText(getApplicationContext(), experiment.isLocked() ? "Experiment Is Now Unlocked!" : "Experiment Is Now Locked!", Toast.LENGTH_SHORT).show();
            experiment.setLocked(!experiment.isLocked());
        }).addOnFailureListener(e -> Log.e("Error", "Error: Couldn't Lock The Experiment!"));
    }

    @Override
    public void refreshProfile() {
        showUserInfo();
        Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyDataChanged(QuerySnapshot data, int returnCode) {

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

}