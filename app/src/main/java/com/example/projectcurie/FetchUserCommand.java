package com.example.projectcurie;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class FetchUserCommand extends DatabaseCommand {
    private final String user;
    private User userObject;

    public FetchUserCommand(String user) {
        this.user = user;
    }

    public void putData(User user) {
        this.userObject = user;
    }

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
