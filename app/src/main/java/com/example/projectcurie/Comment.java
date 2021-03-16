package com.example.projectcurie;
/**
 * Comments are represented by this class and inherited by Question and Answer classes.
 * @author Bo Cen
 */
public class Comment {
    private String poster;
    private String body;

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
}
