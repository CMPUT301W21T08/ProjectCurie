package com.example.projectcurie;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DatabaseCommand {
    private Runnable callback;

    public abstract void execute(FirebaseFirestore db);

    public DatabaseCommand addCallback(Runnable callback) {
        this.callback = callback;
        return this;
    }

    public void run() {
        DatabaseController.getInstance().executeCommand(this);
    }

    public Runnable getCallback() {
        return callback;
    }
}
