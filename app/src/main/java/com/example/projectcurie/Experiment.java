package com.example.projectcurie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * This Enumeration Represents The Different Types Of Experiments That A User Can Create.
 * @author Joshua BIllson
 */
enum ExperimentType {
    COUNT,
    INTEGER_COUNT,
    MEASUREMENT,
    BINOMIAL
}

/**
 * Experiments are represented by this class.
 * @author Joshua BIllson
 */
public class Experiment {
    private String title;
    private String description;
    private String region;
    private int minTrialNumber;
    private boolean geolocationRequired;
    private String owner;
    private ExperimentType type;
    private ArrayList<String> tokens;
    private boolean locked = false;

    public Experiment() { }

    public Experiment(String title, String description, String region, int minTrialNumber, boolean geolocationRequired, String owner, ExperimentType type) throws IllegalArgumentException {
        /* Disallow Empty Fields For Title, Description, Region, & Owner */
        if ((title.trim().isEmpty()) || (description.trim().isEmpty()) || (region.trim()).isEmpty() || (owner.trim().isEmpty())) {
            throw new IllegalArgumentException();
        }

        this.title = title;
        this.description = description;
        this.region = region;
        this.minTrialNumber = minTrialNumber;
        this.geolocationRequired = geolocationRequired;
        this.owner = owner;
        this.type = type;
        tokenize();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public int getMinTrialNumber() {
        return minTrialNumber;
    }

    public boolean isGeolocationRequired() {
        return geolocationRequired;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isLocked() {
        return locked;
    }

    public ExperimentType getType() {
        return type;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTitle(String title) {
        this.title = title;
        tokenize();
    }

    public void setDescription(String description) {
        this.description = description;
        tokenize();
    }

    public void setRegion(String region) {
        this.region = region;
        tokenize();
    }

    public void setMinTrialNumber(int minTrialNumber) {
        this.minTrialNumber = minTrialNumber;
    }

    public void setGeolocationRequired(boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        tokenize();
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setType(ExperimentType type) {
        this.type = type;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    /* Create An Array Of All Tokens (Words) In The Searchable Fields Of The Experiment */
    private void tokenize() {
        ArrayList<String> searchableFields = new ArrayList<>(Arrays.asList(title, description, region, owner));

        tokens = new ArrayList<String>();
        for (String field : searchableFields) {
            if (field != null) {
                for (String token : field.split("\\W+")) {
                    if ((! tokens.contains(token.toLowerCase())) && (token.length() > 3)) {
                        tokens.add(token.toLowerCase());
                    }
                }
            }
        }

    }
}
