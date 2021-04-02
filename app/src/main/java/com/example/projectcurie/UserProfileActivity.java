package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.Locale;

/**
 * This class implements an activity for presenting the user profile.
 * The user profile can be edited by clicking the edit profile button.
 * The user's experiment can be locked or deleted by holding on the experiment.
 * @author Klyde Pausang
 */
public class UserProfileActivity extends AppCompatActivity implements DatabaseListener {

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

        /* Fetch User */
        user = (User) getIntent().getSerializableExtra("user");
        showUserInfo();

        /* Setup Back Button */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /* Initialize Experiment List View */
        experiments = new ArrayList<>();
        experimentArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, experiments);
        ListView experimentListView = findViewById(R.id.user_experiment_list);
        experimentListView.setAdapter(experimentArrayAdapter);

        /* View Experiment Overview On Click */
        experimentListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), ExperimentOverviewActivity.class);
            intent.putExtra("experiment", experiments.get(position));
            startActivity(intent);
        });

        /* Setup List Item On Click Listener */
        if (user.getUsername().equals(App.getUser().getUsername())) {
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
                return true;
            });
        }

        /* Toggle Edit-Profile / Blacklist-User Button Functionality */
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        if (user.getUsername().equals(App.getUser().getUsername())) {
            editProfileButton.setOnClickListener(v -> EditProfileDialogFragment.newInstance(user).show(getSupportFragmentManager(), "EDIT PROFILE FRAGMENT"));
        } else {
            User myProfile = App.getUser();
            editProfileButton.setText(myProfile.isBlacklisted(user.getUsername()) ? "Un-Blacklist User" : "Blacklist User");
            editProfileButton.setOnClickListener(v -> {
                new BlacklistUserCommand(this, myProfile, user)
                        .addCallback(() -> editProfileButton.setText(App.getUser().isBlacklisted(user.getUsername()) ? "Un-Blacklist User" : "Blacklist User"))
                        .run();
            });
        }

        /* Showing experiments by user */
        DatabaseController.getInstance().getOwnedExperiment(user.getUsername(), this, 0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void refreshProfile() {
        showUserInfo();
        Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
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
        new LockExperimentCommand(experiments.get(position), this).run();
    }

    @Override
    public void notifyDataChanged(QuerySnapshot data, int returnCode) {
        if (data != null) {
            for (DocumentSnapshot document : data) {
                experiments.add(document.toObject(Experiment.class));
            }
            experimentArrayAdapter.notifyDataSetChanged();
        }
    }

    private void showUserInfo() {
        usernameTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
        informationTextView.setText(user.getAbout());
        Date joinDate = user.getDateJoined();
        userDateJoin.setText(String.format("%02d-%02d-%d", joinDate.getDate(), joinDate.getMonth() + 1, joinDate.getYear() + 1900));
    }


    /* Database command for toggling the locked state of an experiment */
    private static class LockExperimentCommand extends DatabaseCommand {

        private final Experiment experiment;
        private final Context context;

        public LockExperimentCommand(Experiment experiment, Context context) {
            this.experiment = experiment;
            this.context = context;
        }

        @Override
        public void execute(FirebaseFirestore db) {
            DocumentReference experimentRef = db.collection("experiments").document(experiment.getTitle());
            db.runTransaction((Transaction.Function<Void>) transaction -> {
                DocumentSnapshot document = transaction.get(experimentRef);
                Boolean isLocked = document.getBoolean("locked");
                transaction.update(experimentRef, "locked", !isLocked);
                return null;
            }).addOnSuccessListener(e -> {
                experiment.setLocked(!experiment.isLocked());
                Toast.makeText(context, experiment.isLocked() ? "Experiment Is Now Locked!" : "Experiment Is Now Unlocked!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Log.e("Error", "Error: Couldn't Lock The Experiment!"));

        }
    }


    /* Database command for toggling the blacklisted status of a given user */
    private static class BlacklistUserCommand extends DatabaseCommand {
        private final Context context;
        private final User user;
        private final User owner;

        public BlacklistUserCommand(Context context, User owner, User user) {
            this.context = context;
            this.owner = owner;
            this.user = user;
        }

        @Override
        public void execute(FirebaseFirestore db) {
            if (owner.isBlacklisted(user.getUsername())) {

                /* Un-Blacklist User */
                owner.removeBlacklisted(user.getUsername());
                App.setUser(owner);
                db.collection("users").document(owner.getUsername()).set(owner)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, String.format(Locale.CANADA, "Un-Blacklisted %s!", user.getUsername()), Toast.LENGTH_SHORT).show();
                            getCallback().run();
                        });

            } else {

                /* Add User To Blacklisted */
                owner.addBlacklist(user.getUsername());
                App.setUser(owner);
                db.collection("users").document(owner.getUsername()).set(owner)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, String.format(Locale.CANADA, "Blacklisted %s!", user.getUsername()), Toast.LENGTH_SHORT).show();
                            getCallback().run();
                        });

                /* Delete All Trials Submitted By The Blacklisted User On Experiments Owner By This User */
                db.collection("experiments")
                        .whereEqualTo("owner", owner.getUsername())
                        .get()
                        .addOnSuccessListener(experimentQuerySnapshots -> {
                            for (DocumentSnapshot experimentSnapshot : experimentQuerySnapshots) {
                                experimentSnapshot.getReference().collection("trials")
                                        .whereEqualTo("author", user.getUsername())
                                        .get()
                                        .addOnSuccessListener(trialQuerySnapshots -> {
                                            for (DocumentSnapshot trialSnapshot : trialQuerySnapshots) {
                                                trialSnapshot.getReference().delete();
                                            }
                                        });
                            }
                        });
        }
    }}
}