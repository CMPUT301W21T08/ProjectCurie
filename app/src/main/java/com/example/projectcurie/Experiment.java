package com.example.projectcurie;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

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
 * This class represents a database instance of a single experiment. It records the type of experiment,
 * along with any metadata associated with it.
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

    /** Empty Constructor For Deserializing From FireStore */
    public Experiment() { }

    /**
     * Instantiate a new Experiment to be uploaded to the database.
     * @param title
     *     The title of the experiment.
     * @param description
     *     A textual description of the experiment.
     * @param region
     *     A region in which trials are to take place.
     * @param minTrialNumber
     *     The minimum number of trials that must be submitted to this experiment.
     * @param geolocationRequired
     *     Indicates whether trials requires geolocation on behalf of the experimenter.
     * @param owner
     *     The username of the experiment's publisher.
     * @param type
     *     An enumeration that encodes the type of experiment (binomial, count, integer count, measurement).
     * @throws IllegalArgumentException
     *     If any of the required fields are empty, throws an error.
     */
    public Experiment(String title, String description, String region, int minTrialNumber, boolean geolocationRequired, String owner, ExperimentType type) throws IllegalArgumentException {
        /* Disallow Empty Fields For Title, Description, Region, & Owner */
        if ((title.trim().isEmpty()) || (description.trim().isEmpty()) || (region.trim()).isEmpty() || (owner.trim().isEmpty())) {
            throw new IllegalArgumentException();
        } else {
            this.subscriptions = new ArrayList<>();
            this.title = title;
            this.description = description;
            this.region = region;
            this.minTrialNumber = minTrialNumber;
            this.geolocationRequired = geolocationRequired;
            this.owner = owner;
            this.type = type;
            tokenize();
        }
    }

    /**
     * Subscribe a user to this experiment so that they can submit trials.
     * @param username
     *     The username of the user who wishes to subscribe.
     */
    public void subscribe(String username) {
        if (! this.subscriptions.contains(username)) {
            subscriptions.add(username);
        }
    }

    /**
     * Unsubscribe a user to this experiment so that they can no longer submit trials.
     * @param username
     *     The username of the user who wishes to unsubscribe.
     */
    public void unsubscribe(String username) {
        if (this.subscriptions.contains(username)) {
            subscriptions.remove(username);
        }
    }

    /**
     * Determine whether a given user is subscribed to this experiment.
     * @param username
     *     The username of the user whose subscription status we wish to check.
     * @return
     *     The user's subscription status.
     */
    public boolean isSubscribed(String username) {
        return this.subscriptions.contains(username);
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

    @NotNull
    @Override
    public String toString() {
        return this.title;
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

        tokens = new ArrayList<>();
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
