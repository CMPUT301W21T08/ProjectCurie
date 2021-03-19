package com.example.projectcurie;

import org.junit.Assert;
import org.junit.Test;

public class MeasurementTrialTest {
    private Experiment experiment;
    private MeasurementTrial measure;
    Double test;
    public void mockExperiment() {
        experiment = new Experiment();
        experiment.setTitle("Bitcoin Vendors In Edmonton");
        experiment.setDescription("We are interested in all the bitcoin vendors in Edmonton.");
        experiment.setRegion("Edmonton");
        experiment.setOwner("Crypto7843");
    }

    @Test
    public void getMeasurement() {
        measure = new MeasurementTrial("testname","testAuthor",1000);
        String test = String.valueOf(measure.getMeasurement());
        Assert.assertEquals(test, "1000.0");
        Assert.assertNotEquals(null, test);

    }

    @Test
    public void setMeasurement() {
        measure = new MeasurementTrial("testname","testAuthor",1000);
        measure.setMeasurement(123);
        String test = String.valueOf(measure.getMeasurement());
        Assert.assertEquals(test, "123.0");
        Assert.assertNotEquals(null, test);
    }
}