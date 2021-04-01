package com.example.projectcurie;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.Locale;

/**
 * Use this to submit a new trial to the FireStore database. Will disallow submission of a trial
 * in the event that the experiment is locked, author is unsubscribed, or the author is blacklisted
 * by the experiment owner. A success or error message will be displayed in the activity from
 * which this command originates upon completion.
 * @author Joshua Billson
 */
public class SubmitTrialCommand extends DatabaseCommand  {
    private final Experiment experiment;
    private final String author;
    private final Context context;

    private boolean success;
    private int count;
    private double measurement;

    /**
     * Use this constructor to submit a count trial to the database.
     * @param experiment
     *     The experiment to which we wish to submit a trial.
     * @param author
     *     The username of the person submitting the trial.
     * @param context
     *     The activity to which the results of the submission should be posted once an attempt
     *     has either successfully or unsuccessfully completed.
     */
    public SubmitTrialCommand(Experiment experiment, String author, @NotNull Context context) {
        if (! (experiment.getType() == ExperimentType.COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Count With This Constructor!");
        } else {
            this.experiment = experiment;
            this.author = author;
            this.context = context;
        }
    }

    /**
     * Use this constructor to submit a binomial trial to the database.
     * @param experiment
     *     The experiment to which we wish to submit a trial.
     * @param author
     *     The username of the person submitting the trial.
     * @param context
     *     The activity to which the results of the submission should be posted once an attempt
     *     has either successfully or unsuccessfully completed.
     * @param success
     *     The outcome of the experiment (true or false).
     */
    public SubmitTrialCommand(Experiment experiment, String author, @NotNull Context context, boolean success) {
        if (! (experiment.getType() == ExperimentType.BINOMIAL)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Binomial With This Constructor!");
        } else {
            this.experiment = experiment;
            this.author = author;
            this.success = success;
            this.context = context;
        }
    }

    /**
     * Use this constructor to submit an integer count trial to the database.
     * @param experiment
     *     The experiment to which we wish to submit a trial.
     * @param author
     *     The username of the person submitting the trial.
     * @param context
     *     The activity to which the results of the submission should be posted once an attempt
     *     has either successfully or unsuccessfully completed.
     * @param count
     *     The positive integer count recorded by the trial.
     */
    public SubmitTrialCommand(Experiment experiment, String author, @NotNull Context context, int count) {
        if (! (experiment.getType() == ExperimentType.INTEGER_COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Integer Count With This Constructor!");
        } else {
            this.experiment = experiment;
            this.author = author;
            this.count = count;
            this.context = context;
        }
    }

    /**
     * Use this constructor to submit a measurement count trial to the database.
     * @param experiment
     *     The experiment to which we wish to submit a trial.
     * @param author
     *     The username of the person submitting the trial.
     * @param context
     *     The activity to which the results of the submission should be posted once an attempt
     *     has either successfully or unsuccessfully completed.
     * @param measurement
     *     The real valued measurement recorded by the trial.
     */
    public SubmitTrialCommand(Experiment experiment, String author, @NotNull Context context, double measurement) {
        if (! (experiment.getType() == ExperimentType.MEASUREMENT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Measurement With This Constructor!");
        } else {
            this.experiment = experiment;
            this.author = author;
            this.measurement = measurement;
            this.context = context;
        }
    }

    /**
     * Submit a new trial to the FireStore database.
     * @param db
     *     A reference to the FireStore database to which we want to submit.
     */
    @Override
    public void execute(FirebaseFirestore db) {
        Trial trial = trialFactory(experiment.getType());
        DocumentReference reference = db.collection("experiments").document(experiment.getTitle());
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot documentSnapshot = transaction.get(reference);
            if (documentSnapshot.exists()) {

                /* Get Experiment From Database */
                Experiment experimentDocument = documentSnapshot.toObject(Experiment.class);
                if (experimentDocument == null) {
                    throw new FirebaseFirestoreException(String.format(Locale.CANADA, "Experiment %s Does Not Exist!", experiment.getTitle()), FirebaseFirestoreException.Code.NOT_FOUND);
                }

                /* Get Experiment Owner From Database */
                User experimentOwner = transaction.get(db.collection("users").document(experimentDocument.getOwner())).toObject(User.class);
                if (experimentOwner == null) {
                    throw new FirebaseFirestoreException(String.format(Locale.CANADA, "Experiment %s Is Corrupted!", experiment.getTitle()), FirebaseFirestoreException.Code.NOT_FOUND);
                }

                /* Submit New Trial To Experiment */
                if (experimentDocument.isLocked()) {
                    throw new FirebaseFirestoreException(String.format(Locale.CANADA, "Experiment %s Is Locked!", experiment.getTitle()), FirebaseFirestoreException.Code.NOT_FOUND);
                } else if (! experimentDocument.isSubscribed(author)) {
                    throw new FirebaseFirestoreException(String.format(Locale.CANADA, "You Are Not Subscribed To %s!", experiment.getTitle()), FirebaseFirestoreException.Code.NOT_FOUND);
                } else if (experimentOwner.isBlacklisted(author)) {
                    throw new FirebaseFirestoreException(String.format(Locale.CANADA, "You Are Blacklisted By User %s!", experimentOwner.getUsername()), FirebaseFirestoreException.Code.NOT_FOUND);
                } else {
                    transaction.set(documentSnapshot.getReference().collection("trials").document(), trial);
                }
            } else {
                throw new FirebaseFirestoreException("Experiment Does Not Exist!", FirebaseFirestoreException.Code.NOT_FOUND);
            }
            return null;
        })
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Submitted Trial To:\n" + experiment.getTitle() + "!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /* Helper Factory Method For Constructing A Trial Of The Appropriate Type */
    private Trial trialFactory(ExperimentType type) {
        if (type == ExperimentType.COUNT) {
            return (experiment.isGeolocationRequired()) ? new CountTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation()) : new CountTrial(experiment.getTitle(), author);
        } else if (type == ExperimentType.INTEGER_COUNT) {
            return (experiment.isGeolocationRequired()) ? new IntegerCountTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation(), count) : new IntegerCountTrial(experiment.getTitle(), author, count);
        } else if (type == ExperimentType.BINOMIAL) {
            return (experiment.isGeolocationRequired()) ? new BinomialTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation(), success) : new BinomialTrial(experiment.getTitle(), author, success);
        } else {
            return (experiment.isGeolocationRequired()) ? new MeasurementTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation(), measurement) : new MeasurementTrial(experiment.getTitle(), author, measurement);
        }
    }
}
