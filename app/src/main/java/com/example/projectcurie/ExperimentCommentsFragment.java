package com.example.projectcurie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This class represents the comments associated with an experiment in a tab.
 * This class is a ListView which has a list of questions associated with a class. It bundles information
 * regarding the experiment name and experiment ID to be passed onto the answers ListView. First onclick
 * listener opens listview item selected, second onclick listen opens fragment to add new question.
 * @author Bo Cen
 */
public class ExperimentCommentsFragment extends Fragment {

    private CommentViewer commentViewer;
    private String experiment;
    private ListView listView;

    public ExperimentCommentsFragment() {
    }

    static ExperimentCommentsFragment newInstance(String experiment){
        ExperimentCommentsFragment fragment = new ExperimentCommentsFragment();
        Bundle args = new Bundle();
        args.putString("experiment", experiment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.invalidateViews();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.experiment = getArguments().getString("experiment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commentViewer.stopWatching();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment_comments, container, false);
        listView = view.findViewById(R.id.experimentQuestionsListView);

        /* Initialize List View */
        ArrayList<Comment> questions = new ArrayList<>();
        CommentList arrayAdapter = new CommentList(getActivity(), questions);
        listView.setAdapter(arrayAdapter);

        /* Fetch And Watch Questions For Changes */
        commentViewer = new CommentViewer(arrayAdapter, questions);
        commentViewer.fetchAndNotifyQuestions(experiment);

        /* Set List Item On Click Listener */
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Comment question = questions.get(position);
            String qid = question.getId();
            String q_expname = question.getExperiment();
            Log.i("Question ID", question.getId());
            Intent intent = new Intent(getActivity(), AnswerListActivity.class);
            intent.putExtra("q_expname", q_expname);
            intent.putExtra("qid", qid);
            startActivity(intent);
        });

        /* Set On Click Listener For Adding A New Question */
        Button new_comment = view.findViewById(R.id.add_comment_btn);
        new_comment.setOnClickListener(v -> {
            new AddCommentDialogFragment().show(getActivity().getSupportFragmentManager(), "ADD COMMENT FRAGMENT");
        });
        return view;
    }



    /**
     * This class implements the Dialog Fragment for adding a new question to the FireStore database.
     *
     * @author Bo Cen
     */
    public static class AddCommentDialogFragment extends DialogFragment {

        private AddCommentDialogFragmentListener listener;

        /** Obligatory Empty Constructor */
        public AddCommentDialogFragment() { }

        public interface AddCommentDialogFragmentListener {
            void addComment(String body);
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            if (context instanceof AddCommentDialogFragmentListener){
                listener = (AddCommentDialogFragmentListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement AddCommentDialogFragmentListener");
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_comment, null);
            EditText comment_body = view.findViewById(R.id.addQuestionEditText);

            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Add Question")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Submit", (dialogInterface, i) -> listener.addComment(comment_body.getText().toString())).create();
        }
    }

}