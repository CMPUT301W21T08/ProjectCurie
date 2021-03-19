package com.example.projectcurie;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExperimentStatisticsTest {
    private ExperimentStatistics statistics;

    @Test
    public void testBinomial() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        boolean[] values = {true, true, false, true, true, false, false, true, false, true};

        ArrayList<Trial> trials = new ArrayList<>();
        for (boolean value : values) {
            trials.add(new BinomialTrial(experiment, author, value));
        }
        statistics = new ExperimentStatistics(trials);

        assertEquals(0.6, statistics.mean(), 0.001);
    }

    @Test
    public void testCount() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";

        ArrayList<Trial> trials = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            trials.add(new CountTrial(experiment, author));
        }
        statistics = new ExperimentStatistics(trials);

        assertEquals(1.0, statistics.mean(), 0.001);
    }

    @Test
    public void testIntegerCount() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        int[] values = {5, 1, 7, 3, 9, 11, 21, 7, 19, 28, 31, 22, 35};

        ArrayList<Trial> trials = new ArrayList<>();
        for (int value : values) {
            trials.add(new IntegerCountTrial(experiment, author, value));
        }
        statistics = new ExperimentStatistics(trials);

        assertEquals(15.3, roundDouble(statistics.mean()), 0.1);
    }

    @Test
    public void testMeasurement() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        double[] values = {5.6, 3.4, 21.5, 6.3, 11.4, 67.3, 14.8, 7.3, 13.4, 33.6};

        ArrayList<Trial> trials = new ArrayList<>();
        for (double value : values) {
            trials.add(new MeasurementTrial(experiment, author, value));
        }
        statistics = new ExperimentStatistics(trials);

        assertEquals(18.46, roundDouble(statistics.mean()), 0.1);
    }

    /* Helper Function That Rounds Doubles To 2 Decimal Places By Truncation */
    private double roundDouble(double value) {
        int x = (int) (value * 100);
        return ((double) x) / 100.0;
    }
}
