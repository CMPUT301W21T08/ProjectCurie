package com.example.projectcurie;

import android.content.Context;
import android.widget.ListView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Use this class to dynamically render a Comment List View. Places a SnapshotListener on the
 * questions/answers posted to a given question and re-renders the List View each time a change
 * is registered in the underlying database.
 * @author Joshua Billson
 */
public class CommentViewer implements DatabaseListener {
    private final ArrayList<Comment> comments;
    private final CommentList adapter;
    private boolean watching = false;

    /**
     * Associate this controller with an ArrayAdapter and its underlying ArrayList so that the
     * list view can be re-rendered whenever a change in the database is detected.
     * @param listView
     *     A list view in which the comments are to be displayed.
     * @param context
     *     The context to which the results should be rendered.
     */
    public CommentViewer(ListView listView, Context context) {
        comments = new ArrayList<>();
        adapter = new CommentList(context, comments);
        listView.setAdapter(adapter);
    }

    /**
     * Set this controller to monitor the questions posted to a given experiment. When an update is
     * detected in the underlying database, it will fetch the most recent data and notify its ArrayAdapter
     * of the need to re-render the List View.
     * @param experiment
     *     The title of the experiment whose questions we want to render.
     */
    public void viewQuestions(String experiment) {
        if (watching) {
            throw new IllegalStateException("Error: Attempt To View Questions/Answers Multiple Times!");
        } else {
            watching = true;
            DatabaseController.getInstance().watchQuestions(experiment, this, 0);
        }
    }

    /**
     * Removes the watcher placed on questions.
     */
    public void stopWatchingQuestions() {
        watching = false;
        DatabaseController.getInstance().stopWatchingQuestions();
    }

    /**
     * Set this controller to monitor the answers posted to a given question. When an update is
     * detected in the underlying database, it will fetch the most recent data and notify its ArrayAdapter
     * of the need to re-render the List View.
     * @param experiment
     *     The title of the experiment to which these answers belong.
     * @param questionID
     *     The ID of the question to which these answers belong.
     */
    public void viewAnswers(String experiment, String questionID) {
        if (watching) {
            throw new IllegalStateException("Error: Attempt To View Questions/Answers Multiple Times!");
        } else {
            watching = true;
            DatabaseController.getInstance().watchAnswers(experiment, questionID, this, 0);
        }
    }

    /**
     * Removes the watcher placed on answers.
     */
    public void stopWatchingAnswers() {
        watching = false;
        DatabaseController.getInstance().stopWatchingAnswers();
    }

    /**
     * Return a comment in the ListView at a given index.
     * @param position
     *     The index of the comment we want to retrieve.
     * @return
     *     The comment at ListView[position].
     */
    public Comment getComment(int position) {
        return comments.get(position);
    }

    @Override
    public void notifyDataChanged(QuerySnapshot data, int returnCode) {
        comments.clear();
        for (QueryDocumentSnapshot document : data) {
            Comment comment = document.toObject(Comment.class);
            comment.setId(document.getId());
            comments.add(comment);
        }
        adapter.notifyDataSetChanged();
    }
}
