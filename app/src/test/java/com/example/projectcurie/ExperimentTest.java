package com.example.projectcurie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ExperimentTest {
    private Experiment experiment;

    @Before
    public void mockExperiment() {
        experiment = new Experiment();
    }

    @Test
    public void testTokenize() {
        experiment.setTitle("Bitcoin Vendors In Edmonton");
        experiment.setDescription("We are interested in all the bitcoin vendors in Edmonton.");
        experiment.setRegion("Edmonton");
        experiment.setOwner("Crypto7843");

        ArrayList<String> tokens = experiment.getTokens();
        Assert.assertTrue(tokens.contains("bitcoin"));
        Assert.assertTrue(tokens.contains("edmonton"));
        Assert.assertTrue(tokens.contains("interested"));
        Assert.assertTrue(tokens.contains("crypto7843"));
        Assert.assertFalse(tokens.contains("."));


        experiment.setOwner("MissPeach87");
        tokens = experiment.getTokens();
        Assert.assertTrue(tokens.contains("misspeach87"));
        Assert.assertFalse(tokens.contains("crypto7843"));

    }
}
