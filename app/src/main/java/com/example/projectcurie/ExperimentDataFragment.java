package com.example.projectcurie;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * This class currently doesn't do anything interesting.
 * This class will implement experiment data and statistics in phase 4.
 */
public class ExperimentDataFragment extends Fragment implements DatabaseListener {

    private Experiment experiment;
    private ArrayList<Trial> trials;
    private ExperimentStatistics statistics;
    private TrialFactory trialFactory;

    private TextView trialCountTextView;
    private TextView trialMeanTextView;
    private TextView trialMedianTextView;
    private TextView trialLowerQuartileTextView;
    private TextView trialUpperQuartileTextView;
    private TextView trialStandardDeviationTextView;
    private BarChart barChart;
    private ScatterChart lineChart;

    public ExperimentDataFragment() {
    }

    public static ExperimentDataFragment newInstance(Experiment experiment) {
        ExperimentDataFragment fragment = new ExperimentDataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("experiment", experiment);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseController.getInstance().stopWatchingTrials();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.experiment = (Experiment) getArguments().getSerializable("experiment");
        this.trialFactory = new TrialFactory(this.experiment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Grab Widgets */
        View view = inflater.inflate(R.layout.fragment_experiment_data, container, false);
        trialCountTextView = view.findViewById(R.id.trialCountTextView);
        trialMeanTextView = view.findViewById(R.id.trialMeanTextView);
        trialMedianTextView = view.findViewById(R.id.trialMedianTextView);
        trialLowerQuartileTextView = view.findViewById(R.id.trialLowerQuartileTextView);
        trialUpperQuartileTextView = view.findViewById(R.id.trialUpperQuartileTextView);
        trialStandardDeviationTextView = view.findViewById(R.id.trialStandardDeviationTextView);
        barChart = view.findViewById(R.id.barChart);
        lineChart = view.findViewById(R.id.scatterChart);

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
        this.trials = new ArrayList<>();
        this.statistics = new ExperimentStatistics(this.trials);
        DatabaseController.getInstance().watchTrials(experiment, this, 0);

        return view;
    }

    @Override
    public void notifyDataChanged(QuerySnapshot data, int returnCode) {
        /* Create A New Thread To Compute Statistics */
        new Thread(() -> {
            /* Calculate Statistics */
            extractTrials(data);
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
            BarData barData = new BarData(barDataSet);
            barData.setValueTextSize(16f);
            barData.setDrawValues(false);

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
                /* Animate Statistics */
                ArrayList<View> views = new ArrayList<>();
                Collections.addAll(views, trialCountTextView, trialMeanTextView, trialMedianTextView);

                /* Show Graphs If Number Of Trials Is Greater Than Zero */
                if (trials.size() > 0) {
                    Collections.addAll(views, barChart, lineChart);
                }

                /* Hide Quartiles And Standard Deviation For Binomial Experiments */
                if (experiment.getType() == ExperimentType.BINOMIAL) {
                    trialLowerQuartileTextView.setVisibility(View.GONE);
                    trialUpperQuartileTextView.setVisibility(View.GONE);
                    trialStandardDeviationTextView.setVisibility(View.GONE);
                } else {
                    Collections.addAll(views, trialLowerQuartileTextView, trialUpperQuartileTextView, trialStandardDeviationTextView);
                }

                for (View view : views) {
                    view.setVisibility(View.VISIBLE);
                    view.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .setListener(null);
                }

                /* Show Statistics */
                trialCountTextView.setText(Html.fromHtml("<b>Total Trials: </b><span>" + String.format(Locale.CANADA, "%d", total) + "</span>"));
                trialMeanTextView.setText(Html.fromHtml(((experiment.getType() == ExperimentType.BINOMIAL)?"<b>Success Rate: </b><span>" : "<b>Mean: </b><span>") + String.format(Locale.CANADA, "%.2f", mean) + "</span>"));
                trialMedianTextView.setText(Html.fromHtml("<b>Median: </b><span>" + String.format(Locale.CANADA, "%.2f", median) + "</span>"));
                trialLowerQuartileTextView.setText(Html.fromHtml("<b>Lower Quartile: </b><span>" + String.format(Locale.CANADA, "%.2f", lowerQuartile) + "</span>"));
                trialUpperQuartileTextView.setText(Html.fromHtml("<b>Upper Quartile: </b><span>" + String.format(Locale.CANADA, "%.2f", upperQuartile) + "</span>"));
                trialStandardDeviationTextView.setText(Html.fromHtml("<b>Standard Deviation: </b><span>" + String.format(Locale.CANADA, "%.2f", standardDeviation) + "</span>"));

                /* Show Histogram */
                if (trials.size() == 0) {
                    barChart.setVisibility(View.GONE);
                } else {
                    barChart.setData(barData);
                    barChart.getAxisLeft().setAxisMinimum(0f);
                    barChart.getAxisRight().setAxisMinimum(0f);
                    if (experiment.getType() == ExperimentType.BINOMIAL) {
                        barChart.getAxisLeft().setAxisMaximum(statistics.totalCount());
                        barChart.getAxisRight().setAxisMaximum(statistics.totalCount());
                    }
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setLabelCount(numberOfBins);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.invalidate();
                }

                /* Show Line Chart */
                if (trials.size() == 0) {
                    lineChart.setVisibility(View.GONE);
                } else {
                    lineChart.setData(scatterData);
                    lineChart.getAxisLeft().setAxisMinimum(0f);
                    lineChart.getAxisRight().setAxisMinimum(0f);
                    XAxis xAxis1 = lineChart.getXAxis();
                    xAxis1.setValueFormatter(new DateAxisValueFormatter());
                    xAxis1.setGranularity(Math.max((float) granularity, 1f));
                    xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.invalidate();
                }
            });
        }).start();
    }

    private void extractTrials(QuerySnapshot data) {
        trials.clear();
        if (data != null) {
            for (DocumentSnapshot document : data) {
                trials.add(trialFactory.getTrial(document));
            }
        }
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