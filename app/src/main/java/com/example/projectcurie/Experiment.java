package com.example.projectcurie;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
public class Experiment implements Serializable {
    private String title;
    private String description;
    private String region;
    private int minTrialNumber;
    private boolean geolocationRequired;
    private String owner;
    private ExperimentType type;
    private ArrayList<String> tokens;
    private ArrayList<String> subscriptions;
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

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void subscribe(String username) {
        if (! this.subscriptions.contains(username)) {
            subscriptions.add(username);
        }
    }

    public void unsubscribe(String username) {
        if (this.subscriptions.contains(username)) {
            subscriptions.remove(username);
        }
    }

    /* Create An Array Of All Tokens (Words) In The Searchable Fields Of The Experiment */
    private void tokenize() {
        ArrayList<String> searchableFields = new ArrayList<>(Arrays.asList(title, description, region, owner));

        /*
         * This List Of Stop Words Were Created By Sean Bleier (https://gist.github.com/sebleier).
         * Source: GitHub https://gist.github.com/sebleier/554280
         */
        ArrayList<String> stopWords = new ArrayList<>(Arrays.asList("i", "me", "my", "myself", "we",
                "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he",
                "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they",
                "them", "their", "theirs", "what", "which", "who", "whom", "this", "that", "these",
                "those", "am", "is", "are", "was", "were", "be", "been", "being", "do", "does",
                "did", "doing", "a", "an", "the", "and", "but", "if", "or", "as", "of", "at", "by",
                "for", "with", "about", "into", "through", "to", "from", "up", "down", "in", "out",
                "on", "off", "over", "under", "again", "then", "here", "there", "when", "where",
                "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some",
                "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s",
                "t", "can", "will", "just", "don", "should", "now"));

        tokens = new ArrayList<String>();
        for (String field : searchableFields) {
            if (field != null) {
                for (String token : field.split("\\W+")) {
                    if ((! tokens.contains(token.toLowerCase())) && (! stopWords.contains(token.toLowerCase()))) {
                        tokens.add(token.toLowerCase());
                    }
                }
            }
        }

    }
}
