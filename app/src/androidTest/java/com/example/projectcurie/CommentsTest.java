package com.example.projectcurie;


import android.Manifest;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertTrue;

/**
 * Tests adding questions and answers to an experiment.
 * @author Joshua Billson
 */
public class CommentsTest {
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

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            mActivityTestRule.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        });

        /* Create Mock Experiment */
        solo.unlockScreen();
        solo.waitForText("Start");
        solo.clickOnText("Start");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.addExperiment_btn));
        solo.waitForActivity(".NewExperimentActivity");

        solo.enterText((EditText) solo.getView(R.id.titleEditText), "Delete This Experiment");
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "This is a description");
        solo.enterText((EditText) solo.getView(R.id.minTrialsEditText), "1");
        solo.enterText((EditText) solo.getView(R.id.regionEditText), "Edmonton");
        closeSoftKeyboard();
        solo.clickOnView(solo.getView(R.id.createExperimentButton));
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
    public void addAnswerTest() throws InterruptedException {
        /* Navigate To Experiment Overview */
        solo.clickOnView(solo.getView(R.id.viewExperiments_btn));
        onView(withId(R.id.viewExperiments_btn));
        solo.waitForActivity(".ExperimentListActivty");
        solo.clickOnText("Delete This Experiment");
        solo.waitForActivity(".ExperimentOverviewActivity");
        onView(withText("Comments")).perform(click());

        /* Add Comment */
        Thread.sleep(1000);
        solo.clickOnText("New Comment");
        solo.waitForDialogToOpen();
        solo.enterText((EditText) solo.getView(R.id.addQuestionEditText), "This is a question");
        closeSoftKeyboard();
        onView(withText("Submit")).perform(click());
        solo.waitForDialogToClose();
        assertTrue(solo.searchText("This is a question"));
        solo.clickOnText("This is a question");

        /* Add Answer */
        Thread.sleep(1000);
        onView(withText("New Answer")).perform(click());
        solo.waitForDialogToOpen();
        solo.enterText((EditText) solo.getView(R.id.addAnswerEditText), "This is an answer");
        closeSoftKeyboard();
        onView(withText("Submit")).perform(click());
        solo.waitForDialogToClose();
        assertTrue(solo.searchText("This is an answer"));
    }
}
