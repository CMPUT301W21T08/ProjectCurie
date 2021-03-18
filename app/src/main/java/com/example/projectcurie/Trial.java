package com.example.projectcurie;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;
/**
 * This class represents an experiment trial and is the base class from which all other
 * trials inherit.
 * @author Joshua Billson
 */
public class Trial implements Serializable {
    private Date timestamp;
    private String experiment;
    private String author;
    private double latitude;
    private double longitude;
    private LatLng location;
    private LatLng locationLatLng;
    public Trial() { }
    /** Empty Constructor For Deserializing From FireStore
     * @param experiment
     * @param author
     * @param location*/
    public Trial(String experiment, String author, com.google.type.LatLng location) {
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
    public Trial(String experiment, String author,LatLng location) {
        GetGeoLocation geo = new GetGeoLocation();
        this.experiment = experiment;
        this.author = author;
        this.timestamp = new Date();
        this.latitude = geo.getLatitude();
        this.longitude = geo.getLongitude();
        this.location = location;
        this.locationLatLng = getLocation();
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
    public void setLatitude(double latitude) { this.latitude = latitude; }



    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) {
        this.longitude = longitude; }

    public LatLng getLocation() {;
        // Creates a new geolocation object and retrieves latitude/longitude
        //then converts both lat/long into a LatLng Type
        GetGeoLocation geo = new GetGeoLocation();
        Double lat_val = geo.getLatitude();
        Double lng_val = geo.getLongitude();
        LatLng currentLoc = new LatLng(lat_val,lng_val);
        return currentLoc;

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



}
