package com.example.projectcurie;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;

/**
 * @author Bo Cen
 * This class represents the comments associated with an experiment in a tab.
 * This class is a ListView which has a list of questions associated with a class. It bundles information
 * regarding the experiment name and experiment ID to be passed onto the answers ListView. First onclick
 * listener opens listview item selected, second onclick listen opens fragment to add new question.
 */
public class ExperimentCommentsFragment extends Fragment {

    private Button new_comment;
    private ArrayList<Comment> questions;
    private ArrayAdapter<Comment> arrayAdapter;
    private CommentController commentController;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.experiment = getArguments().getString("experiment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment_comments, container, false);

        /* Initialize List View */
        listView = view.findViewById(R.id.experimentQuestionsListView);
        questions = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, questions);
        listView.setAdapter(arrayAdapter);

        commentController = new CommentController(questions, arrayAdapter);
        commentController.fetchAndNotifyQuestions(experiment);

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
        new_comment = view.findViewById(R.id.add_comment_btn);
        new_comment.setOnClickListener(v -> {
            AddCommentFragment fragment = new AddCommentFragment();
            fragment.show(getActivity().getSupportFragmentManager(), "ADD COMMENT FRAGMENT");
        });
        return view;
    }
}