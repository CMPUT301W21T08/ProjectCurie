package com.example.projectcurie;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Monitors a collection of questions/answers stored in the FireStore database for updates in their
 * underlying data. If such a change is detected, fetches the latest data and executes a Runnable
 * upon completion. This class is useful for triggering effects when a change in a collection of
 * comments or questions is detected.
 * @author Joshua Billson
 */
public class CommentFetcher implements Serializable {

    ArrayList<Comment> comments;
    private boolean isSet = false;

    /**
     * Associate this class with an ArrayList whose contents will be initially populated by the
     * underlying data stored in the FireStore database, then asynchronously updated to reflect
     * the most recent data whenever a change is detected.
     * @param comments
     *     The ArrayList we want to populate with questions/answers from the database.
     */
    public CommentFetcher(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Grabs the data from a collection of questions and sets an observer such that any update
     * to the underlying data will result in re-querying the database and trigger a caller
     * defined Runnable to execute at its conclusion.
     * @param experiment
     *     The title of the experiment to which the questions belong.
     * @param runnable
     *     User-defined Runnable that will execute each time a change in the database is detected.
     */
    public void fetchQuestions(String experiment, Runnable runnable) {
        if (isSet) {
            throw new IllegalStateException();
        } else {
            isSet = true;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("experiments")
                    .document(experiment)
                    .collection("questions")
                    .addSnapshotListener((value, error) -> {
                        comments.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Comment comment = document.toObject(Comment.class);
                            comment.setId(document.getId());
                            comments.add(comment);
                        }
                        runnable.run();
                    });

        }
    }

    /**
     * Grabs the data from a collection of answers and sets an observer such that any update
     * to the underlying data will result in re-querying the database and trigger a caller
     * defined Runnable to execute at its conclusion.
     * @param experiment
     *     The title of the experiment to which the answers belong.
     * @param questionId
     *     The ID of the question to which the answers belong.
     * @param runnable
     *     User-defined Runnable that will execute each time a change in the database is detected.
     */
    public void fetchAnswers(String experiment, String questionId, Runnable runnable) {
        if (isSet) {
            throw new IllegalStateException();
        } else {
            isSet = true;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("experiments")
                    .document(experiment)
                    .collection("questions")
                    .document(questionId)
                    .collection("answers")
                    .addSnapshotListener((value, error) -> {
                        comments.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Comment comment = document.toObject(Comment.class);
                            comment.setId(document.getId());
                            comments.add(comment);
                        }
                        runnable.run();
                    });
        }
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
