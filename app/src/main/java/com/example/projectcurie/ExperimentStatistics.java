package com.example.projectcurie;

import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.BiFunction;

/**
 * This class stores all trials associated with a given experiment and provides methods for
 * extracting common statistics from those trials.
 * @author Joshua Billson
 */
public class ExperimentStatistics implements Serializable {

    private ArrayList<Trial> trials;
    private ArrayList<Double> values;
    private boolean extracted = false;

    /**
     * Constructor for initializing a new ExperimentStatics object when creating a new Experiment.
     * @param trials
     *     An array of trials for which we want to compute some statistics.
     */
    public ExperimentStatistics(ArrayList<Trial> trials) {
        this.trials = trials;
        this.values = new ArrayList<>();
    }

    /**
     * Get the total number of trials submitted to this experiment.
     * @return
     *     The number of trials submitted to this experiment.
     */
    public int totalCount() {
        extractValues();
        return values.size();
    }

    public double min() {
        extractValues();
        if (trials.size() == 0) {
            return 0.0;
        } else {
            return values.get(0);
        }
    }

    public double max() {
        extractValues();
        if (trials.size() == 0) {
            return 0.0;
        } else {
            return values.get(values.size() - 1);
        }
    }

    /**
     * Get the mean of all trial results for this experiment.
     * @return
     *     The mean value of all trials.
     */
    public double mean() {
        extractValues();
        if (values.size() == 0) {
            return 0.0;
        } else {
            double total = 0.0;
            for (Double value : values) {
                total += value;
            }
            return total / ((double) values.size());
        }
    }

    public double median() {
        extractValues();
        if (values.size() == 0) {
            return 0.0;
        } else if ((values.size() % 2) == 0) {
            int medianIndex = (values.size() / 2) - 1;
            return (values.get(medianIndex) + values.get(medianIndex + 1)) / 2.0;
        } else {
            int medianIndex = (values.size() / 2);
            return values.get(medianIndex);
        }
    }

    public double lowerQuartile() {
        extractValues();
        if (values.size() == 0) {
            return 0.0;
        } else if (values.size() < 3) {
            return median();
        } else if (values.size() < 5) {
            return values.get(0);
        } else if ((values.size() % 2) == 0) {
            int medianIndex = (values.size() / 2) - 1;
            int firstQuartileIndex = ((medianIndex) / 2) - 1;
            return (values.get(firstQuartileIndex) + values.get(firstQuartileIndex + 1)) / 2.0;
        } else {
            int medianIndex = (values.size() / 2);
            int firstQuartileIndex = ((medianIndex) / 2) - 1;
            return (values.get(firstQuartileIndex) + values.get(firstQuartileIndex + 1)) / 2.0;
        }
    }

    public double upperQuartile() {
        extractValues();
        if (values.size() == 0) {
            return 0.0;
        } else if (values.size() < 3) {
            return median();
        }  else if (values.size() < 5) {
            return values.get(trials.size() - 1);
        } else if ((values.size() % 2) == 0) {
            int medianIndex = (values.size() / 2) - 1;
            int upperQuartileIndex = ((medianIndex) / 2) + medianIndex + 1;
            return (values.get(upperQuartileIndex) + values.get(upperQuartileIndex + 1)) / 2.0;
        } else {
            int medianIndex = (values.size() / 2);
            int upperQuartileIndex = ((medianIndex) / 2) + medianIndex;
            return (values.get(upperQuartileIndex) + values.get(upperQuartileIndex + 1)) / 2.0;
        }
    }

    public double standardDeviation() {
        extractValues();
        if (trials.size() == 0) {
            return 0.0;
        } else {
            double mean = mean();
            double accumulator = 0;
            for (Double value : values) {
                accumulator += Math.pow(value - mean, 2);
            }
            return Math.sqrt(accumulator / ((double) totalCount()));
        }
    }

    public int populateHistogram(ArrayList<BarEntry> entries, ArrayList<String> labels) {
        extractValues();
        if (trials.size() == 0) {
            return 0;
        } else if (trials.get(0) instanceof BinomialTrial) {
            return populateHistogramBinomial(entries, labels);
        } else if (trials.get(0) instanceof MeasurementTrial) {
            return populateHistogramMeasurement(entries, labels);
        } else {
            return populateHistogramIntegerCount(entries, labels);
        }
    }

