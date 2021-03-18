package com.example.projectcurie;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a single question or answer posted to an experiment.
 * @author Bo Cen
 */
public class Comment implements Serializable {
    private String poster;
    private String body;
    private String experiment;
    private transient String id;

    /**
     *  Empty Constructor For FireStore
     */
    public Comment() {
    }

    /**
     * Create A new comment associated with a particular experiment.
     * @param body
     *     The body of the comment.
     * @param poster
     *     The username of the comment's poster.
     * @param experiment
     *     The title of the experiment to which this comment is posted.
     */
    public Comment(String body, String poster, String experiment){
        this.body = body;
        this.poster = poster;
        this.experiment = experiment;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String toString() {
        return this.body;
    }
}
