package com.example.projectcurie;
import android.annotation.SuppressLint;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;
/**
 * This class represents an experiment trial and is the base class from which all other
 * trials inherit.
 * @author Joshua Billson
 */
public abstract class Trial implements Serializable, Comparable<Trial> {
    private Date timestamp;
    private String experiment;
    private String author;
    private double latitude;
    private double longitude;

    /** Empty Constructor For Deserializing From FireStore */
    public Trial() { }

    /**
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     * @param location
     *     The geolocation of the trial.
     */
    public Trial(String experiment, String author, com.google.type.LatLng location) {
        this.timestamp = new Date();
        this.experiment = experiment;
        this.author = author;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

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
     * @param location
     *     The location of the user at the time the trial is submitted.
     * @param experiment
     *     The title of the experiment to which this trial belongs.
     * @param author
     *     The username of the user who submits this trial.
     */
    public Trial(String experiment, String author, Location location) {
        this.experiment = experiment;
        this.author = author;
        this.timestamp = new Date();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    /**
     * Return the date the trial was submitted as a formatted string.
     * @return
     *     The submission date of the trial formatted as dd-MM-YY.
     */
    @SuppressLint("DefaultLocale")
    public String formattedDate() {
        return String.format("%02d-%02d-%d", timestamp.getDate(), timestamp.getMonth()+1, timestamp.getYear()+1900);

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() { return latitude;}

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

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

}
