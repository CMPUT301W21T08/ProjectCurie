package com.example.projectcurie;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TrialFetcher {
    private ArrayList<Trial> trials;

    public TrialFetcher(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    public void fetchTrials(Experiment experiment, Runnable runnable) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .document(experiment.getTitle())
                .collection("trials")
                .addSnapshotListener((value, error) -> {
                    trials.clear();
                    if (value != null) {
                        for (DocumentSnapshot document : value) {
                            switch (experiment.getType().ordinal()) {
                                case (0):
                                    trials.add(document.toObject(CountTrial.class));
                                    break;
                                case (1):
                                    trials.add(document.toObject(IntegerCountTrial.class));
                                    break;
                                case (2):
                                    trials.add(document.toObject(MeasurementTrial.class));
                                    break;
                                case (3):
                                    trials.add(document.toObject(BinomialTrial.class));
                                    break;
                            }
                        }
                        runnable.run();
                    }
                });
    }
}
