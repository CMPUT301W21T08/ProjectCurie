package com.example.projectcurie;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteExperimentCommand extends DatabaseCommand {
    final private String experiment;

    public DeleteExperimentCommand(String experiment) {
        this.experiment = experiment;
    }

    @Override
    public void execute(FirebaseFirestore db) {
        /* Delete Questions And Answers */
        DocumentReference experimentRef = db.collection("experiments").document(experiment);
        experimentRef.collection("questions")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot question : queryDocumentSnapshots) {
                        question.getReference().collection("answers")
                                .get()
                                .addOnSuccessListener(snapshots -> {
                                    for (DocumentSnapshot answer : snapshots) {
                                        answer.getReference().delete();
                                    }
                                });
                        question.getReference().delete();
                    }
                });

        /* Delete Experiment Trials */
        experimentRef.collection("trials")
                .get()
                .addOnSuccessListener((queryDocumentSnapshots -> {
                    for (DocumentSnapshot trial : queryDocumentSnapshots) {
                        trial.getReference().delete();
                    }
                }));

        /* Delete Experiment */
        experimentRef.delete();

        /* Run Callback If Provided */
        if (getCallback() != null) {
            getCallback().run();
        }
    }
}
