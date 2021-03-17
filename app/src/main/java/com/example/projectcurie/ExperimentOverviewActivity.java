package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class implements a tabbed activity for viewing and commenting on an experiment.
 * @author Joshua Billson
 */
public class ExperimentOverviewActivity extends AppCompatActivity implements AddCommentFragment.AddCommentDialogFragmentListener,
                                                                                ExperimentOverviewFragment.ExperimentOverviewFragmentInteractionListener,
                                                                                SubscribeDialogFragment.SubscribeDialogFragmentInteractionListener,
                                                                                WarningSubscribeFragment.WarningSubscribeFragmentInteractionListener{

    /* Widgets */
    private TabLayout tabs;
    private ViewPager2 viewPager;
    private StateAdapter stateAdapter;

    /* Data */
    private User user = App.getUser();
    private Experiment experiment;
    private ArrayList<Trial> trials;
    private MessageBoard comments;

    /* Fragments */
    private ExperimentOverviewFragment overviewFragment;
    private ExperimentDataFragment dataFragment;
    private ExperimentCommentsFragment commentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_overview);

        /* Grab Data From Intent */
        grabExperiment();
        grabTrials();
        grabComments();

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
    private void grabComments() {
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
        comments.postQuestion(body, this.user.getUsername());
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

    @Override
    public void goSubscribeDialog() {
        new SubscribeDialogFragment().show(getSupportFragmentManager(),"SUBSCRIBE DIALOG");
    }

    // TO DO: Modify to get status from database
    // This function returns true if user is subscribed
    @Override
    public boolean getSubscriptionStatus(){
        return true;
    }

    @Override
    public void goUnsubscribeDialog() {

    }

    @Override
    public void goSubscribeSuccess() {

    }

    @Override
    public void goWarningSubscribe() {
        new WarningSubscribeFragment().show(getSupportFragmentManager(),"SUBSCRIBE WARNING");
    }

    @Override
    public void subscribeToExperiment() {
        // TO DO: Add username to Subscription collection in the database
        new SubscribeSuccessFragment().show(getSupportFragmentManager(),"SUBSCRIBE SUCCESS");
        Button subscribeButton = findViewById(R.id.experimentSubscriptionButton);
        Button unsubscribeButton = findViewById(R.id.experimentUnsubscribeButton);
        subscribeButton.setVisibility(View.INVISIBLE);
        unsubscribeButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void warningSubscribe() {

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