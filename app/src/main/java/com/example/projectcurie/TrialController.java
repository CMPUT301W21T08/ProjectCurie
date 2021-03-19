package com.example.projectcurie;

import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Provides a link between the database and UI. This class is responsible for monitoring a collection
 * of trials submitted to a particular experiment and rendering some statistics about said trials
 * to the screen. Any changes detected in the underlying data will result in re-rendering the UI
 * to display the most up-to-date statistics.
 * @author Joshua Billson
 */
public class TrialController {

    private ExperimentStatistics statistics;
    private ArrayList<Trial> trials;
    private Experiment experiment;
    private View view;

    /**
     * Create a new TrialController which will monitor the database for trials submitted to a
     * given experiment and render the statistics to a given View.
     * @param experiment
     *     The experiment whose trials we are interested in.
     * @param view
     *     The Activity or Fragment to which we want to render the trial statistics.
     */
    public TrialController(Experiment experiment, View view) {
        this.trials = new ArrayList<>();
        this.statistics = new ExperimentStatistics(this.trials);
        this.experiment = experiment;
        this.view = view;
    }

    /**
     * Sets a listener on the trials associated with the experiment that this controller is interested
     * in. After initially acquiring the trials and rendering their statistics to the IU, it will
     * monitor for any changes in the database. If such a change occurs in the relevant collection
     * of trials, it will re-fetch the data and re-render the UI to display the current statistics.
     */
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
