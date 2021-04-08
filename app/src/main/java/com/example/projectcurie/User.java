package com.example.projectcurie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents a user of the app.
 * @author Paul Cleofas & Joshua Billson
 */
public class User implements Serializable {
    private String username;
    private String email;
    private String about;
    private Date dateJoined;
    private ArrayList<String> blacklisted;

    /**
     * Empty constructor needed by FireStore to deserialize object.
     */
    public User(){}

    /**
     * Create a new user.
     * @param username
     *     The username for the new user.
     */
    public User(String username) {
        this.username = username;
        this.email = "";
        this.about = "";
        this.dateJoined = new Date();
        this.blacklisted = new ArrayList<>();
    }

    /**
     * Remove a given username from this user's list of blacklisted users.
     * @param username
     *     The username of the user we want to un-blacklist.
     */
    public void removeBlacklisted(String username) {
        this.blacklisted.remove(username);
    }

    /**
     * Add a given user to this user's list of blacklisted users.
     * @param username
     *     The username of the user we want to blacklist.
     */
    public void addBlacklist(String username) {
        if (! this.blacklisted.contains(username)) {
            this.blacklisted.add(username);
        }
    }

    /**
     * Check whether a given user is blacklisted by this user.
     * @param username
     *     The username of the user we want to check.
     */
    public boolean isBlacklisted(String username) {
        return this.blacklisted.contains(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public void setBlacklisted(ArrayList<String> blacklisted) {
        this.blacklisted = blacklisted;
    }

    public ArrayList<String> getBlacklisted(){
        return blacklisted;
    }

}

