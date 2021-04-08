package com.example.projectcurie;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Database command for fetching a given user from the FireStore database.
 * @author Joshua Billson
 */
public class FetchUserCommand extends DatabaseCommand {
    private final String user;
    private User userObject;

    /**
     * Construct a new command which fetches a given user from the database.
     * @param user
     *     The username of the user we want to fetch.
     */
    public FetchUserCommand(String user) {
        this.user = user;
    }

    /**
     * Used inside execute() to put the results of the query into this object for future
     * retrieval in the callback.
     * @param user
     *     The user we fetched from the FireStore database.
     */
    public void putData(User user) {
        this.userObject = user;
    }

    /**
     * Retrieves the user inserted by putData(user).
     * @return
     *     The User that this command fetched from the FireStore database.
     */
    public User fetchData() {
        return this.userObject;
    }

    @Override
    public void execute(FirebaseFirestore db) {
        db.collection("users").document(user)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User userObject = documentSnapshot.toObject(User.class);
                    putData(userObject);
                    if (getCallback() != null) {
                        getCallback().run();
                    }
                })
                .addOnFailureListener(e -> Log.e("Error", "Error Fetching User!"));
    }
}
