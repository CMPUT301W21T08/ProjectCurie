package com.example.projectcurie;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExperimentStatisticsTest {
    private ExperimentStatistics statistics;

    @Test
    public void testBinomial() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        boolean[] values = {true, true, false, true, true, false, false, true, false, true};
        statistics = new ExperimentStatistics("Mock Experiment", ExperimentType.BINOMIAL);

        for (boolean value : values) {
            statistics.addTrial(new BinomialTrial(experiment, author, value));
        }

        assertEquals(0.6, statistics.mean(), 0.001);
    }

    @Test
    public void testCount() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        statistics = new ExperimentStatistics("Mock Experiment", ExperimentType.COUNT);

        for (int i = 0; i < 10; i++) {
            statistics.addTrial(new CountTrial(experiment, author));
        }

        assertEquals(1.0, statistics.mean(), 0.001);
    }

    @Test
    public void testIntegerCount() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        int[] values = {5, 1, 7, 3, 9, 11, 21, 7, 19, 28, 31, 22, 35};
        statistics = new ExperimentStatistics("Mock Experiment", ExperimentType.INTEGER_COUNT);

        for (int value : values) {
            statistics.addTrial(new IntegerCountTrial(experiment, author, value));
        }

        assertEquals(15.3, roundDouble(statistics.mean()), 0.1);
    }

    @Test
    public void testMeasurement() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        double[] values = {5.6, 3.4, 21.5, 6.3, 11.4, 67.3, 14.8, 7.3, 13.4, 33.6};
        statistics = new ExperimentStatistics("Mock Experiment", ExperimentType.MEASUREMENT);

        for (double value : values) {
            statistics.addTrial(new MeasurementTrial(experiment, author, value));
        }

        assertEquals(18.46, roundDouble(statistics.mean()), 0.1);
    }

    /* Helper Function That Rounds Doubles To 2 Decimal Places By Truncation */
    private double roundDouble(double value) {
        int x = (int) (value * 100);
        return ((double) x) / 100.0;
    }
}
