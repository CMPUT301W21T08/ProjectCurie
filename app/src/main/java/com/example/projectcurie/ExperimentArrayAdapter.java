package com.example.projectcurie;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExperimentArrayAdapter extends ArrayAdapter<Experiment> {

    private ArrayList<Experiment> experiments;
    private Context context;

    public ExperimentArrayAdapter(@NonNull Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /* Inflate the view if necessary */
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.experiment_list_item, parent, false);
        }

        Experiment experiment = experiments.get(position);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView statusTextView = view.findViewById(R.id.statusTextView);
        TextView authorTextView = view.findViewById(R.id.authorTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);

        titleTextView.setText(experiment.getTitle());
        statusTextView.setText(Html.fromHtml("<b>Status: </b><span>" + (experiment.isLocked() ? "Locked" : "Open") + "</span>"));
        authorTextView.setText(Html.fromHtml("<b>Author: </b><span>" + experiment.getOwner() + "</span>"));
        descriptionTextView.setText(Html.fromHtml("<b>Description: </b><span>" + experiment.getDescription() + "</span>"));

        return view;
    }
}
