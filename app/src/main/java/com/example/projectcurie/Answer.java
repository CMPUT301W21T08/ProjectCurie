package com.example.projectcurie;
/**
 * Answers are represented by this class and inherits attributes from Comment.
 * @author Bo Cen
 */
public class Answer extends Comment{

    /* Empty Constructor For FireStore */
    public Answer() { }

    public Answer(String body, String poster) {
        super(body, poster);
    }
}
