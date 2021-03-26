package com.example.projectcurie;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
/**
 * This class implements the activity to see list of answers attached to a question, and button to add answer to question.
 * User input for answer text fetched from AddAnswerFragment, poster fetched from app, and question Id and experiment name
 * fetched from bundle packaged in ExperimentCommentsFragment. All relevant information taken and sent to Firestore DB.
 *
 * @author Bo Cen
 */
public class AnswerListActivity extends AppCompatActivity implements AddAnswerFragment.AddAnswerDialogFragmentListener {
    String poster;
    String questionID;
    String question_ExperimentName;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseController.getInstance().stopWatchingAnswers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);

        poster = App.getUser().getUsername();
        questionID = getIntent().getStringExtra("qid");
        question_ExperimentName = getIntent().getStringExtra("q_expname");

        ListView listView = findViewById(R.id.answersListView);
        ArrayList<Comment> answers = new ArrayList<>();
        ArrayAdapter<Comment> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, answers);
        listView.setAdapter(arrayAdapter);

        CommentViewer commentViewer = new CommentViewer(arrayAdapter, answers);
        commentViewer.fetchAndNotifyAnswers(question_ExperimentName, questionID);

        Button new_answer = findViewById(R.id.add_answer_btn);
        new_answer.setOnClickListener(v -> {
            new AddAnswerFragment().show(getSupportFragmentManager(), "ADD_ANSWER");
        });
    }

    /**
     * Adds answer body from fragment with poster name and experiment to the Firestore database.
     * @param body
     *     The body of the answer.
     */
    public void addAnswer(String body){
        Log.i("Poster Id", poster);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .document(question_ExperimentName)
                .collection("questions")
                .document(questionID)
                .collection("answers")
                .document()
                .set(new Comment(body, poster, question_ExperimentName));
    }
}

