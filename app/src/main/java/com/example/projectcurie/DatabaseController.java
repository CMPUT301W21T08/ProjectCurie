package com.example.projectcurie;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This singleton class provides common functionality for interacting with the FireStore database.
 * @author Joshua Billson
 */
public class DatabaseController {
    private static DatabaseController controller;

    private final FirebaseFirestore db;
    private ListenerRegistration questionWatcher;
    private ListenerRegistration answerWatcher;
    private ListenerRegistration trialWatcher;

    /**
     * Private constructor; initializes the connection to the FireStore database.
     */
    private DatabaseController() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Get access to the DatabaseController singleton instance. If no instance exists, calls
     * the private constructor.
     * @return
     *     The DatabaseController instance.
     */
    public static DatabaseController getInstance() {
        if (controller == null) {
            controller = new DatabaseController();
        }
        return controller;
    }

    /**
     * Fetches the questions associated with a given experiment and notifies the listener if any
     * changes to the underlying data is detected.
     * @param experiment
     *     The experiment whose questions we want to fetch/watch.
     * @param listener
     *     The listener object who should be notified when new data is available.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void watchQuestions(String experiment, DatabaseListener listener, int returnCode) {
        if (questionWatcher != null) { questionWatcher.remove(); }
        questionWatcher =  db.collection("experiments")
                .document(experiment)
                .collection("questions")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Remove watcher on questions.
     */
    public void stopWatchingQuestions() {
        if (questionWatcher != null) {
            questionWatcher.remove();
            questionWatcher = null;
        }
    }

    /**
     * Fetches the answers associated with a given experiment and question. Notifies the listener
     * if any changes to the underlying data is detected.
     * @param experiment
     *     The experiment whose questions we want to fetch/watch.
     * @param qid
     *     The unique id associated with the question whose answers we want to fetch.
     * @param listener
     *     The listener object who should be notified when new data is available.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void watchAnswers(String experiment, String qid,  DatabaseListener listener, int returnCode) {
        if (answerWatcher != null) { answerWatcher.remove(); }
        answerWatcher =  db.collection("experiments")
                .document(experiment)
                .collection("questions")
                .document(qid)
                .collection("answers")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Remove watcher on answers.
     */
    public void stopWatchingAnswers() {
        if (answerWatcher != null) {
            answerWatcher.remove();
            answerWatcher = null;
        }
    }

    /**
     * Fetches the trials associated with a given experiment and notifies the listener if any
     * changes to the underlying data is detected.
     * @param experiment
     *     The experiment whose trials we want to fetch/watch.
     * @param listener
     *     The listener object who should be notified when new data is available.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void watchTrials(Experiment experiment, DatabaseListener listener, int returnCode) {
        if (trialWatcher != null) { trialWatcher.remove(); }
        trialWatcher = db.collection("experiments")
                .document(experiment.getTitle())
                .collection("trials")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Fetches the trials associated with a given experiment once and passes the results to a listener.
     * @param experiment
     *     The experiment whose trials we want to fetch.
     * @param listener
     *     The listener object who should be notified when the query is complete.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void fetchTrials(Experiment experiment, DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .document(experiment.getTitle())
                .collection("trials")
                .get()
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Remove watcher on trials.
     */
    public void stopWatchingTrials() {
        if (trialWatcher != null) {
            trialWatcher.remove();
            trialWatcher = null;
        }
    }

    /**
     * Fetches all experiments in the database once and passes the results to a listener.
     * @param listener
     *     The listener object who should be notified when the query is complete.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void fetchExperiments(DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .get()
                .addOnFailureListener(e -> Log.e("Error", "Could Not Fetch Experiments!"))
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Fetches all experiments in the database matching a given keyword search and passes the
     * results to a listener.
     * @param listener
     *     The listener object who should be notified when the query is complete.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void searchExperiments(String keywords, DatabaseListener listener, int returnCode) {
        /* Tokenize Keywords */
        ArrayList<String> keywordsArrayList = new ArrayList<>();
        Collections.addAll(keywordsArrayList, keywords.split("\\W+"));

        /* Fetch Matching Experiments From Database */
        db.collection("experiments")
                .whereArrayContainsAny("tokens", keywordsArrayList)
                .get()
                .addOnFailureListener(e -> Log.e("Error", "Could Not Fetch Experiments!"))
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Fetches all experiments to which the user is subscribed.
     * @param listener
     *     The listener object who should be notified when the query is complete.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void getSubscriptions(DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .whereArrayContains("subscriptions", App.getUser().getUsername())
                .get()
                .addOnSuccessListener((value) -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Fetches all experiments which are owned by a given user.
     * @param owner
     *     The username of the user whose owned experiments we want to fetch.
     * @param listener
     *     The listener object who should be notified when the query is complete.
     * @param returnCode
     *     A unique return code passed by the listener so that it may distinguish between multiple
     *     observers if necessary.
     */
    public void getOwnedExperiment(String owner, DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .whereEqualTo("owner", owner)
                .get()
                .addOnFailureListener(e -> Log.e("Error", "Could Not Fetch Experiments!"))
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, returnCode));
    }

    /**
     * Executes a DatabaseCommand object.
     * @param command
     *     The command we want to execute.
     */
    public void executeCommand(DatabaseCommand command) {
        command.execute(db);
    }
}
