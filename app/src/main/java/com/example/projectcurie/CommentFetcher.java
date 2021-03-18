package com.example.projectcurie;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
