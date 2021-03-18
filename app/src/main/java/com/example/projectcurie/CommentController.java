package com.example.projectcurie;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CommentController {
    private CommentFetcher fetcher;
    private ArrayAdapter<Comment> adapter;

    public CommentController(ArrayList<Comment> comments, ArrayAdapter<Comment> adapter) {
        this.adapter = adapter;
        this.fetcher = new CommentFetcher(comments);
    }

    public void fetchAndNotifyQuestions(String experiment) {
        fetcher.fetchQuestions(experiment, () -> this.adapter.notifyDataSetChanged());
    }

    public void fetchAndNotifyAnswers(Comment question) {
        fetcher.fetchAnswers(question, () -> this.adapter.notifyDataSetChanged());
    }
}
