package com.example.projectcurie;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
//public IntegerCountTrial(String experiment, String author, int count) {
//        super(experiment, author);
//        this.count = count;
//        }
public class CountTrialTest {
    private Experiment experiment;
    private CountTrial count;
    Double test;

    public void mockExperiment() {
        experiment = new Experiment();
        experiment.setTitle("Bitcoin Vendors In Edmonton");
        experiment.setDescription("We are interested in all the bitcoin vendors in Edmonton.");
        experiment.setRegion("Edmonton");
        experiment.setOwner("Crypto7843");
    }
    @Test
    public void getCount() {
        count = new CountTrial("TestExperiment","TestAuthor");
        // CountTrial sets count to 1, when created
        Assert.assertEquals(1, count.getCount());
        Assert.assertNotEquals(null, count.getCount());

    }

    @Test
    public void setCount() {
        count = new CountTrial("TestExperiment","TestAuthor");
        count.setCount(9999);
        Assert.assertEquals(9999, count.getCount());
        Assert.assertNotEquals(null, count.getCount());

    }

}
