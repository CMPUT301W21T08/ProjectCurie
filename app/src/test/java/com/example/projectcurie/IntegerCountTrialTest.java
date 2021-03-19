package com.example.projectcurie;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
//public IntegerCountTrial(String experiment, String author, int count) {
//        super(experiment, author);
//        this.count = count;
//        }
public class IntegerCountTrialTest {
    private Experiment experiment;
    private IntegerCountTrial intCount;
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
        intCount = new IntegerCountTrial("TestExperiment","TestAuthor",100);
        Assert.assertEquals(100, intCount.getCount());
        Assert.assertNotEquals(null, intCount.getCount());

    }

    @Test
    public void setCount() {
        intCount = new IntegerCountTrial("TestExperiment","TestAuthor",100);
        intCount.setCount(9999);
        Assert.assertEquals(9999, intCount.getCount());
        Assert.assertNotEquals(null, intCount.getCount());

    }

}
