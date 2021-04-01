package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
                return false;
            });
        }

        /* Toggle Edit-Profile / Blacklist-User Button Functionality */
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        if (user.getUsername().equals(App.getUser().getUsername())) {
            editProfileButton.setOnClickListener(v -> EditUserDialogFragment.newInstance(user).show(getSupportFragmentManager(), "EDIT PROFILE FRAGMENT"));
        } else {
            User myProfile = App.getUser();
            editProfileButton.setText(myProfile.isBlacklisted(user.getUsername()) ? "Un-Blacklist User" : "Blacklist User");
            editProfileButton.setOnClickListener(v -> {
                if (myProfile.isBlacklisted(user.getUsername())) {
                    myProfile.removeBlacklisted(user.getUsername());
                } else {
                    myProfile.addBlacklist(user.getUsername());
                }
                FirebaseFirestore.getInstance().collection("users").document(myProfile.getUsername()).set(myProfile)
                        .addOnSuccessListener(aVoid -> {
                            editProfileButton.setText(myProfile.isBlacklisted(user.getUsername()) ? "Un-Blacklist User" : "Blacklist User");
                            Toast.makeText(this, String.format(Locale.CANADA, "%s %s!", myProfile.isBlacklisted(user.getUsername()) ? "Blacklisted" : "Un-Blacklisted", user.getUsername()), Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Log.e("Error", "Couldn't Update User Blacklist Status!"));
            });
        }

        /* Showing experiments by user */
        DatabaseController.getInstance().getOwnedExperiment(user.getUsername(), this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
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

    public void refreshProfile() {
        showUserInfo();
        Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
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
        /* Showing the username */
        usernameTextView.setText(user.getUsername());

        /* Showing the user email */
        emailTextView.setText(user.getEmail());

        /* Showing the Contact information */
        informationTextView.setText(user.getAbout());

        /* Showing the User join date */
        Date joinDate = user.getDateJoined();
        userDateJoin.setText(String.format("%02d-%02d-%d", joinDate.getDate(), joinDate.getMonth() + 1, joinDate.getYear() + 1900));
    }


    /**
     * This DialogFragment allows a user to enter new values for the editable fields of their profile.
     * @author Joshua Billson
     */
    public static class EditUserDialogFragment extends DialogFragment {

        private UserProfileActivity listener;
        private EditText emailEditText;
        private EditText aboutEditText;
        private User user;

        /** Obligatory Empty Constructor */
        public EditUserDialogFragment() {
        }

        /**
         * Use this for making new instances of EditUserDialogFragment. It binds the user whose profile
         * we want to edit to this fragment.
         * @param user
         *     The user whose details we want to edit.
         * @return
         *     A initialized EditUserDialogFragment.
         */
        public static EditUserDialogFragment newInstance(User user) {
            EditUserDialogFragment fragment = new EditUserDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable("user", user);
            fragment.setArguments(args);
            return fragment;
        }

        /** Check that the activity who owns this fragment implements the EditUserCallbackListener interface. */
        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            if (context instanceof UserProfileActivity){
                listener = (UserProfileActivity) context;
            } else {
                throw new RuntimeException("Must Be Attached To The UserProfileActivity Activity!");
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.user = (User) getArguments().getSerializable("user");
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_user_dialog, null);

            /* Grab Widgets */
            emailEditText = view.findViewById(R.id.editTextEmailAddress);
            aboutEditText = view.findViewById(R.id.editTextDescription);

            /* Set UI Fields */
            emailEditText.setText(user.getEmail());
            aboutEditText.setText(user.getAbout());

            /* Create The Alert Dialog & Set On Click Listeners */
            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Edit Profile")
                    .setNegativeButton("BACK", null)
                    .setPositiveButton("SUBMIT", (dialog, which) -> {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        user.setAbout(aboutEditText.getText().toString());
                        user.setEmail(emailEditText.getText().toString());
                        db.collection("users").document(user.getUsername())
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    App.setUser(user);
                                    listener.refreshProfile();
                                });

                    })
                    .create();
        }
    }

    public static class LockExperimentCommand extends DatabaseCommand {

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
}