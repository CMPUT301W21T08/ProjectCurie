package com.example.projectcurie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class App extends Application {
    private static String username;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static String getUser() {
        return username;
    }

    public static void setUsername(String username) {
        App.username = username;
    }

}
