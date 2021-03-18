package com.example.projectcurie;

import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommentFetcher {
    ArrayList<Comment> comments;
    ArrayAdapter<Comment> adapter;

    public CommentFetcher(ArrayList<Comment> comments, ArrayAdapter<Comment> adapter) {
        this.comments = comments;
        this.adapter = adapter;
    }

    public void fetchQuestions(String experiment) {
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
                    adapter.notifyDataSetChanged();
                });
    }

    public void fetchAnswers(Comment question) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("questions").document(question.getId()).collection("answers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    comments.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        comments.add(document.toObject(Comment.class));
                    }
                    adapter.notifyDataSetChanged();
                });

    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayAdapter<Comment> getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter<Comment> adapter) {
        this.adapter = adapter;
    }
}
