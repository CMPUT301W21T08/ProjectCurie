package com.example.projectcurie;

import org.junit.Test;

import java.lang.reflect.Array;
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

        assertEquals(10.0, statistics.mean(), 0.001);
    }

    @Test
    public void testIntegerCount() {
        /* Create Data */
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        int[] valuesSingle = {1};
        int[] valuesPair = {1, 3};
        int[] valuesSmallOdd = {1, 3, 7};
        int[] valuesSmallEven = {1, 3, 7, 9};
        int[] valuesEven = {1, 3, 7, 9, 11, 19, 21, 22, 28, 35};
        int[] valuesOdd = {1, 3, 7, 9, 11, 19, 21, 22, 28};

        /* Initialize Statistics For Empty Number Of Trials */
        ArrayList<Trial> trialsEmpty = new ArrayList<>();
        ExperimentStatistics statisticsEmpty = new ExperimentStatistics(trialsEmpty);

        /* Initialize Statistics For Single Trial */
        ArrayList<Trial> trialsSingle = new ArrayList<>();
        trialsSingle.add(new IntegerCountTrial(experiment, author, valuesSingle[0]));
        ExperimentStatistics statisticsSingle = new ExperimentStatistics(trialsSingle);

        /* Initialize Statistics For Pair Of Trials */
        ArrayList<Trial> trialsPair = new ArrayList<>();
        trialsPair.add(new IntegerCountTrial(experiment, author, valuesPair[0]));
        trialsPair.add(new IntegerCountTrial(experiment, author, valuesPair[1]));
        ExperimentStatistics statisticsPair = new ExperimentStatistics(trialsPair);

        /* Initialize Statistics For Small Number Of Odd Trials */
        ArrayList<Trial> trialsSmallOdd = new ArrayList<>();
        for (int value : valuesSmallOdd) {
            trialsSmallOdd.add(new IntegerCountTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsSmallOdd = new ExperimentStatistics(trialsSmallOdd);

        /* Initialize Statistics For Small Number Of Even Trials */
        ArrayList<Trial> trialsSmallEven = new ArrayList<>();
        for (int value : valuesSmallEven) {
            trialsSmallEven.add(new IntegerCountTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsSmallEven = new ExperimentStatistics(trialsSmallEven);

        /* Initialize Statistics For Even Number Of Trials */
        ArrayList<Trial> trialsEven = new ArrayList<>();
        for (int value : valuesEven) {
            trialsEven.add(new IntegerCountTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsEven = new ExperimentStatistics(trialsEven);

        /* Initialize Statistics For Odd Number Of Trials */
        ArrayList<Trial> trialsOdd = new ArrayList<>();
        for (int value : valuesOdd) {
            trialsOdd.add(new IntegerCountTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsOdd = new ExperimentStatistics(trialsOdd);

        /* Test For Empty Number Of Trials */
        assertEquals(0.0, statisticsEmpty.mean(), 0.0);
        assertEquals(0.0, statisticsEmpty.median(), 0.0);
        assertEquals(0.0, statisticsEmpty.lowerQuartile(), 0.0);
        assertEquals(0.0, statisticsEmpty.upperQuartile(), 0.0);
        assertEquals(0.0, statisticsEmpty.standardDeviation(), 0.0);

        /* Test For Single Trial */
        assertEquals(1.0, statisticsSingle.mean(), 0.0);
        assertEquals(1.0, statisticsSingle.median(), 0.0);
        assertEquals(1.0, statisticsSingle.lowerQuartile(), 0.0);
        assertEquals(1.0, statisticsSingle.upperQuartile(), 0.0);
        assertEquals(0.0, statisticsSingle.standardDeviation(), 0.0);

        /* Test For Pair Of Trials */
        assertEquals(2.0, statisticsPair.mean(), 0.0);
        assertEquals(2.0, statisticsPair.median(), 0.0);
        assertEquals(2.0, statisticsPair.lowerQuartile(), 0.0);
        assertEquals(2.0, statisticsPair.upperQuartile(), 0.0);
        assertEquals(1.0, statisticsPair.standardDeviation(), 0.0);

        /* Test For Small Number Of Odd Trials */
        assertEquals(3.66, statisticsSmallOdd.mean(), 0.01);
        assertEquals(3.0, statisticsSmallOdd.median(), 0.0);
        assertEquals(1.0, statisticsSmallOdd.lowerQuartile(), 0.0);
        assertEquals(7.0, statisticsSmallOdd.upperQuartile(), 0.0);
        assertEquals(2.49, statisticsSmallOdd.standardDeviation(), 0.01);

        /* Test For Small Number Of Even Trials */
        assertEquals(5.0, statisticsSmallEven.mean(), 0.01);
        assertEquals(5.0, statisticsSmallEven.median(), 0.0);
        assertEquals(1.0, statisticsSmallEven.lowerQuartile(), 0.0);
        assertEquals(9.0, statisticsSmallEven.upperQuartile(), 0.0);
        assertEquals(3.16, statisticsSmallEven.standardDeviation(), 0.01);

        /* Test For Even Number Of Trials */
        assertEquals(15.6, statisticsEven.mean(), 0.01);
        assertEquals(15.0, statisticsEven.median(), 0.01);
        assertEquals(5.0, statisticsEven.lowerQuartile(), 0.01);
        assertEquals(25.0, statisticsEven.upperQuartile(), 0.01);
        assertEquals(10.59, statisticsEven.standardDeviation(), 0.01);

        /* Test For Odd Number Of Trials */
        assertEquals(13.44, statisticsOdd.mean(), 0.1);
        assertEquals(11.0, statisticsOdd.median(), 0.01);
        assertEquals(5.0, statisticsOdd.lowerQuartile(), 0.01);
        assertEquals(21.5, statisticsOdd.upperQuartile(), 0.01);
        assertEquals(8.84, statisticsOdd.standardDeviation(), 0.01);
    }

    @Test
    public void testMeasurement() {
        String experiment = "Mock Experiment";
        String author = "MockUser87";
        double[] valuesSingle = {3.4};
        double[] valuesPair = {3.4, 5.6};
        double[] valuesSmallOdd = {3.4, 5.6, 6.3};
        double[] valuesSmallEven = {3.4, 5.6, 6.3, 7.2};
        double[] valuesEven = {3.4, 5.6, 6.3, 7.2, 11.4, 13.4, 14.8, 21.5, 33.6, 67.3};
        double[] valuesOdd = {3.4, 5.6, 6.3, 7.2, 11.4, 13.4, 14.8, 21.5, 33.6};

        /* Initialize Statistics For Empty Number Of Trials */
        ArrayList<Trial> trialsEmpty = new ArrayList<>();
        ExperimentStatistics statisticsEmpty = new ExperimentStatistics(trialsEmpty);

        /* Initialize Statistics For Single Trial */
        ArrayList<Trial> trialsSingle = new ArrayList<>();
        trialsSingle.add(new MeasurementTrial(experiment, author, valuesSingle[0]));
        ExperimentStatistics statisticsSingle = new ExperimentStatistics(trialsSingle);

        /* Initialize Statistics For Pair Of Trials */
        ArrayList<Trial> trialsPair = new ArrayList<>();
        trialsPair.add(new MeasurementTrial(experiment, author, valuesPair[0]));
        trialsPair.add(new MeasurementTrial(experiment, author, valuesPair[1]));
        ExperimentStatistics statisticsPair = new ExperimentStatistics(trialsPair);

        /* Initialize Statistics For Small Number Of Odd Trials */
        ArrayList<Trial> trialsSmallOdd = new ArrayList<>();
        for (double value : valuesSmallOdd) {
            trialsSmallOdd.add(new MeasurementTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsSmallOdd = new ExperimentStatistics(trialsSmallOdd);

        /* Initialize Statistics For Small Number Of Even Trials */
        ArrayList<Trial> trialsSmallEven = new ArrayList<>();
        for (double value : valuesSmallEven) {
            trialsSmallEven.add(new MeasurementTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsSmallEven = new ExperimentStatistics(trialsSmallEven);

        /* Initialize Statistics For Even Number Of Trials */
        ArrayList<Trial> trialsEven = new ArrayList<>();
        for (double value : valuesEven) {
            trialsEven.add(new MeasurementTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsEven = new ExperimentStatistics(trialsEven);

        /* Initialize Statistics For Odd Number Of Trials */
        ArrayList<Trial> trialsOdd = new ArrayList<>();
        for (double value : valuesOdd) {
            trialsOdd.add(new MeasurementTrial(experiment, author, value));
        }
        ExperimentStatistics statisticsOdd = new ExperimentStatistics(trialsOdd);

        /* Test For Empty Number Of Trials */
        assertEquals(0.0, statisticsEmpty.mean(), 0.0);
        assertEquals(0.0, statisticsEmpty.median(), 0.0);
        assertEquals(0.0, statisticsEmpty.lowerQuartile(), 0.0);
        assertEquals(0.0, statisticsEmpty.upperQuartile(), 0.0);
        assertEquals(0.0, statisticsEmpty.standardDeviation(), 0.0);

        /* Test For Single Trial */
        assertEquals(3.4, statisticsSingle.mean(), 0.0);
        assertEquals(3.4, statisticsSingle.median(), 0.0);
        assertEquals(3.4, statisticsSingle.lowerQuartile(), 0.0);
        assertEquals(3.4, statisticsSingle.upperQuartile(), 0.0);
        assertEquals(0.0, statisticsSingle.standardDeviation(), 0.0);

        /* Test For Pair Of Trials */
        assertEquals(4.5, statisticsPair.mean(), 0.01);
        assertEquals(4.5, statisticsPair.median(), 0.01);
        assertEquals(4.5, statisticsPair.lowerQuartile(), 0.01);
        assertEquals(4.5, statisticsPair.upperQuartile(), 0.01);
        assertEquals(1.1, statisticsPair.standardDeviation(), 0.01);

        /* Test For Small Number Of Odd Trials */
        assertEquals(5.1, statisticsSmallOdd.mean(), 0.01);
        assertEquals(5.6, statisticsSmallOdd.median(), 0.0);
        assertEquals(3.4, statisticsSmallOdd.lowerQuartile(), 0.0);
        assertEquals(6.3, statisticsSmallOdd.upperQuartile(), 0.0);
        assertEquals(1.23, statisticsSmallOdd.standardDeviation(), 0.01);

        /* Test For Small Number Of Even Trials */
        assertEquals(5.62, statisticsSmallEven.mean(), 0.01);
        assertEquals(5.95, statisticsSmallEven.median(), 0.01);
        assertEquals(3.4, statisticsSmallEven.lowerQuartile(), 0.0);
        assertEquals(7.2, statisticsSmallEven.upperQuartile(), 0.0);
        assertEquals(1.40, statisticsSmallEven.standardDeviation(), 0.01);

        /* Test For Even Number Of Trials */
        assertEquals(18.45, statisticsEven.mean(), 0.01);
        assertEquals(12.4, statisticsEven.median(), 0.01);
        assertEquals(5.95, statisticsEven.lowerQuartile(), 0.01);
        assertEquals(27.55, statisticsEven.upperQuartile(), 0.01);
        assertEquals(18.38, statisticsEven.standardDeviation(), 0.01);

        /* Test For Odd Number Of Trials */
        assertEquals(13.02, statisticsOdd.mean(), 0.1);
        assertEquals(11.4, statisticsOdd.median(), 0.01);
        assertEquals(5.95, statisticsOdd.lowerQuartile(), 0.01);
        assertEquals(18.15, statisticsOdd.upperQuartile(), 0.01);
        assertEquals(8.99, statisticsOdd.standardDeviation(), 0.01);
    }
}
