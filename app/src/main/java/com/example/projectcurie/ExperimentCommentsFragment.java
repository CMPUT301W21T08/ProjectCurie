package com.example.projectcurie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExperimentCommentsFragment extends Fragment {
    Button new_comment;
    String experimentTitle;

    public ExperimentCommentsFragment() {
    }

    static ExperimentCommentsFragment newInstance(Experiment experiment){
        Bundle args = new Bundle();
        args.putSerializable("experiment", experiment);

        ExperimentCommentsFragment fragment = new ExperimentCommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_experiment_comments, container, false);
        if (getArguments() != null) {
            Experiment args = (Experiment) getArguments().getSerializable("experiment");
            experimentTitle = args.getTitle();
        }
        new_comment = view.findViewById(R.id.add_comment_btn);
        new_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("experimentTitle", experimentTitle);
                AddCommentFragment newComment = new AddCommentFragment();
                newComment.setArguments(bundle);
                newComment.show(getFragmentManager(), "Add new comment");

            }
        });
        return view;
    }


}