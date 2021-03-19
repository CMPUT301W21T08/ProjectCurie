package com.example.projectcurie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores all trials associated with a given experiment and provides methods for
 * extracting common statistics from those trials.
 * @author Joshua Billson
 */
public class ExperimentStatistics implements Serializable {

    private ArrayList<Trial> trials;

    /**
     * Constructor for initializing a new ExperimentStatics object when creating a new Experiment.
     * @param trials
     *     An array of trials for which we want to compute some statistics.
     */
    public ExperimentStatistics(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    /**
     * Get the total number of trials submitted to this experiment.
     * @return
     *     The number of trials submitted to this experiment.
     */
    public int totalCount() {
        return this.trials.size();
    }

    /**
     * Get the mean of all trial results for this experiment.
     * @return
     *     The mean value of all trials.
     */
    public double mean() {
        double total = 0.0;
        for (Trial trial : trials) {
            total += getValue(trial);
        }
        return total / ((double) this.totalCount());
    }

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    /* Helper method for extracting a value from different types of trial. */
    private double getValue(Trial trial) {
        if (trial instanceof CountTrial) {
            CountTrial countTrial = (CountTrial) trial;
            return countTrial.getCount();
        } else if (trial instanceof IntegerCountTrial) {
            IntegerCountTrial integerCountTrial = (IntegerCountTrial) trial;
            return integerCountTrial.getCount();
        } else if (trial instanceof MeasurementTrial) {
            MeasurementTrial measurementTrial = (MeasurementTrial) trial;
            return measurementTrial.getMeasurement();
        } else {
            BinomialTrial binomialTrial = (BinomialTrial) trial;
            return (binomialTrial.isSuccess()) ? 1.0 : 0.0;
        }
    }
}
