package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;

public class UserProfileActivity extends AppCompatActivity {
    TextView usernameTextView;
    TextView emailTextView;
    TextView informationTextView;
    TextView userDateJoin;
    User user = new User();
    ListView experimentListView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        usernameTextView = findViewById(R.id.username_text);
        emailTextView = findViewById(R.id.user_email);
        informationTextView = findViewById(R.id.information_text);
        experimentListView = findViewById(R.id.user_experiment_list);
        userDateJoin = findViewById(R.id.user_join_date);

        /* Setups */
        user.setEmail(App.getUser()+"@gmail.com");
        user.setAbout("Tell us something about yourself!");





        /* Showing the username */
        usernameTextView.setText(App.getUser());

        /* Showing the user email */
        emailTextView.setText(user.getEmail());

        /* Showing the Contact information */
        informationTextView.setText(user.getAbout());

        /* Showing the User join date */

        /* Showing experiments by user */



    }
}