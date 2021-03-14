package com.example.projectcurie;

import androidx.annotation.NonNull;
import java.util.Date;

/**
 * This class represents an experiment trial and is the base class from which all other
 * trials inherit.
 * @author Joshua Billson
 */
public abstract class Trial {
    private Date timestamp;
    private double latitude;
    private double longitude;
    private String experiment;
    private String author;

    /** Empty Constructor For Deserializing From FireStore */
    public Trial() { }

    /**
     * Constructor for trials which do not require geolocation.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     */
    public Trial(String experiment, String author) {
        this.experiment = experiment;
        this.author = author;
        this.timestamp = new Date();
    }

    /**
     * Constructor for trials which require geolocation.
     * @param latitude
     *     The latitude of the user at the time the trial is submitted.
     * @param longitude
     *     The longitude of the user at the time the trial is submitted.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     */
    public Trial(double latitude, double longitude, String experiment, String author) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.experiment = experiment;
        this.author = author;
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @NonNull
    public abstract String toString();
}