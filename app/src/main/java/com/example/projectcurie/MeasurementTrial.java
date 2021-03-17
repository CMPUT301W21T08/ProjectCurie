package com.example.projectcurie;

import com.google.type.LatLng;

/**
 * This class represents the results of a single measurement trial.
 * @author Joshua Billson
 */
public class MeasurementTrial extends Trial {
    private double measurement;

    /** Empty Constructor For Deserializing From FireStore */
    public MeasurementTrial() { }

    /**
     * Constructor for trials which do not require geolocation.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
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
     */
    public MeasurementTrial(String experiment, String author, LatLng location, double measurement) {
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
}
