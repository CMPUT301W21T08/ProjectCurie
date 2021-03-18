package com.example.projectcurie;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class UserTest {
    private User user;
    private Date testDateJoined;
    @Test
    public void removeBlacklisted() {
        user = new User("CoolGuy123");
        user.addBlacklist("UnCoolGuy900");
        user.removeBlacklisted("UnCoolGuy900");
        Assert.assertFalse(user.getBlacklisted().contains("UnCoolGuy900"));
    }

    @Test
    public void addBlacklist() {
    // Testing if the user can add to the black list
        user = new User("CoolGuy123");
        user.addBlacklist("UnCoolGuy900");

        Assert.assertTrue(user.getBlacklisted().contains("UnCoolGuy900"));
    }

    @Test
    public void isBlacklisted() {
        //isBlackListed should return true if the input is contained in the blacklist
        user = new User("CoolGuy123");
        user.addBlacklist("UnCoolGuy900");
        Assert.assertTrue("UnCoolGuy900", user.isBlacklisted("UnCoolGuy900"));    }

    @Test
    public void getUsername() {
        user = new User("CoolGuy123");

        Assert.assertEquals("CoolGuy123", user.getUsername());
    }

    @Test
    public void setUsername() {
        user = new User("CoolGuy123");
        user.setUsername("NiceDude4");
        Assert.assertEquals("NiceDude4", user.getUsername());

    }

    @Test
    public void getEmail() {
        user = new User("CoolGuy123");
        user.setEmail("coolguy123@gmail.com");
        Assert.assertEquals("coolguy123@gmail.com", user.getEmail());

    }

    @Test
    public void getAbout() {
        // Set new about for user, tests if users about is the same as the expected

        user = new User("CoolGuy123");
        user.setAbout("I am CoolGuy123, and I like to party");
        Assert.assertEquals("I am CoolGuy123, and I like to party", user.getAbout());
        Assert.assertNotEquals("yepyep", user.getAbout());

    }

    @Test
    public void setAbout() {
        // Similar to getAbout,  added another line to see if it changes after being set
        user = new User("CoolGuy123");
        user.setAbout("I am CoolGuy123, and I like to party");
        user.setAbout("TestAbout!!!");

        Assert.assertEquals("TestAbout!!!", user.getAbout());
        Assert.assertNotEquals("yooo", user.getAbout());

    }

    @Test
    public void getDateJoined() {
        // Checks get date by creating a new date and user
        // asserts True if both the dates are equal
        user = new User("CoolGuy123");
        this.testDateJoined = new Date();
        Assert.assertEquals(testDateJoined, user.getDateJoined());
    }

    @Test
    public void setBlacklisted() {
        // Checks get/setBlacklisted by creating a new array, adding an element to that array
        //  using setBlackListed to update the blacklist for that user,
        // asserts True if it contains the testBlackList Elements and false if it does not contain
        user = new User("CoolGuy123");
        ArrayList<String> testBlackList = new ArrayList<>();
        testBlackList.add("tester1!");
        user.setBlacklisted(testBlackList);
        Assert.assertTrue(user.getBlacklisted().contains("tester1!"));
        Assert.assertFalse(user.getBlacklisted().contains("tester2!"));
    }
}