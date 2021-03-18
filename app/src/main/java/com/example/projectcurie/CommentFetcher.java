package com.example.projectcurie;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentFetcher implements Serializable {
    ArrayList<Comment> comments;
    private boolean isSet = false;

    public CommentFetcher(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void fetchQuestions(String experiment, Runnable runnable) {
        if (isSet) {
            throw new IllegalStateException();
        } else {
            isSet = true;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("questions").whereEqualTo("experiment", experiment)
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

    public void fetchAnswers(String questionId, Runnable runnable) {
        if (isSet) {
            throw new IllegalStateException();
        } else {
            isSet = true;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("questions").document(questionId).collection("answers")
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
