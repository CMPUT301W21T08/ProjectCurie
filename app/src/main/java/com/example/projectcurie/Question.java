package com.example.projectcurie;

import java.util.ArrayList;

/**
 * Questions are represented by this class and inherits attributes from Comment.
 * @author Bo Cen
 */
public class Question extends Comment{

    private ArrayList<Answer> answers;

    /* Empty Constructor For FireStore */
    public Question() { }

    public Question(String body, String poster) {
        super(body, poster);
        answers = new ArrayList<>();
    }

    public void reply(String body, String poster){
        answers.add(new Answer(body, poster));
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
