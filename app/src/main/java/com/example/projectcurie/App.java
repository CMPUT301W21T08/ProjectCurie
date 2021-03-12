package com.example.projectcurie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.FirebaseFirestore;

public class App extends Application {
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setUser(User user) {
        App.user = user;
    }
}
