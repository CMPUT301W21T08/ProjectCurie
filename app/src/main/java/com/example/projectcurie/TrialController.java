package com.example.projectcurie;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class TrialController {

    private ExperimentStatistics statistics;
    private ArrayList<Trial> trials;
    private Experiment experiment;
    private View view;

    public TrialController(Experiment experiment, View view) {
        this.trials = new ArrayList<>();
        this.statistics = new ExperimentStatistics(this.trials);
        this.experiment = experiment;
        this.view = view;
    }

    public void postStatistics() {
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
                        TextView trialCountTextView = view.findViewById(R.id.trialCountTextView);
                        TextView trialMeanTextView = view.findViewById(R.id.trialMeanTextView);
                        trialCountTextView.setText(String.format(Locale.CANADA, "Trial Count: %d", statistics.totalCount()));
                        trialMeanTextView.setText(String.format(Locale.CANADA, "Mean: %.2f", statistics.mean()));
                    }
                });

    }
}
