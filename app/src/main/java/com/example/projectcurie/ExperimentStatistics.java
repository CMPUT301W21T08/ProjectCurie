package com.example.projectcurie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

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
        if (trials.size() == 0) {
            return 0.0;
        } else {
            double total = 0.0;
            for (Trial trial : trials) {
                total += getValue(trial);
            }
            return total / ((double) this.totalCount());
        }
    }

    public double median() {
        trials.sort(null);
        if (trials.size() == 0) {
            return 0.0;
        } else if ((this.trials.size() % 2) == 0) {
            int medianIndex = (trials.size() / 2) - 1;
            return (getValue(trials.get(medianIndex)) + getValue(trials.get(medianIndex + 1))) / 2.0;
        } else {
            int medianIndex = (trials.size() / 2);
            return getValue(trials.get(medianIndex));
        }
    }

    public double lowerQuartile() {
        trials.sort(null);
        if (trials.size() == 0) {
            return 0.0;
        } else if (trials.size() < 3) {
            return median();
        } else if (trials.size() < 5) {
            return getValue(trials.get(0));
        } else if ((this.trials.size() % 2) == 0) {
            int medianIndex = (trials.size() / 2) - 1;
            int firstQuartileIndex = ((medianIndex) / 2) - 1;
            return (getValue(trials.get(firstQuartileIndex)) + getValue(trials.get(firstQuartileIndex + 1))) / 2.0;
        } else {
            int medianIndex = (trials.size() / 2);
            int firstQuartileIndex = ((medianIndex) / 2) - 1;
            return (getValue(trials.get(firstQuartileIndex)) + getValue(trials.get(firstQuartileIndex + 1))) / 2.0;
        }
    }

    public double upperQuartile() {
        trials.sort(null);
        if (trials.size() == 0) {
            return 0.0;
        } else if (trials.size() < 3) {
            return median();
        }  else if (trials.size() < 5) {
            return getValue(trials.get(trials.size() - 1));
        } else if ((this.trials.size() % 2) == 0) {
            int medianIndex = (trials.size() / 2) - 1;
            int upperQuartileIndex = ((medianIndex) / 2) + medianIndex + 1;
            return (getValue(trials.get(upperQuartileIndex)) + getValue(trials.get(upperQuartileIndex + 1))) / 2.0;
        } else {
            int medianIndex = (trials.size() / 2);
            int upperQuartileIndex = ((medianIndex) / 2) + medianIndex;
            return (getValue(trials.get(upperQuartileIndex)) + getValue(trials.get(upperQuartileIndex + 1))) / 2.0;
        }
    }

    public double standardDeviation() {
        if (trials.size() == 0) {
            return 0.0;
        } else {
            double mean = mean();
            double accumulator = 0;
            for (Trial trial : this.trials) {
                accumulator += Math.pow(getValue(trial) - mean, 2);
            }
            return Math.sqrt(accumulator / ((double) totalCount()));
        }
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
