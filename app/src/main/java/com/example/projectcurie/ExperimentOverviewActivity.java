package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class implements a tabbed activity for viewing and commenting on an experiment.
 * @author Joshua Billson
 */
public class ExperimentOverviewActivity extends AppCompatActivity implements AddCommentFragment.AddCommentDialogFragmentListener {

    /* Widgets */
    private TabLayout tabs;
    private ViewPager2 viewPager;
    private StateAdapter stateAdapter;

    /* Data */
    private User user = App.getUser();
    private Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_overview);

        /* Grab Data From Intent */
        Intent intent = getIntent();
        this.experiment = (Experiment) intent.getSerializableExtra("experiment");

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

    /**
     * Add a new question to this experiment and update the database.
     * @param body
     *     The body of the question we want to post.
     */
    @Override
    public void addComment(String body) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("questions")
                .document()
                .set(new Comment(body, user.getUsername(), experiment.getTitle()));
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
                    return ExperimentOverviewFragment.newInstance(experiment);
                case 1:
                    return ExperimentDataFragment.newInstance(experiment);
                default:
                    return ExperimentCommentsFragment.newInstance(experiment.getTitle());
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}