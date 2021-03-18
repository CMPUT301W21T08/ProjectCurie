package com.example.projectcurie;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Unit test class for Experiment class
 * @author Paul Cleofas, Joshua Billson
 *
 */
public class ExperimentTest {
    Experiment experiment;

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
        assertTrue(tokens.contains("bitcoin"));
        assertTrue(tokens.contains("edmonton"));
        assertTrue(tokens.contains("interested"));
        assertTrue(tokens.contains("crypto7843"));
        assertFalse(tokens.contains("."));
        assertFalse(tokens.contains("we"));


        experiment.setOwner("MissPeach87");
        tokens = experiment.getTokens();
        assertTrue(tokens.contains("misspeach87"));
        assertFalse(tokens.contains("crypto7843"));

    }

    @Test
    public void testSubscribe() {
        String randomUser = "NoobMaster69";
        ArrayList<String> subscriptions = new ArrayList<String>();
        experiment.setSubscriptions(subscriptions);
        assertTrue(experiment.getSubscriptions().isEmpty());
        experiment.subscribe(randomUser);
        assertTrue(experiment.getSubscriptions().contains(randomUser));
    }

    @Test
    public void testUnsubscribe() {
        String randomUser = "NoobMaster69";
        ArrayList<String> subscriptions = new ArrayList<String>();
        subscriptions.add(randomUser);
        experiment.setSubscriptions(subscriptions);
        assertTrue(experiment.getSubscriptions().contains(randomUser));
        experiment.unsubscribe(randomUser);
        assertTrue(experiment.getSubscriptions().isEmpty());
    }

    @Test
    public void testIsSubscribed() {
        String randomUser = "NoobMaster69";
        ArrayList<String> subscriptions = new ArrayList<String>();
        experiment.setSubscriptions(subscriptions);
        experiment.subscribe(randomUser);
        assertTrue(experiment.isSubscribed(randomUser));
    }

    @Test
    public void testGetTitle(){
        String title = "Title";
        experiment.setTitle(title);
        assertEquals(title, experiment.getTitle());
    }

    @Test
    public void testSetTitle(){
        String title = "Awesome Butterflies";
        experiment.setTitle(title);
        assertTrue(experiment.getTokens().contains("awesome"));
        assertTrue(experiment.getTokens().contains("butterflies"));
    }

    @Test
    public void testGetDescription(){
        String description = "The flying thingies are colorful and eye-catching";
        experiment.setDescription(description);
        assertEquals(description, experiment.getDescription());
    }

    @Test
    public void testSetDescription(){
        String description = "The flying thingies are colorful and eye-catching";;
        experiment.setDescription(description);
        assertFalse(experiment.getTokens().contains("the"));
        assertTrue(experiment.getTokens().contains("flying"));
        assertTrue(experiment.getTokens().contains("thingies"));
        assertTrue(experiment.getTokens().contains("colorful"));
        assertTrue(experiment.getTokens().contains("eye"));
        assertTrue(experiment.getTokens().contains("catching"));
    }

    @Test
    public void testGetRegion(){
        String region = "Manila";
        experiment.setRegion(region);
        experiment.setRegion(region);
        assertEquals(region, experiment.getRegion());
    }

    @Test
    public void testSetRegion(){
        String region = "Manila";
        experiment.setRegion(region);
        assertTrue(experiment.getTokens().contains("manila"));
    }

    @Test
    public void testGetOwner(){
        String owner = "Thor Odinson";
        experiment.setOwner(owner);
        assertEquals(owner, experiment.getOwner());
    }

    @Test
    public void testSetOwner(){
        String owner = "ThorOdinson10";
        experiment.setOwner(owner);
        assertTrue(experiment.getTokens().contains("thorodinson10"));
    }

    @Test
    public void testIsGeolocationRequired() {
        boolean require = true;
        experiment.setGeolocationRequired(require);
        assertTrue(experiment.isGeolocationRequired());
        require = false;
        experiment.setGeolocationRequired(require);
        assertFalse(experiment.isGeolocationRequired());
    }

    @Test
    public void testSetGeolocationRequired(){
        experiment.setGeolocationRequired(true);
        assertEquals(true, experiment.isGeolocationRequired());
        experiment.setGeolocationRequired(false);
        assertEquals(false, experiment.isGeolocationRequired());
    }

    @Test
    public void testIsLocked() {
        boolean require = true;
        experiment.setLocked(require);
        assertTrue(experiment.isLocked());
        require = false;
        experiment.setLocked(require);
        assertFalse(experiment.isLocked());
    }

    @Test
    public void testSetLocked(){
        experiment.setLocked(true);
        assertEquals(true, experiment.isLocked());
        experiment.setLocked(false);
        assertEquals(false, experiment.isLocked());
    }

    @Test
    public void testGetType(){
        ExperimentType type = ExperimentType.BINOMIAL;
        experiment.setType(type);
        assertEquals(ExperimentType.BINOMIAL, experiment.getType());
        type = ExperimentType.INTEGER_COUNT;
        experiment.setType(type);
        assertEquals(ExperimentType.INTEGER_COUNT, experiment.getType());
        type = ExperimentType.COUNT;
        experiment.setType(type);
        assertEquals(ExperimentType.COUNT, experiment.getType());
        type = ExperimentType.MEASUREMENT;
        experiment.setType(type);
        assertEquals(ExperimentType.MEASUREMENT, experiment.getType());
    }

    @Test
    public void testSetType(){
        experiment.setType(ExperimentType.BINOMIAL);
        ExperimentType type1 = experiment.getType();
        experiment.setType(ExperimentType.MEASUREMENT);
        ExperimentType type2 = experiment.getType();
        assertNotEquals(type1, type2);
        assertNotEquals(ExperimentType.INTEGER_COUNT, experiment.getType());
    }
}
