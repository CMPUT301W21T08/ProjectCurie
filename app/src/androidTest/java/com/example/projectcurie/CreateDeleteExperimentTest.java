package com.example.projectcurie;


import android.os.Looper;
import android.os.Handler;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Executes test for deleting and creating experiment
 * @author Joshua Billson
 */
public class CreateDeleteExperimentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<WelcomeActivity> mActivityTestRule = new ActivityTestRule<>(WelcomeActivity.class, true, true);

    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), mActivityTestRule.getActivity());
    }

    @Test
    public void createExperimentTest(){
        /* Navigate To New Experiment Activity */
        solo.waitForText("Start");
        solo.clickOnButton("Start");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.addExperiment_btn));
        solo.waitForActivity(".NewExperimentActivity");

        /* Create New Experiment */
        solo.enterText((EditText) solo.getView(R.id.titleEditText), "Delete This Experiment");
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "This is a description");
        solo.enterText((EditText) solo.getView(R.id.minTrialsEditText), "1");
        solo.enterText((EditText) solo.getView(R.id.regionEditText), "Edmonton");
        solo.clickOnButton("Create Experiment");

        /* Navigate To Profile */
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.view_profile_btn));
        solo.waitForActivity(".UserProfileActivity");

        /* Test That Experiment Exists */
        assertTrue(solo.waitForText("Delete This Experiment"));
    }

    @Test
    public void deleteExperimentTest(){
        /* Navigate To Profile */
        solo.waitForText("Start");
        solo.clickOnButton("Start");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.view_profile_btn));
        solo.waitForActivity(".UserProfileActivity");

        /* Test That Experiment Exists */
        solo.waitForText("Delete This Experiment");

        /* Delete Experiment */
        solo.clickLongOnText("Delete This Experiment");
        solo.waitForDialogToOpen();
        solo.clickOnButton("Delete");

        /* Test That Experiment No Longer Exists */
        solo.waitForDialogToClose();
        solo.waitForCondition(() -> !solo.searchText("Delete This Experiment"), 5000);
        assertFalse(solo.searchText("Delete This Experiment"));
    }
}
