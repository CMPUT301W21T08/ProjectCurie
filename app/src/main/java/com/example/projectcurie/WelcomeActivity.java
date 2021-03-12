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
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Button start_btn = findViewById(R.id.start_button);

        start_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        login();
    }

    public void login() {
        TextView usernameTextView = findViewById(R.id.username);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);

        /* Grab Username From Shared Preferences */
        String username = sharedPreferences.getString("Username", null);

        /* Handle The Case Where User Is Opening App For First Time */
        if (username == null) {
            user = new User(NameGenerator.uniqueName());
            usernameTextView.setText(user.getUsername());
            db.collection("users").document(user.getUsername()).set(user);
            sharedPreferences.edit().putString("Username", user.getUsername()).apply();
            App.setUser(user);
        }

        /* Handle The Case Where User Is Already Registered */
        else {
            usernameTextView.setText(username);
            db.collection("users").document(username)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(User.class);
                            App.setUser(user);
                        } else {
                            sharedPreferences.edit().remove("Username").apply();
                            login();
                        }
                    });
        }

    }
}