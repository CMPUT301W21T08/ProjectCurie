package com.example.projectcurie;

import com.google.type.LatLng;

/**
 * This class represents the results of a single binomial trial.
 * @author Joshua Billson
 */
public class BinomialTrial extends Trial {
    private boolean success;

    /** Empty Constructor For Deserializing From FireStore */
    public BinomialTrial() { }

    /**
     * Constructor for trials which do not require geolocation.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     */
    public BinomialTrial(String experiment, String author, boolean success) {
        super(experiment, author);
        this.success = success;
    }

    /**
     * Constructor for trials which require geolocation.
     * @param location
     *     The location of the user at the time the trial is submitted.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     */
    public BinomialTrial(String experiment, String author, LatLng location, boolean success) {
        super(experiment, author, location);
        this.success = success;
    }

    /** Getter for success */
    public boolean isSuccess() {
        return success;
    }

    /** Setter for success */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
