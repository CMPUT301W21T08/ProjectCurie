package com.example.projectcurie;

import android.graphics.Color;
import android.text.Html;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Provides a link between the database and UI. This class is responsible for monitoring a collection
 * of trials submitted to a particular experiment and rendering some statistics about said trials
 * to the screen. Any changes detected in the underlying data will result in re-rendering the UI
 * to display the most up-to-date statistics.
 * @author Joshua Billson
 */
public class TrialController {

    private ExperimentStatistics statistics;
    private TrialFetcher fetcher;
    private View view;

    /**
     * Create a new TrialController which will monitor the database for trials submitted to a
     * given experiment and render the statistics to a given View.
     * @param view
     *     The Activity or Fragment to which we want to render the trial statistics.
     */
    public TrialController(View view) {
        ArrayList<Trial> trials = new ArrayList<>();
        this.fetcher = new TrialFetcher(trials);
        this.statistics = new ExperimentStatistics(trials);
        this.view = view;
    }

    /**
     * Sets a listener on the trials associated with the experiment that this controller is interested
     * in. After initially acquiring the trials and rendering their statistics to the IU, it will
     * monitor for any changes in the database. If such a change occurs in the relevant collection
     * of trials, it will re-fetch the data and re-render the UI to display the current statistics.
     * @param experiment
     *     The experiment whose trials we are interested in.
     */
    public void postStatistics(Experiment experiment) {
        /* Grab Widgets */
        TextView trialCountTextView = view.findViewById(R.id.trialCountTextView);
        TextView trialMeanTextView = view.findViewById(R.id.trialMeanTextView);
        TextView trialMedianTextView = view.findViewById(R.id.trialMedianTextView);
        TextView trialLowerQuartileTextView = view.findViewById(R.id.trialLowerQuartileTextView);
        TextView trialUpperQuartileTextView = view.findViewById(R.id.trialUpperQuartileTextView);
        TextView trialStandardDeviationTextView = view.findViewById(R.id.trialStandardDeviationTextView);
        BarChart barChart = view.findViewById(R.id.barChart);

        /* Hide Quartiles And Standard Deviation For Binomial Experiments */
        if (experiment.getType() == ExperimentType.BINOMIAL) {
            trialLowerQuartileTextView.setVisibility(View.GONE);
            trialUpperQuartileTextView.setVisibility(View.GONE);
            trialStandardDeviationTextView.setVisibility(View.GONE);
        }

        /* Hide The Bar Chart Description */
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        barChart.setDrawBorders(true);

        this.fetcher.fetchTrials(experiment, () -> {
            /* Show Statistics */
            statistics.notifyDataChanged();
            trialCountTextView.setText(Html.fromHtml("<b>Total Trials: </b><span>" + String.format(Locale.CANADA, "%d", statistics.totalCount()) + "</span>"));
            trialMeanTextView.setText(Html.fromHtml(((experiment.getType() == ExperimentType.BINOMIAL)?"<b>Success Rate: </b><span>" : "<b>Mean: </b><span>") + String.format(Locale.CANADA, "%.2f", statistics.mean()) + "</span>"));
            trialMedianTextView.setText(Html.fromHtml("<b>Median: </b><span>" + String.format(Locale.CANADA, "%.2f", statistics.median()) + "</span>"));
            trialLowerQuartileTextView.setText(Html.fromHtml("<b>Lower Quartile: </b><span>" + String.format(Locale.CANADA, "%.2f", statistics.lowerQuartile()) + "</span>"));
            trialUpperQuartileTextView.setText(Html.fromHtml("<b>Upper Quartile: </b><span>" + String.format(Locale.CANADA, "%.2f", statistics.upperQuartile()) + "</span>"));
            trialStandardDeviationTextView.setText(Html.fromHtml("<b>Standard Deviation: </b><span>" + String.format(Locale.CANADA, "%.2f", statistics.standardDeviation()) + "</span>"));

            /* Hide Charts If No Trials Have Been Submitted */
            if (statistics.totalCount() == 0) {
                barChart.setVisibility(View.GONE);
            } else {
                barChart.setVisibility(View.VISIBLE);
            }

            /* Get Histogram Data */
            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            int numberOfBins = statistics.populateHistogram(entries, labels);
            BarDataSet barDataSet = new BarDataSet(entries, experiment.getTitle());
            barDataSet.setColor(Color.parseColor("#03A9F4"));
            BarData data = new BarData(barDataSet);
            data.setValueTextSize(16f);
            data.setDrawValues(false);
            barChart.setData(data);

            /* Setup Histogram Labels */
            XAxis xAxis = barChart.getXAxis();
            xAxis.setLabelCount(numberOfBins);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.invalidate();
        });
    }
}
