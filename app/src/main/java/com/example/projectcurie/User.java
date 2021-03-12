package com.example.projectcurie;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String username;
    private String email;
    private String about;
    private Date dateJoined;
    private ArrayList<Experiment> ownedExperiments;
    private ArrayList<String> blacklisted;

    public User(){}

    public User(String username) {
        this.username = username;
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

    public ArrayList<Experiment> getOwnedExperiments() {
        return ownedExperiments;
    }

    public ArrayList<String> getBlacklisted(){
        return blacklisted;
    }

    public void removeBlacklisted(String username) {
        this.blacklisted.remove(username);
    }

    public void addBlacklist(String username) {
        this.blacklisted.add(username);
    }
}
