package com.example.projectcurie;

import android.location.Location;

/**
 * This class stores the results of a single integer count trial.
 * @author Joshua Billson
 */
public class IntegerCountTrial extends Trial {
    private int count;

    /** Empty Constructor For Deserializing From FireStore */
    public IntegerCountTrial() { }

    /**
     * Constructor for trials which do not require geolocation.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     */
    public IntegerCountTrial(String experiment, String author, int count) {
        super(experiment, author);
        this.count = count;
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
    public IntegerCountTrial(String experiment, String author, Location location, int count) {
        super(experiment, author, location);
        this.count = count;
    }

    /** Getter for count */
    public int getCount() {
        return count;
    }

    /** Setter for count */
    public void setCount(int count) {
        this.count = count;
    }
}
