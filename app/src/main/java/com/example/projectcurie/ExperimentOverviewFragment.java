package com.example.projectcurie;

import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * This class implements the experiment overview fragment for the overview tab of the Experiment
 * Overview activity.
 * @author Joshua Billson
 */
public class ExperimentOverviewFragment extends Fragment {

    private Experiment experiment;
    /*TO DO: Change this boolean value to experiment.getSubscriptions().contains(user.getUserName()) */
    public ExperimentOverviewFragmentInteractionListener listener;
    private User user = App.getUser();
    private ExperimentStatistics statistics;

    public ExperimentOverviewFragment() {
    }

    /**
     * Use this to pass an experiment to the fragment.
     * @param experiment
     *     The experiment we want the fragment to display.
     * @return
     *     A new fragment.
     */
    public static ExperimentOverviewFragment newInstance(Experiment experiment, ExperimentStatistics statistics) {
        ExperimentOverviewFragment fragment = new ExperimentOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("experiment", experiment);
        bundle.putSerializable("trials", statistics);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface ExperimentOverviewFragmentInteractionListener {
        Experiment goSubscribeDialog();
        Experiment goUnsubscribeDialog();
        void goWarningSubscribe();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExperimentOverviewFragment.ExperimentOverviewFragmentInteractionListener){
            listener = (ExperimentOverviewFragment.ExperimentOverviewFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.experiment = (Experiment) getArguments().getSerializable("experiment");
        this.statistics = (ExperimentStatistics) getArguments().getSerializable("trials");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* Inflate View */
        View view = inflater.inflate(R.layout.fragment_experiment_overview, container, false);

        /* Grab Widgets */
        TextView experimentTitle = view.findViewById(R.id.experimentTitleTextView);
        TextView experimentDescription = view.findViewById(R.id.experimentDescriptionTextView);
        TextView experimentRegion = view.findViewById(R.id.experimentRegionTextView);
        TextView experimentGeolocation = view.findViewById(R.id.experimentGeolocationTextView);
        TextView experimentOwner = view.findViewById(R.id.experimentOwnerTextView);

        /* Display Experiment Details */
        experimentTitle.setText( this.experiment.getTitle());
        experimentDescription.setText(Html.fromHtml("<b>Description: </b><span>" + this.experiment.getDescription() + "</span>"));
        experimentRegion.setText(Html.fromHtml("<b>Region: </b><span>" + experiment.getRegion() + "</span>"));
        experimentGeolocation.setText(Html.fromHtml("<b>Geolocation Required: </b><span>" + (this.experiment.isGeolocationRequired() ? "True" : "False") + "</span>"));
        experimentOwner.setText(Html.fromHtml("<b>Owner: </b><span>" + this.experiment.getOwner() + "</span>"));

        /* Setup On Click Listener For Submitting Trials */
        Button submitButton = view.findViewById(R.id.submitTrialButton);
        submitButton.setOnClickListener(v -> {
            // Only participate in trial if subscribed to the experiment
            if (experiment.isSubscribed(user.getUsername())) {
                try {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SubmitTrialActivity.class);
                    intent.putExtra("experiment", ObjectSerializer.serialize(this.experiment));
                    startActivity(intent);
                } catch (IOException e) {
                    Log.e("Error", "Error: Could Not Serialize Experiment!");
                }
            } else {
                listener.goWarningSubscribe();
            }
            /* If geolocation is required, we warn the user*/
            if (this.experiment.isGeolocationRequired()) {
                showAlertDialog();
            } else {
                Intent intent = new Intent(getActivity().getApplicationContext(), SubmitTrialActivity.class);
                intent.putExtra("experiment", this.experiment);
                intent.putExtra("trials", this.statistics);
                startActivity(intent);
            }

        });
        //TO DO: put an if statement between subscribe and unsuubscribe button depending on subscription status
        Button subscribeButton = view.findViewById(R.id.experimentSubscriptionButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experiment = listener.goSubscribeDialog();
            }
        });

        Button unsubscribeButton = view.findViewById(R.id.experimentUnsubscribeButton);
        unsubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experiment = listener.goUnsubscribeDialog();
            }
        });


        return view;
    }

    public void showAlertDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("This Experiment Requires Geolocation!")
                .setPositiveButton("Ok", (dialog, which) -> {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SubmitTrialActivity.class);
                    intent.putExtra("experiment", this.experiment);
                    intent.putExtra("trials", this.statistics);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(getContext(), "Nothing Happened", Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }
}