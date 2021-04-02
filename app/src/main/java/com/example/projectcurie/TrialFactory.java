package com.example.projectcurie;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * This class is used to construct a trial of the correct type from a FireStore DocumentSnapshot object.
 * @author Joshua Billson
 */
public class TrialFactory {
    private final Experiment experiment;

    /**
     * Create a new TrialFactory instance to construct trials associated with a given experiment.
     * @param experiment
     *     The experiment to which the trials we want to construct belong.
     */
    public TrialFactory(Experiment experiment) {
        this.experiment = experiment;
    }

    /**
     * Takes a FireStore DocumentSnapshot and constructs a trial of the correct type.
     * @param document
     *     The DocumentSnapshot we want to turn into a Trial.
     * @return
     *     The constructed trial.
     */
    public Trial getTrial(DocumentSnapshot document) {
        ExperimentType type = experiment.getType();
        switch (experiment.getType().ordinal()) {
            case (0):
                return document.toObject(CountTrial.class);
            case (1):
                return document.toObject(IntegerCountTrial.class);
            case (2):
                return document.toObject(MeasurementTrial.class);
            default:
                return document.toObject(BinomialTrial.class);
        }
    }
}
