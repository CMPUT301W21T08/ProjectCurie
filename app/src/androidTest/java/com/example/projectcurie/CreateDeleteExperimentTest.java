package com.example.projectcurie;


import android.Manifest;
import android.app.Instrumentation;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

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

    @Rule
    public GrantPermissionRule coarseLocationPermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Rule
    public GrantPermissionRule findLocationPermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule networkStatePermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_NETWORK_STATE);

    @Rule
    public GrantPermissionRule internetPermission = GrantPermissionRule.grant(Manifest.permission.INTERNET);

    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), mActivityTestRule.getActivity());
        solo.unlockScreen();
    }

    @Test
    public void createExperimentTest() {
        /* Navigate To New Experiment Activity */
        solo.waitForText("Start");
        solo.clickOnText("Start");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.addExperiment_btn));
        solo.waitForActivity(".NewExperimentActivity");

        /* Create New Experiment */
        solo.enterText((EditText) solo.getView(R.id.titleEditText), "Delete This Experiment");
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "This is a description");
        solo.enterText((EditText) solo.getView(R.id.minTrialsEditText), "1");
        solo.enterText((EditText) solo.getView(R.id.regionEditText), "Edmonton");
        solo.hideSoftKeyboard();
        solo.clickOnView(solo.getView(R.id.createExperimentButton));

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
        solo.clickOnText("Start");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.view_profile_btn));
        solo.waitForActivity(".UserProfileActivity");

        /* Test That Experiment Exists */
        assertTrue(solo.waitForText("Delete This Experiment"));

        /* Delete Experiment */
        solo.clickLongOnText("Delete This Experiment");
        solo.waitForDialogToOpen();
        solo.clickOnText("Delete");

        /* Test That Experiment No Longer Exists */
        solo.waitForDialogToClose();
        assertFalse(solo.searchText("Delete This Experiment"));
    }
}
