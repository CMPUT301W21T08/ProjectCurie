package com.example.projectcurie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class App extends Application {
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }

}
