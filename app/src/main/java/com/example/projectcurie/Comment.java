package com.example.projectcurie;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Comments are represented by this class and inherited by Question and Answer classes.
 * @author Bo Cen
 */
public class Comment implements Serializable {
    private String poster;
    private String body;

    /**
     *  Empty Constructor For FireStore
     */
    public Comment() {
    }

    public Comment(String body, String poster){
        this.body = body;
        this.poster = poster;
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

    @NonNull
    public String toString() {
        return this.body;
    }
}
