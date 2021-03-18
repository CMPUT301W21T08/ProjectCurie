package com.example.projectcurie;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class implements the activity to see list of answers attached to a question, and button to add answer to question.
 * User input for answer text fetched from AddAnswerFragment, poster fetched from app, and question Id and experiment name
 * fetched from bundle packaged in ExperimentCommentsFragment. All relevant information taken and sent to Firestore DB.
 *
 * @author Bo Cen
 */
public class AnswerListActivity extends AppCompatActivity implements AddAnswerFragment.AddAnswerDialogFragmentListener {

    private Button new_answer;
    Comment question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);

        new_answer = findViewById(R.id.add_answer_btn);
        new_answer.setOnClickListener(v -> {
            new AddAnswerFragment().show(getSupportFragmentManager(), "ADD_ANSWER");
        });
    }

    public void addAnswer(String body){
        String poster = App.getUser().getUsername();
        Bundle question = getIntent().getExtras();
        String questionID = question.getString("qid");
        String question_ExperimentName = question.getString("q_expname");
        Log.i("Poster Id", poster);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("questions").document(questionID).collection("answers")
                .add(new Comment(body, poster, question_ExperimentName));
    }
}

