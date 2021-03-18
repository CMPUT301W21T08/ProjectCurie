package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;

public class UserSearchProfileActivity extends AppCompatActivity {

    TextView userEmail;
    TextView username;
    TextView userInformation;
    TextView userDateJoin;
    private ListView userexperimentListView;
    private User user;
    private User searcher = App.getUser();
    private ArrayList<String> blacklisted;

    private ArrayAdapter<Experiment> experimentArrayAdapter;
    private ArrayList<Experiment> experiments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_profile);

        userEmail = findViewById(R.id.user_email1);
        username = findViewById(R.id.username_text1);
        userInformation = findViewById(R.id.information_text1);
        userexperimentListView = findViewById(R.id.user_experiment_list1);
        userDateJoin = findViewById(R.id.user_join_date1);

        /* Initialize Experiment List View */
        experiments = new ArrayList<>();
        experimentArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, experiments);
        userexperimentListView.setAdapter(experimentArrayAdapter);


        user = (User) getIntent().getSerializableExtra("user");

        showUserInfo();


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

        Button blockButton = (Button) findViewById(R.id.blacklistButton);
        if (searcher.getBlacklisted().contains(user.getUsername())) {
            blockButton.setText("Unblock");
        }  else {
            blockButton.setText("Block");
        }

        blockButton.setOnClickListener(v -> {
            if (searcher.getBlacklisted().contains(user.getUsername())) {
                unblockUser(user.getUsername(), searcher.getUsername());
                blockButton.setText("Block");
            } else {
                blockUser(user.getUsername(), searcher.getUsername());
                blockButton.setText("Unblock");
            }
        });

    }

    private void showUserInfo() {

        /* Showing the username */
        username.setText(user.getUsername());

        /* Showing the user email */
        userEmail.setText(user.getEmail());

        /* Showing the Contact information */
        userInformation.setText(user.getAbout());

        /* Showing the User join date */
        Date joinDate = user.getDateJoined();
        userDateJoin.setText(String.format("%02d-%02d-%d", joinDate.getDate(), joinDate.getMonth()+1, joinDate.getYear()+1900));

    }

    private void blockUser(String username, String searchername){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(searchername);
        db.runTransaction(transaction -> {
                DocumentSnapshot snapshot = transaction.get(userRef);
                ArrayList newBlacklisted = (ArrayList) snapshot.get("blacklisted");
                if (! newBlacklisted.contains(username)) {
                    newBlacklisted.add(username);
                }
                transaction.update(userRef, "blacklisted", newBlacklisted);
                return newBlacklisted;
        }).addOnSuccessListener(transaction -> {
            searcher.addBlacklist(user.getUsername());
            Toast.makeText(getApplicationContext(), username +" is now in your blacklist.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(transaction ->{
            Toast.makeText(getApplicationContext(), "Blocking failed", Toast.LENGTH_SHORT).show();
        });

    }

    private void unblockUser(String username, String searchername) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(searchername);
        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(userRef);
            ArrayList newBlacklisted = (ArrayList) snapshot.get("blacklisted");
            if (newBlacklisted.contains(username)) {
                newBlacklisted.remove(username);
            }
            transaction.update(userRef, "blacklisted", newBlacklisted);
            return newBlacklisted;
        }).addOnSuccessListener(transaction -> {
            searcher.removeBlacklisted(user.getUsername());
            Toast.makeText(getApplicationContext(), username +" is removed from your blacklist.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(transaction ->{
            Toast.makeText(getApplicationContext(), "Unblocking failed", Toast.LENGTH_SHORT).show();
        });
    }

}