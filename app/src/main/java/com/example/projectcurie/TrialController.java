package com.example.projectcurie;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
        ScatterChart lineChart = view.findViewById(R.id.scatterChart);

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

        /* Set Line Chart Properties */
        Description description2 = new Description();
        description2.setEnabled(false);
        lineChart.setDescription(description2);
        lineChart.setDrawBorders(true);

        /* When New Data Is Available, Compute Statistics And Render To The UI */
        this.fetcher.fetchTrials(experiment, () -> {

            /* Create A New Thread To Compute Statistics */
            new Thread(() -> {
                /* Calculate Statistics */
                statistics.notifyDataChanged();
                int total = statistics.totalCount();
                double mean = statistics.mean();
                double median = statistics.median();
                double lowerQuartile = statistics.lowerQuartile();
                double upperQuartile = statistics.upperQuartile();
                double standardDeviation = statistics.standardDeviation();

                /* Get Histogram Data */
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                int numberOfBins = statistics.populateHistogram(entries, labels);
                BarDataSet barDataSet = new BarDataSet(entries, "Observations By Value");
                barDataSet.setColor(Color.parseColor("#03A9F4"));
                BarData data = new BarData(barDataSet);
                data.setValueTextSize(16f);
                data.setDrawValues(false);

                /* Get Scatter Plot Data */
                ArrayList<Entry> lineEntries = new ArrayList<>();
                float granularity = statistics.populateScatterChart(lineEntries);
                ScatterDataSet scatterDataSet = new ScatterDataSet(lineEntries, (experiment.getType() == ExperimentType.COUNT) ? "Counts Per Day" : "Results Over Time");
                scatterDataSet.setColor(Color.parseColor("#03A9F4"));
                scatterDataSet.setScatterShape(ScatterChart.ScatterShape.X);
                scatterDataSet.setScatterShapeSize(22f);
                ScatterData scatterData = new ScatterData(scatterDataSet);

                /* Render Results To UI */
                new Handler(Looper.getMainLooper()).post(() -> {
                    /* Show Statistics */
                    trialCountTextView.setText(Html.fromHtml("<b>Total Trials: </b><span>" + String.format(Locale.CANADA, "%d", total) + "</span>"));
                    trialMeanTextView.setText(Html.fromHtml(((experiment.getType() == ExperimentType.BINOMIAL)?"<b>Success Rate: </b><span>" : "<b>Mean: </b><span>") + String.format(Locale.CANADA, "%.2f", mean) + "</span>"));
                    trialMedianTextView.setText(Html.fromHtml("<b>Median: </b><span>" + String.format(Locale.CANADA, "%.2f", median) + "</span>"));
                    trialLowerQuartileTextView.setText(Html.fromHtml("<b>Lower Quartile: </b><span>" + String.format(Locale.CANADA, "%.2f", lowerQuartile) + "</span>"));
                    trialUpperQuartileTextView.setText(Html.fromHtml("<b>Upper Quartile: </b><span>" + String.format(Locale.CANADA, "%.2f", upperQuartile) + "</span>"));
                    trialStandardDeviationTextView.setText(Html.fromHtml("<b>Standard Deviation: </b><span>" + String.format(Locale.CANADA, "%.2f", standardDeviation) + "</span>"));

                    /* Show Histogram */
                    if (statistics.totalCount() == 0) {
                        barChart.setVisibility(View.GONE);
                    } else {
                        barChart.setVisibility(View.VISIBLE);
                        barChart.setData(data);
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setLabelCount(numberOfBins);
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChart.invalidate();
                    }

                    /* Show Line Chart */
                    if (statistics.totalCount() == 0) {
                        lineChart.setVisibility(View.GONE);
                    } else {
                        lineChart.setData(scatterData);
                        XAxis xAxis1 = lineChart.getXAxis();
                        xAxis1.setValueFormatter(new DateAxisValueFormatter());
                        xAxis1.setGranularity(Math.max((float) granularity, 1f));
                        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
                        lineChart.invalidate();
                    }
                });
            }).start();
        });
    }

    /* Used To Format Trial Time Values Into Date Strings */
    private static class DateAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yy");
            long longValue = Double.valueOf(value).longValue();
            return sdf.format(new Date(longValue));
        }
    }
}
