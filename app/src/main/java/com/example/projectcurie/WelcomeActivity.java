package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

        start_btn.setVisibility(View.INVISIBLE);
        start_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        /* Wait For Login */
        login();
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
            App.setUser(new User(username));
            db.collection("users").document(username).set(App.getUser());
            sharedPreferences.edit().putString("Username", username).apply();
            start_btn.setVisibility(View.VISIBLE);
        }

        /* Handle The Case Where User Is Already Registered */
        else {
            usernameTextView.setText(username);
            db.collection("users").document(username)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (! documentSnapshot.exists()) {
                            sharedPreferences.edit().remove("Username").apply();
                            login();
                        } else {
                            App.setUser(documentSnapshot.toObject(User.class));
                            start_btn.setVisibility(View.VISIBLE);
                        }
                    });
        }

    }

}