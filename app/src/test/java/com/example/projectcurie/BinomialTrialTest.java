package com.example.projectcurie;

import org.junit.Assert;
import org.junit.Test;

public class BinomialTrialTest {
    private Experiment experiment;
    private BinomialTrial binomialTrial;
    Double test;
    @Test
    public void isSuccess() {
        binomialTrial = new BinomialTrial("TestExperiment","TestAuthor",true);
        Assert.assertEquals(true, binomialTrial.isSuccess());
        Assert.assertNotEquals(false, binomialTrial.isSuccess());
    }

    @Test
    public void setSuccess() {
        binomialTrial = new BinomialTrial("TestExperiment","TestAuthor",true);
        binomialTrial.setSuccess(false);
        Assert.assertEquals(false, binomialTrial.isSuccess());
        Assert.assertNotEquals(true, binomialTrial.isSuccess());
    }
}
