
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

import java.util.Objects;


/**
 * This class implements the experiment overview tab of the Experiment Overview activity.
 * @author Joshua Billson
 */
public class ExperimentOverviewFragment extends Fragment {

    private Experiment experiment;
    private Button subscribeButton;

    /** Obligatory Empty Constructor */
    public ExperimentOverviewFragment() {
    }

    /**
     * Use this to pass an experiment and its associated trials to the fragment.
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
        experimentOwner.setText(this.experiment.getOwner());

        /* Setup On Click Listener For Subscribing To Experiment */
        subscribeButton = view.findViewById(R.id.experimentSubscriptionButton);
        subscribeButton.setText(experiment.isSubscribed(App.getUser().getUsername()) ? "Unsubscribe" : "Subscribe");
        subscribeButton.setOnClickListener(v -> toggleSubscription());

        /* On Clicking On Experiment Owner, Take User To Their Profile */
        experimentOwner.setOnClickListener(v -> {
            FetchUserCommand fetchUserCommand = new FetchUserCommand(experiment.getOwner());
            fetchUserCommand.addCallback(() -> {
                User user = fetchUserCommand.fetchData();
                if (user != null) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), UserProfileActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "User Profile Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }).run();
        });

        /* Setup On Click Listener For Submitting Trials */
        Button submitButton = view.findViewById(R.id.submitTrialButton);
        submitButton.setOnClickListener(v -> submitTrial());

        return view;
    }

    private void toggleSubscription() {
        SubscribeCommand subscribeCommand = new SubscribeCommand(experiment.getTitle());
        subscribeCommand.addCallback(() -> {
            this.experiment = subscribeCommand.getData();
            subscribeButton.setText(experiment.isSubscribed(App.getUser().getUsername()) ? "Unsubscribe" : "Subscribe");
            String message = experiment.isSubscribed(App.getUser().getUsername()) ? "You Have Subscribed To This Experiment!" : "You Have Unsubscribed From This Experiment!";
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }).run();
    }

    /*
    * Open Submit Trial Activity If The User Is Subscribed, The Experiment is Unlocked, And
    * They Accept To Submit Geolocation If The Experiment Requires It
    */
    private void submitTrial() {
        /* Check That The Experiment Is Unlocked */
        if (! experiment.isLocked()) {

            /* If We Are Not Subscribed, Inform User To Subscribe Before Submitting A Trial */
            if (! experiment.isSubscribed(App.getUser().getUsername())) {
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Must Subscribe Before Submitting A Trial!", Toast.LENGTH_SHORT).show();
            } else {

                /* If geolocation is required, we warn the user*/
                if (this.experiment.isGeolocationRequired()) {
                    notifyGeolocationRequired();
                } else {
                    goToSubmitTrialActivity();
                }
            }
        } else {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Experiment Is Locked!", Toast.LENGTH_SHORT).show();
        }
    }

    /* Navigate To The Submit Trial Activity */
    private void goToSubmitTrialActivity() {
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), SubmitTrialActivity.class);
        intent.putExtra("experiment", this.experiment);
        startActivity(intent);
    }

    /* Display A Warning If Geolocation Is Required To Submit A Trial */
    private void notifyGeolocationRequired() {
        new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("This Experiment Requires Geolocation!")
                .setPositiveButton("Ok", (dialog, which) -> goToSubmitTrialActivity())
                .setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Nothing Happened", Toast.LENGTH_SHORT).show())
                .create()
                .show();
    }

    private static class SubscribeCommand extends DatabaseCommand {
        private final String experiment;
        private Experiment experimentObject;

        public SubscribeCommand(String experiment) {
            this.experiment = experiment;
        }

        private void putData(Experiment experiment) {
            this.experimentObject = experiment;
        }

        public Experiment getData() {
            return this.experimentObject;
        }

        @Override
        public void execute(FirebaseFirestore db) {
            DocumentReference ref = db.collection("experiments").document(experiment);

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

            }).addOnSuccessListener(experiment -> {
                putData(experiment);
                if (getCallback() != null) {
                    getCallback().run();
                }
            });

        }
    }
}