package com.example.projectcurie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * This class implements the experiment overview fragment for the overview tab of the Experiment
 * Overview activity.
 * @author Joshua Billson
 */
public class ExperimentOverviewFragment extends Fragment {

    private Experiment experiment;
    /*TO DO: Change this boolean value to experiment.getSubscriptions().contains(user.getUserName()) */
    private boolean isSubscribed = false;
    public ExperimentOverviewFragmentInteractionListener listener;

    public ExperimentOverviewFragment() {
    }

    /**
     * Use this to pass an experiment to the fragment.
     * @param experiment
     *     The experiment we want the fragment to display.
     * @return
     *     A new fragment.
     */
    public static ExperimentOverviewFragment newInstance(Experiment experiment) {
        ExperimentOverviewFragment fragment = new ExperimentOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("experiment", experiment);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface ExperimentOverviewFragmentInteractionListener {
        void goSubscribeDialog();
        void goUnsubscribeDialog();
        void goSubscribeSuccess();
        void goWarningSubscribe();

        // TO DO: Modify to get status from database
        boolean getSubscriptionStatus();

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
            if (isSubscribed) {
                try {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SubmitTrialActivity.class);
                    intent.putExtra("experiment", ObjectSerializer.serialize(this.experiment));
                    startActivity(intent);
                } catch (IOException e) {
                    Log.e("Error", "Error: Could Not Serialize Experiment!");
                }
            } else if (!isSubscribed) {
                listener.goWarningSubscribe();
            }

        });
        //TO DO: put an if statement between subscribe and unsuubscribe button depending on subscription status
        Button subscribeButton = view.findViewById(R.id.experimentSubscriptionButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goSubscribeDialog();
                isSubscribed = listener.getSubscriptionStatus();
            }
        });

        Button unsubscribeButton = view.findViewById(R.id.experimentUnsubscribeButton);
        unsubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goUnsubscribeDialog();
                isSubscribed = false;
            }
        });


        return view;
    }
}