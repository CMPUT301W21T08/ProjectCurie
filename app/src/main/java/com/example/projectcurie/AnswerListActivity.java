package com.example.projectcurie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
/**
 * This class implements the activity to see list of answers attached to a question, and button to add answer to question.
 * User input for answer text fetched from AddAnswerFragment, poster fetched from app, and question Id and experiment name
 * fetched from bundle packaged in ExperimentCommentsFragment. All relevant information taken and sent to Firestore DB.
 *
 * @author Bo Cen
 */
public class AnswerListActivity extends AppCompatActivity {
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
        ArrayAdapter<Comment> arrayAdapter = new CommentList(this, answers);
        listView.setAdapter(arrayAdapter);

        CommentViewer commentViewer = new CommentViewer(arrayAdapter, answers);
        commentViewer.fetchAndNotifyAnswers(question_ExperimentName, questionID);

        Button new_answer = findViewById(R.id.add_answer_btn);
        new_answer.setOnClickListener(v -> new AddAnswerDialogFragment().show(getSupportFragmentManager(), "ADD_ANSWER"));
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


    /**
     * This class implements the fragment for inputting a new answer to a question.
     * User input for answer text fetched from this fragment and sent to addAnswer in AnswerListActivity.
     *
     * @author Bo Cen
     */
    public static class AddAnswerDialogFragment extends DialogFragment {

        private AnswerListActivity listener;

        /** Obligatory Empty Constructor */
        public AddAnswerDialogFragment() { }

        @Override
        public void onAttach(@NotNull Context context) {
            super.onAttach(context);
            if (context instanceof AnswerListActivity){
                listener = (AnswerListActivity) context;
            } else {
                throw new RuntimeException(context.toString() + " Must Be Of Type AnswerListActivity!");
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_answer, null);
            EditText answer_body = view.findViewById(R.id.addAnswerEditText);
            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Add Answer")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Submit", (dialogInterface, i) -> listener.addAnswer(answer_body.getText().toString())).create();
        }
    }

}

