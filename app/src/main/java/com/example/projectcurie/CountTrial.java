package com.example.projectcurie;

import android.location.Location;

/**
 * This class stores the results of a single count trial.
 * @author Joshua Billson
 */
public class CountTrial extends Trial implements Comparable<Trial> {

    private int count;

    /** Empty Constructor For Deserializing From FireStore */
    public CountTrial() { }

    /**
     * Constructor for trials which do not require geolocation.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     */
    public CountTrial(String experiment, String author) {
        super(experiment, author);
        this.count = 1;
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
    public CountTrial(String experiment, String author, Location location) throws IllegalArgumentException {
        super(experiment, author, location);
        this.count = 1;
    }

    /** Getter for count */
    public int getCount() {
        return count;
    }

    /** Setter for count */
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(Trial o) {
        return 0;
    }
}
