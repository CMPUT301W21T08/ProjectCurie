package com.example.projectcurie;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
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

    public void watchQuestions(String experiment, DatabaseListener listener) {
        if (questionWatcher != null) { questionWatcher.remove(); }
        questionWatcher =  db.collection("experiments")
                .document(experiment)
                .collection("questions")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, 0));
    }

    public void stopWatchingQuestions() {
        questionWatcher.remove();
        questionWatcher = null;
    }

    public void watchAnswers(String experiment, String qid,  DatabaseListener listener) {
        if (answerWatcher != null) { answerWatcher.remove(); }
        answerWatcher =  db.collection("experiments")
                .document(experiment)
                .collection("questions")
                .document(qid)
                .collection("answers")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, 0));
    }

    public void stopWatchingAnswers() {
        answerWatcher.remove();
        answerWatcher = null;
    }

    public void watchTrials(Experiment experiment, DatabaseListener listener) {
        if (trialWatcher != null) { trialWatcher.remove(); }
        trialWatcher = db.collection("experiments")
                .document(experiment.getTitle())
                .collection("trials")
                .addSnapshotListener((value, error) -> listener.notifyDataChanged(value, 0));
    }

    public void stopWatchingTrials() {
        trialWatcher.remove();
        trialWatcher = null;
    }

    public void fetchExperiments(DatabaseListener listener) {
        db.collection("experiments")
                .get()
                .addOnFailureListener(e -> Log.e("Error", "Could Not Fetch Experiments!"))
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, 0));
    }

    public void searchExperiments(String keywords, DatabaseListener listener) {
        /* Tokenize Keywords */
        ArrayList<String> keywordsArrayList = new ArrayList<>();
        Collections.addAll(keywordsArrayList, keywords.split("\\W+"));

        /* Fetch Matching Experiments From Database */
        db.collection("experiments")
                .whereArrayContainsAny("tokens", keywordsArrayList)
                .get()
                .addOnFailureListener(e -> Log.e("Error", "Could Not Fetch Experiments!"))
                .addOnSuccessListener(value -> listener.notifyDataChanged(value, 0));
    }

    public void getSubscriptions(DatabaseListener listener) {
        db.collection("experiments")
                .whereArrayContains("subscriptions", App.getUser().getUsername())
                .get()
                .addOnSuccessListener((value) -> listener.notifyDataChanged(value, 0));
    }

    public void executeCommand(DatabaseCommand command) {
        command.execute(db);
    }
}
