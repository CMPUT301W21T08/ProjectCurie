package com.example.projectcurie;

import android.location.Location;

/**
 * This class stores the results of a single measurement trial.
 * @author Joshua Billson
 */
public class MeasurementTrial extends Trial implements Comparable<Trial> {
    private double measurement;

    /** Empty Constructor For Deserializing From FireStore */
    public MeasurementTrial() { }

    /**
     * Constructor for trials which do not require geolocation.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     * @param measurement
     *     The measurement associated with the trial.
     */
    public MeasurementTrial(String experiment, String author, double measurement) {
        super(experiment, author);
        this.measurement = measurement;
    }

    /**
     * Constructor for trials which require geolocation.
     * @param location
     *     The location of the user at the time the trial is submitted.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     * @param measurement
     *     The measurement associated with the trial.
     * @throws IllegalArgumentException
     *     If the location is null, the constructor throws an error.
     */
    public MeasurementTrial(String experiment, String author, Location location, double measurement) throws IllegalArgumentException {
        super(experiment, author, location);
        this.measurement = measurement;
    }

    /** Getter for measurement */
    public double getMeasurement() {
        return measurement;
    }

    /** Setter for measurement */
    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    @Override
    public int compareTo(Trial o) {
        MeasurementTrial other = (MeasurementTrial) o;
        return Double.compare(this.measurement, other.getMeasurement());
    }
}
