package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    private String username = null;
    Button start_btn;
    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        start_btn = findViewById(R.id.start_button);
        usernameTextView = findViewById(R.id.username);


        /* Wait For Login */
        login();

        start_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    public void login() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);

        /* Grab Username From Shared Preferences */
        username = sharedPreferences.getString("Username", null);

        /* Handle The Case Where User Is Opening App For First Time */
        if (username == null) {
            username = NameGenerator.uniqueName();
            usernameTextView.setText(username);
            App.setUsername(username);
            db.collection("users").document(username).set(new User(username));
            sharedPreferences.edit().putString("Username", username).apply();
        }

        /* Handle The Case Where User Is Already Registered */
        else {
            App.setUsername(username);
            usernameTextView.setText(username);
            db.collection("users").document(username)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (! documentSnapshot.exists()) {
                            sharedPreferences.edit().remove("Username").apply();
                            login();
                        }
                    });
        }

    }

}