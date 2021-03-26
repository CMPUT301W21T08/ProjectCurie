package com.example.projectcurie;

import android.widget.ArrayAdapter;

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
    private ArrayList<Comment> comments;
    private ArrayAdapter<Comment> adapter;

    /**
     * Associate this controller with an ArrayAdapter and its underlying ArrayList so that the
     * list view can be re-rendered whenever a change in the database is detected.
     * @param adapter
     *     An ArrayAdapter who is responsible for rendering the contents of comments to the List View.
     */
    public CommentViewer(ArrayAdapter<Comment> adapter, ArrayList<Comment> comments) {
        this.adapter = adapter;
        this.comments = comments;
    }

    /**
     * Set this controller to monitor the questions posted to a given experiment. When an update is
     * detected in the underlying database, it will fetch the most recent data and notify its ArrayAdapter
     * of the need to re-render the List View.
     * @param experiment
     *     The title of the experiment whose questions we want to render.
     */
    public void fetchAndNotifyQuestions(String experiment) {
        DatabaseController.getInstance().watchQuestions(experiment, this);
    }

    public void stopWatching() {
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
    public void fetchAndNotifyAnswers(String experiment, String questionID) {
        DatabaseController.getInstance().watchAnswers(experiment, questionID, this);
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
