package com.example.projectcurie;

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

            /* If geolocation is required, we warn the user*/
            if (this.experiment.isGeolocationRequired()) {
                showAlertDialog(submitButton);

            } else {
                try {
                Intent intent = new Intent(getActivity().getApplicationContext(), SubmitTrialActivity.class);
                intent.putExtra("experiment", ObjectSerializer.serialize(this.experiment));
                intent.putExtra("trials", ObjectSerializer.serialize(this.statistics));
                startActivity(intent);
            } catch (IOException e) {
                Log.e("Error", "Error: Could Not Serialize Experiment!");
            }

        });

        return view;
    }

    public void showAlertDialog(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Warning");
        alert.setMessage("This experiment requires geolocation turned on.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getContext(), "GoodJob", Toast.LENGTH_SHORT).show();
                try {

                    Intent intent = new Intent(getActivity().getApplicationContext(), SubmitTrialActivity.class);
                    intent.putExtra("experiment", ObjectSerializer.serialize(experiment));
                    startActivity(intent);
                } catch (IOException e) {
                    Log.e("Error", "Error: Could Not Serialize Experiment!");
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Nothing Happened", Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }
}