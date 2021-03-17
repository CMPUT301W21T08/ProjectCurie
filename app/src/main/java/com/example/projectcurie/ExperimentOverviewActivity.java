package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class implements a tabbed activity for viewing and commenting on an experiment.
 * @author Joshua Billson
 */
public class ExperimentOverviewActivity extends AppCompatActivity implements AddCommentFragment.AddCommentDialogFragmentListener {

    private TabLayout tabs;
    private ViewPager2 viewPager;
    private Experiment experiment;
    private ArrayList<Trial> trials;
    private MessageBoard comments;
    private StateAdapter stateAdapter;

    /* Fragments */
    ExperimentOverviewFragment overviewFragment;
    ExperimentDataFragment dataFragment;
    ExperimentCommentsFragment commentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_overview);


        /* Grab Data From Intent */
        grabExperiment();
        grabTrials();
        grabQuestions();

        /* Grab Widgets */
        tabs = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.experimentOverviewViewPager);

        /* Initialize Fragment State Adapter */
        stateAdapter = new StateAdapter(this);
        viewPager.setAdapter(stateAdapter);

        /* Attach Tabs To Fragment State Adapter */
        String[] tabLabels = {"Overview", "Data", "Comments"};
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(tabLabels[position]);
        }).attach();
    }

    /* Deserialize Experiment From Intent */
    private void grabExperiment() {
        String serialString = getIntent().getStringExtra("experiment");
        if (serialString != null) {
            try {
                this.experiment = (Experiment) ObjectSerializer.deserialize(serialString);
            } catch (IOException e) {
                Log.e("Error", "Error Deserializing Experiment!");
            }
        } else {
            Log.i("Info", "Error Deserializing Experiment!");
        }
    }

    /* Deserialize Trials From Intent */
    private void grabTrials() {
        String serialString = getIntent().getStringExtra("trials");
        if (serialString != null) {
            try {
                this.trials = (ArrayList<Trial>) ObjectSerializer.deserialize(serialString);
            } catch (IOException e) {
                Log.e("Error", "Error Deserializing Experiment!");
            }
        } else {
            Log.i("Info", "Error Deserializing Experiment!");
        }
    }

    /* Deserialize Comments From Intent */
    private void grabQuestions() {
        String serialString = getIntent().getStringExtra("comments");
        if (serialString != null) {
            try {
                this.comments = (MessageBoard) ObjectSerializer.deserialize(serialString);
            } catch (IOException e) {
                Log.e("Error", "Error Deserializing Experiment!");
            }
        } else {
            Log.i("Info", "Error Deserializing Experiment!");
        }
    }

    @Override
    public void addComment(String body) {
        comments.postQuestion(body, App.getUser().getUsername());
        commentsFragment.refreshList();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiments")
                .document(experiment.getTitle())
                .collection("comments")
                .document(experiment.getTitle())
                .set(comments)
                .addOnFailureListener(e -> Log.e("Error", "Error: Couldn't Add New Question!"));

        Log.i("Info: The Comment To Be Posted:", body);
    }


    /**
     * This class manages fragments for each tab in the activity. Each tab is associated with a
     * particular fragment whose lifecycle is managed by this class.
     * @author Joshua Billson
     */
    private class StateAdapter extends FragmentStateAdapter {

        public StateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    overviewFragment = ExperimentOverviewFragment.newInstance(experiment);
                    return overviewFragment;
                case 1:
                    dataFragment = new ExperimentDataFragment();
                    return dataFragment;
                default:
                    commentsFragment = ExperimentCommentsFragment.newInstance(comments.getQuestions());
                    return commentsFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}