    public float populateScatterChart(ArrayList<Entry> entries) {
        float granularity = 1.0f;
        trials.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
        if (trials.size() > 0) {

            if (trials.get(0) instanceof CountTrial) {

                /* Extract Counts Per Date */
                HashMap<String, Integer> countsByDate = new HashMap<>();
                for (Trial trial : trials) {
                    if (countsByDate.containsKey(trial.formattedDate())) {
                        Integer currentCount = countsByDate.get(trial.formattedDate());
                        countsByDate.put(trial.formattedDate(), currentCount + 1);
                    } else {
                        countsByDate.put(trial.formattedDate(), 1);
                    }
                }

                /* Put Counts Per Date In Line Chart Entries */
                for (String dateString : countsByDate.keySet()) {
                    try {
                        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
                        entries.add(new Entry(Long.valueOf(date.getTime()).floatValue(), (float) countsByDate.get(dateString)));
                    } catch (ParseException e) {
                        Log.e("Error", "Error Parsing Date!");
                    }
                }

                /* Calculate Axis Granularity */
                float axisMin = Float.MAX_VALUE;
                float axisMax = Float.MIN_VALUE;
                for (Entry entry : entries) {
                    if (entry.getX() < axisMin) {
                        axisMin = entry.getX();
                    }
                    if (entry.getX() > axisMax) {
                        axisMax = entry.getX();
                    }
                }
                granularity = ((float) axisMax - (float) axisMin) / 5.5f;

            } else {
                /* Get Values For Each Trial */
                for (Trial trial : trials) {
                    entries.add(new Entry(Long.valueOf(trial.getTimestamp().getTime()).floatValue(), (float) ExperimentStatistics.getValue(trial)));
                }

                /* Calculate Axis Granularity */
                long axisMin = trials.get(0).getTimestamp().getTime();
                long axisMax = trials.get(trials.size() - 1).getTimestamp().getTime();
                granularity = ((float) axisMax - (float) axisMin) / 5.5f;
            }
        }
        return granularity;
    }

    public void notifyDataChanged() {
        extracted = false;
    }

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    /* Helper method for extracting a value from different types of trial. */
    public static double getValue(Trial trial) {
        if (trial instanceof CountTrial) {
            CountTrial countTrial = (CountTrial) trial;
            return countTrial.getCount();
        } else if (trial instanceof IntegerCountTrial) {
            IntegerCountTrial integerCountTrial = (IntegerCountTrial) trial;
            return integerCountTrial.getCount();
        } else if (trial instanceof MeasurementTrial) {
            MeasurementTrial measurementTrial = (MeasurementTrial) trial;
            return measurementTrial.getMeasurement();
        } else {
            BinomialTrial binomialTrial = (BinomialTrial) trial;
            return (binomialTrial.isSuccess()) ? 1.0 : 0.0;
        }
    }

    private HashMap<String, Integer> extractCountByUser() {
        HashMap<String, Integer> countsByUser = new HashMap<>();
        for (Trial trial : trials) {
            if (countsByUser.containsKey(trial.getAuthor())) {
                Integer currentCount = countsByUser.get(trial.getAuthor());
                countsByUser.put(trial.getAuthor(), currentCount + 1);
            } else {
                countsByUser.put(trial.getAuthor(), 1);
            }
        }
        return countsByUser;
    }

    private void extractValues() {
        if (!extracted) {
            values.clear();
            if (trials.size() > 0) {
                if (trials.get(0) instanceof CountTrial) {
                    HashMap<String, Integer> trialsByUser = extractCountByUser();
                    for (String user : trialsByUser.keySet()) {
                        values.add((double) trialsByUser.get(user));
                    }
                } else {
                    for (Trial trial : trials) {
                        values.add(getValue(trial));
                    }
                }
            }
            extracted = true;
            values.sort(null);
        }
    }

    private int populateHistogramIntegerCount(ArrayList<BarEntry> entries, ArrayList<String> labels) {
        int countsSize = (int) max() + 1;
        int[] counts = new int[countsSize];
        Arrays.fill(counts, 0);

        for (Double value : values) {
            counts[(int) value.doubleValue()] += 1;
        }

        int numberOfBins = 0;
        int binIndex = 0;
        for (int i = 0; i < countsSize; i++) {
            if (counts[i] != 0) {
                entries.add(new BarEntry(binIndex++, counts[i]));
                labels.add(String.format(Locale.CANADA, "%d", i));
                numberOfBins++;
            }
        }

        return numberOfBins;
    }

    private int populateHistogramBinomial(ArrayList<BarEntry> entries, ArrayList<String> labels) {
        int falseTrials = 0;
        int trueTrials = 0;
        for (Double value : values) {
            if (value == 0.0) {
                falseTrials++;
            } else {
                trueTrials++;
            }
        }
        entries.add(new BarEntry(0, falseTrials));
        entries.add(new BarEntry(1, trueTrials));
        labels.add("False");
        labels.add("True");
        return 2;
    }

    private int populateHistogramMeasurement(ArrayList<BarEntry> entries, ArrayList<String> labels) {
        /* Calculate Bin Width & Number Of Bins */
        double min = min();
        double max = max();
        int numberOfBins = (int) Math.sqrt((double) values.size());
        double binWidth = (max - min) / ((double) numberOfBins);

        /* Add Bins To Entries */
        int[] counts = new int[numberOfBins];
        Arrays.fill(counts, 0);;
        for (Double value : values) {
            int bin = 0;
            while (value > (min + ((bin + 1) * binWidth))) {
                bin++;
            }
            counts[bin] += 1;
        }

        for (int i = 0; i < numberOfBins; i++) {
            entries.add(new BarEntry(i, counts[i]));
            labels.add(String.format(Locale.CANADA, "%.2f - %.2f", (min + (i * binWidth)), (min + ((i + 1) * binWidth))));
        }

        return numberOfBins;
    }
}
