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

import java.io.IOException;

/**
 * This class implements a tabbed activity for viewing and commenting on an experiment.
 * @author Joshua Billson
 */
public class ExperimentOverviewActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager2 viewPager;
    private Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_overview);

        /* Grab Widgets */
        tabs = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.experimentOverviewViewPager);

        /* Initialize Fragment State Adapter */
        StateAdapter stateAdapter = new StateAdapter(this);
        viewPager.setAdapter(stateAdapter);

        /* Attach Tabs To Fragment State Adapter */
        String[] tabLabels = {"Overview", "Data", "Comments"};
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(tabLabels[position]);
        }).attach();

        /* Deserialize Experiment From Intent */
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
                    return new ExperimentDataFragment();
                default:
                    return ExperimentCommentsFragment.newInstance(experiment);
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}