package com.example.projectcurie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores all trials associated with a given experiment and provides methods for
 * extracting common statistics from those trials.
 * @author Joshua Billson
 */
public class ExperimentStatistics implements Serializable {

    private String experiment;
    private ExperimentType experimentType;
    private ArrayList<Trial> trials;

    /** Empty constructor for deserializing from FireStore. */
    public ExperimentStatistics() {
    }

    /**
     * Constructor for initializing a new ExperimentStatics object when creating a new Experiment.
     * @param experiment
     *     The name of the experiment to which this object refers.
     * @param experimentType
     *     The type of experiment to which this experiment refers (binomial, measurement, etc).
     */
    public ExperimentStatistics(String experiment, ExperimentType experimentType) {
        this.experiment = experiment;
        this.experimentType = experimentType;
        trials = new ArrayList<>();
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

    /**
     * Add a new trial to this experiment. Can be any of type BinomialTrial, CountTrial,
     * IntegerCountTrial, or MeasurementTrial.
     * @param trial
     *     The trial we want to submit.
     */
    public void addTrial(Trial trial) {
        this.trials.add(trial);
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    public ExperimentType getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(ExperimentType experimentType) {
        this.experimentType = experimentType;
    }

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    /* Helper method for extracting a value from different types of trial. */
    private double getValue(Trial trial) {
        switch (this.experimentType.ordinal()) {
            case 0:  // Count
                CountTrial countTrial = (CountTrial) trial;
                return countTrial.getCount();
            case 1:  // Integer Count
                IntegerCountTrial integerCountTrial = (IntegerCountTrial) trial;
                return integerCountTrial.getCount();
            case 2:  // Measurement
                MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                return measurementTrial.getMeasurement();
            default:  // Binomial
                BinomialTrial binomialTrial = (BinomialTrial) trial;
                return (binomialTrial.isSuccess()) ? 1.0 : 0.0;
        }
    }
}
