package com.example.projectcurie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores the questions and answers posted to a particular experiment.
 * @author Joshua Billson
 */
public class MessageBoard implements Serializable {
    private String experiment;
    private ArrayList<Question> questions;

    /**
     * Empty constructor for FireStore.
     */
    public MessageBoard() {
    }

    /**
     * Constructor for initialization when creating a new experiment.
     */
    public MessageBoard(String experiment) {
        this.experiment = experiment;
        this.questions = new ArrayList<>();
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    /**
     * Post a new question to the message board.
     * @param body
     *     The body of the question to be posted.
     * @param poster
     *     The username of the poster.
     */
    public void postQuestion(String body, String poster) {
        this.questions.add(new Question(body, poster));

    }
}
