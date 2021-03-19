package com.example.projectcurie;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class TrialTest {
    private Trial trial;
    private Date testDateJoined;
    @Test
    public void getTimestamp() {
        trial = new Trial("testExperiment","TestUserName");
        this.testDateJoined = new Date();
        Assert.assertEquals(trial.getTimestamp(),testDateJoined);
        Assert.assertNotEquals(null,testDateJoined);

    }

    @Test
    public void setTimestamp() {
        trial = new Trial("testExperiment","TestUserName");
        this.testDateJoined = new Date();
        trial.setTimestamp(testDateJoined);
        Assert.assertEquals(trial.getTimestamp(),testDateJoined);
        Assert.assertNotEquals(null,testDateJoined);
    }

    @Test
    public void getLatitude() {
        /// Not yet implemented
    }

    @Test
    public void setLatitude() {
        /// Not yet implemented
    }

    @Test
    public void getLongitude() {
        // Not yet implemented
    }

    @Test
    public void setLongitude() {
        // Not yet implemented
    }

    @Test
    public void getExperiment() {
        trial = new Trial("testExperiment","TestUserName");
        Assert.assertEquals(trial.getExperiment(),"testExperiment");
        Assert.assertNotEquals(null,"testExperiment");
    }

    @Test
    public void setExperiment() {
        trial = new Trial("testExperiment","TestUserName");
        trial.setExperiment("new!!!");
        Assert.assertEquals(trial.getExperiment(),"new!!!");
        Assert.assertNotEquals(null,"new!!!");
    }

    @Test
    public void getAuthor() {
        trial = new Trial("testExperiment","TestUserName");
        Assert.assertEquals(trial.getAuthor(),"TestUserName");
        Assert.assertNotEquals(null,"TestUserName");
    }

    @Test
    public void setAuthor() {
        trial = new Trial("testExperiment","TestUserName");
        trial.setAuthor("new!!!");
        Assert.assertEquals(trial.getAuthor(),"new!!!");
        Assert.assertNotEquals(null,"new!!!");
    }
}