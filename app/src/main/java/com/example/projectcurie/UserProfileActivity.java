package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class UserProfileActivity extends AppCompatActivity {
    TextView usernameTextView;
    TextView emailTextView;
    TextView informationTextView;
    TextView userDateJoin;
    private User user = App.getUser();
    String email = user.getUsername() + "@gmail.com";
    String about = "Tell us something about yourself!";


    ListView experimentListView;
    private ExperimentArrayAdapter experimentArrayAdapter;
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

        /* Initialization of user info */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(user.getUsername());

        /* Updating the email and about of the user in firestore */
        userRef.update("email", email);
        userRef.update("about", about);
        
        user.setEmail(email);
        user.setAbout(about);
        user.setDateJoined(new Date(System.currentTimeMillis())); /* implement in WelcomeActivity when user is new */
        String dateJoined = user.getDateJoined().toString();
        userRef.update("dateJoined", dateJoined);

        /* Showing the username */
        usernameTextView.setText(user.getUsername());

        /* Showing the user email */
        emailTextView.setText(user.getEmail());

        /* Showing the Contact information */
        informationTextView.setText(user.getAbout());

        /* Showing the User join date */
        userDateJoin.setText(dateJoined);

        /* Showing experiments by user */



    }
}