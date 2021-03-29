package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeActivity extends AppCompatActivity {
    Button start_btn;
    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        start_btn = findViewById(R.id.start_button);
        usernameTextView = findViewById(R.id.username);
        usernameTextView.setAlpha(0f);
        start_btn.setAlpha(0f);
        login();
    }

    /**
     * Logs a user into the app. If this is the first time they are opening, a unique username will
     * be assigned to them.
     */
    public void login() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);
        LoginCommand command = new LoginCommand(sharedPreferences.getString("Username", null));
        command.addCallback(() -> {
            /* Log User Into The App */
            App.setUser(command.getUser());
            sharedPreferences.edit().putString("Username", command.getUser().getUsername()).apply();

            /* Show Username And Start Button */
            usernameTextView.setText(command.getUser().getUsername());
            start_btn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
            for (View view : new View[] {start_btn, usernameTextView}) {
                view.animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .setListener(null);
            }

        }).run();
    }

    /* Database Command For Logging In Or Registering A User With The Database */
    private static class LoginCommand extends DatabaseCommand {
        String username;
        User user;

        public LoginCommand(String username) {
            this.username = username;
        }

        @Override
        public void execute(FirebaseFirestore db) {
            /* Handle The Case Where User Is Opening The App For The First Time */
            if (username == null) {
                String tempUsername = NameGenerator.uniqueName();

                /* Check That Name Is Unique */
                db.collection("users").document(tempUsername)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                execute(db);
                            } else {
                                username = tempUsername;
                                setUser(new User(username));
                                db.collection("users").document(username).set(this.user);
                                getCallback().run();
                            }
                        });
            }

            /* Handle The Case Where User Is Already Registered */
            else {
                db.collection("users").document(username)
                        .get()
                        .addOnFailureListener(e -> Log.e("Error", "Error: Couldn't Fetch User From Database!"))
                        .addOnSuccessListener(documentSnapshot -> {
                            if (! documentSnapshot.exists()) {
                                username = null;
                                execute(db);
                            } else {
                                setUser(documentSnapshot.toObject(User.class));
                                getCallback().run();
                            }
                        });
            }
        }

        public void setUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return this.user;
        }
    }
}