package com.example.projectcurie;


import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Executes test for locking an experiment
 * @author Joshua Billson
 */
public class LockExperimentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<WelcomeActivity> mActivityTestRule = new ActivityTestRule<>(WelcomeActivity.class, true, true);

    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), mActivityTestRule.getActivity());

        /* Create Mock Experiment */
        solo.waitForText("Start");
        solo.clickOnButton("Start");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.addExperiment_btn));
        solo.waitForActivity(".NewExperimentActivity");

        solo.enterText((EditText) solo.getView(R.id.titleEditText), "Delete This Experiment");
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "This is a description");
        solo.enterText((EditText) solo.getView(R.id.minTrialsEditText), "1");
        solo.enterText((EditText) solo.getView(R.id.regionEditText), "Edmonton");
        solo.clickOnButton("Create Experiment");

        solo.goBack();
        solo.waitForActivity(".MainActivity");
    }

    @After
    public void tearDown() {
        /* Clean Up Database */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference experimentRef = db.collection("experiments").document("Delete This Experiment");
        CollectionReference questionsRef = experimentRef.collection("questions");
        questionsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        doc.getReference().collection("answers").get()
                                .addOnSuccessListener(snapshots -> {
                                    for (DocumentSnapshot document : snapshots) {
                                        document.getReference().delete();
                                    }
                                });
                        doc.getReference().delete();
                    }
                });
        experimentRef.delete();
    }

    @Test
    public void lockExperimentTest() {
        /* Navigate To Experiment Overview */
        solo.clickOnView(solo.getView(R.id.view_profile_btn));
        solo.waitForActivity(".UserProfileActivity");

        /* Test That Experiment Exists */
        assertTrue(solo.waitForText("Delete This Experiment"));

        /* Lock Experiment */
        solo.clickLongOnText("Delete This Experiment");
        solo.waitForDialogToOpen();
        solo.clickOnButton("Lock");
        solo.waitForDialogToClose();

        /* Unlock Experiment */
        solo.clickLongOnText("Delete This Experiment");
        solo.waitForDialogToOpen();
        solo.clickOnButton("Unlock");
        solo.waitForDialogToClose();
    }
}
