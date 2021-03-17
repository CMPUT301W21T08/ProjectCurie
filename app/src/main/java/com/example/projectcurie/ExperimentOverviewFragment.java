package com.example.projectcurie;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class implements the experiment overview fragment for the overview tab of the Experiment
 * Overview activity.
 * @author Joshua Billson
 */
public class ExperimentOverviewFragment extends Fragment {

    private Experiment experiment;
    private ExperimentStatistics statistics;
    private Button subscribeButton;

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

        /* Setup On Click Listener For Subscribing To Experiment */
        subscribeButton = view.findViewById(R.id.experimentSubscriptionButton);
        subscribeButton.setText(experiment.isSubscribed(App.getUser().getUsername()) ? "Unsubscribe" : "Subscribe");
        subscribeButton.setOnClickListener(v -> {
            toggleSubscription();
            subscribeButton.setText(experiment.isSubscribed(App.getUser().getUsername()) ? "Unsubscribe" : "Subscribe");
        });

        /* Setup On Click Listener For Submitting Trials */
        Button submitButton = view.findViewById(R.id.submitTrialButton);
        submitButton.setOnClickListener(v -> submitTrial());

        return view;
    }

    private void toggleSubscription() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("experiments").document(experiment.getTitle());

        /* Run Database Transaction To Toggle Subscription */
        db.runTransaction(transaction -> {
            Experiment experiment = transaction.get(ref).toObject(Experiment.class);
            if (experiment.isSubscribed(App.getUser().getUsername())) {
                experiment.unsubscribe(App.getUser().getUsername());
            } else {
                experiment.subscribe(App.getUser().getUsername());
            }
            transaction.set(ref, experiment);
            return experiment;

        /* On Transaction Success, Update Local Copy Of Experiment & Post Results To UI */
        }).addOnSuccessListener(experiment -> {
            this.experiment = experiment;
            subscribeButton.setText(experiment.isSubscribed(App.getUser().getUsername()) ? "Unsubscribe" : "Subscribe");
            String message = experiment.isSubscribed(App.getUser().getUsername()) ? "You Have Subscribed To This Experiment!" : "You Have Unsubscribed From This Experiment!";
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private void submitTrial() {
        /* If We Are Not Subscribed, Inform User To Subscribe Before Submitting A Trial */
        if (! experiment.isSubscribed(App.getUser().getUsername())) {
            Toast.makeText(getActivity().getApplicationContext(), "Must Subscribe Before Submitting A Trial!", Toast.LENGTH_SHORT).show();
        } else {
            /* If geolocation is required, we warn the user*/
            if (this.experiment.isGeolocationRequired()) {
                notifyGeolocationRequired();
            } else {
                goToSubmitTrialActivity();
            }
        }
    }

    private void goToSubmitTrialActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), SubmitTrialActivity.class);
        intent.putExtra("experiment", this.experiment);
        intent.putExtra("trials", this.statistics);
        startActivity(intent);
    }

    private void notifyGeolocationRequired() {
        new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("This Experiment Requires Geolocation!")
                .setPositiveButton("Ok", (dialog, which) -> {
                    goToSubmitTrialActivity();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(getContext(), "Nothing Happened", Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }
}