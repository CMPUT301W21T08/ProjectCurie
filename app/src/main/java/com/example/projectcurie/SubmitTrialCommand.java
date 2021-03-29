package com.example.projectcurie;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Locale;

public class SubmitTrialCommand extends DatabaseCommand  {
    private final Experiment experiment;
    private final String author;
    private final Context context;

    private boolean success;
    private int count;
    private double measurement;

    public SubmitTrialCommand(Experiment experiment, String author, @NotNull Context context) {
        if (! (experiment.getType() == ExperimentType.COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Count With This Constructor!");
        } else {
            this.experiment = experiment;
            this.author = author;
            this.context = context;
        }
    }

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

    @Override
    public void execute(FirebaseFirestore db) {
        if (experiment.getType() == ExperimentType.COUNT) {
            CountTrial trial = (experiment.isGeolocationRequired()) ? new CountTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation()) : new CountTrial(experiment.getTitle(), author);
            uploadTrial(db, trial);
        } else if (experiment.getType() == ExperimentType.INTEGER_COUNT) {
            IntegerCountTrial trial = (experiment.isGeolocationRequired()) ? new IntegerCountTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation(), count) : new IntegerCountTrial(experiment.getTitle(), author, count);
            uploadTrial(db, trial);
        } else if (experiment.getType() == ExperimentType.BINOMIAL) {
            BinomialTrial trial = (experiment.isGeolocationRequired()) ? new BinomialTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation(), success) : new BinomialTrial(experiment.getTitle(), author, success);
            uploadTrial(db, trial);
        } else {
            MeasurementTrial trial = (experiment.isGeolocationRequired()) ? new MeasurementTrial(experiment.getTitle(), author, new GeoLocation(context).getLocation(), measurement) : new MeasurementTrial(experiment.getTitle(), author, measurement);
            uploadTrial(db, trial);
        }
}

    private void uploadTrial(FirebaseFirestore db, Trial trial) {
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
}
