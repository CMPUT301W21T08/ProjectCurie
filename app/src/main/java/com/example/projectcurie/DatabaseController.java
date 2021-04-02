package com.example.projectcurie;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseController {
    private static DatabaseController controller;

    private final FirebaseFirestore db;
    private ListenerRegistration questionWatcher;
    private ListenerRegistration answerWatcher;
    private ListenerRegistration trialWatcher;

    private DatabaseController() {
        db = FirebaseFirestore.getInstance();
    }

    public static DatabaseController getInstance() {
        if (controller == null) {
            controller = new DatabaseController();
        }
        return controller;
    }

    public void watchQuestions(String experiment, DatabaseListener listener, int returnCode) {
        if (questionWatcher != null) { questionWatcher.remove(); }
        questionWatcher =  db.collection("experiments")
                .document(experiment)
                .collection("questions")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, returnCode));
    }

    public void stopWatchingQuestions() {
        questionWatcher.remove();
        questionWatcher = null;
    }

    public void watchAnswers(String experiment, String qid,  DatabaseListener listener, int returnCode) {
        if (answerWatcher != null) { answerWatcher.remove(); }
        answerWatcher =  db.collection("experiments")
                .document(experiment)
                .collection("questions")
                .document(qid)
                .collection("answers")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, returnCode));
    }

    public void stopWatchingAnswers() {
        answerWatcher.remove();
        answerWatcher = null;
    }

    public void watchTrials(Experiment experiment, DatabaseListener listener, int returnCode) {
        if (trialWatcher != null) { trialWatcher.remove(); }
        trialWatcher = db.collection("experiments")
                .document(experiment.getTitle())
                .collection("trials")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, returnCode));
    }

    public void fetchTrials(Experiment experiment, DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .document(experiment.getTitle())
                .collection("trials")
                .get()
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, returnCode));
    }

    public void stopWatchingTrials() {
        trialWatcher.remove();
        trialWatcher = null;
    }

    public void fetchExperiments(DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .get()
                .addOnFailureListener(e -> Log.e("Error", "Could Not Fetch Experiments!"))
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, returnCode));
    }

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

    public void getSubscriptions(DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .whereArrayContains("subscriptions", App.getUser().getUsername())
                .get()
                .addOnSuccessListener((value) -> listener.notifyDataChanged(value, returnCode));
    }

    public void getOwnedExperiment(String owner, DatabaseListener listener, int returnCode) {
        db.collection("experiments")
                .whereEqualTo("owner", owner)
                .get()
                .addOnFailureListener(e -> Log.e("Error", "Could Not Fetch Experiments!"))
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, returnCode));
    }

    public void executeCommand(DatabaseCommand command) {
        command.execute(db);
    }
}
