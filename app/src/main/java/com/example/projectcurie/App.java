package com.example.projectcurie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Provides a global state for the application. State can be accessed from any Activity ot Fragment
 * bu calling its static getter methods.
 * @author Joshua Billson
 */
public class App extends Application {
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     *  Get the current user of the app.
     */
    public static User getUser() {
        return user;
    }

    /**
     * Set the current user of the app. Typically called after login/registration.
     * @param user
     *     The user of the app.
     */
    public static void setUser(User user) {
        App.user = user;
    }

}
