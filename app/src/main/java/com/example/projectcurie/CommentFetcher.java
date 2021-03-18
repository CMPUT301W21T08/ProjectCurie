package com.example.projectcurie;

import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentFetcher implements Serializable {
    ArrayList<Comment> comments;

    public CommentFetcher(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void fetchQuestions(String experiment, Runnable runnable) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("questions").whereEqualTo("experiment", experiment)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    comments.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Comment comment = document.toObject(Comment.class);
                        comment.setId(document.getId());
                        comments.add(comment);
                    }
                    runnable.run();
                });
    }

    public void fetchAnswers(Comment question, Runnable runnable) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("questions").document(question.getId()).collection("answers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    comments.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        comments.add(document.toObject(Comment.class));
                    }
                    runnable.run();
                });
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
