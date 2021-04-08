package com.example.projectcurie;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A generic class which represents a command to be acted upon by the FireStore database. Specific
 * commands must inherit from this base class.
 * @author Joshua Billson
 */
public abstract class DatabaseCommand {
    private Runnable callback;

    /**
     * Execute the command.
     * @param db
     *     And instance of the FireStore database on which we want to execute the command.
     */
    public abstract void execute(FirebaseFirestore db);

    /**
     * Add a callback to be executed upon completion of the command.
     * @param callback
     *     The callback functionality to be carried out upon completion of the command.
     * @return
     *     A reference to this DatabaseCommand instance for chaining this method with run().
     */
    public DatabaseCommand addCallback(Runnable callback) {
        this.callback = callback;
        return this;
    }

    /**
     * Pass the command to the DatabaseController and execute.
     */
    public void run() {
        DatabaseController.getInstance().executeCommand(this);
    }

    public Runnable getCallback() {
        return callback;
    }
}
