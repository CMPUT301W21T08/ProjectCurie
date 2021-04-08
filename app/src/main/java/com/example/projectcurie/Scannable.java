package com.example.projectcurie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * This class implements the basic functionality for a scannable which, when scanned, submits an
 * associated trial result to the database. It is extended by BarCode and QRCode.
 * @author Joshua Billson
 */
public abstract class Scannable implements Serializable {

    private String code;
    private Experiment experiment;

    private int count;
    private boolean success;
    private double measurement;


    /**
     * Creates a bitmap of the scannable that can be presented to the user.
     * @return
     *     The bitmap representation of this scannable.
     */
    abstract Bitmap show();

    /**
     * Tests whether or not a given code matches this scannable.
     * @param code
     *     The code whose equality we want to test.
     * @return
     *     True if a match is found, false otherwise.
     */
    public boolean isEqual(String code) {
        return this.code.equals(code);
    }

    /**
     * Submit a new trial to the experiment associated with this scannable.
     * @param context
     *     The activity that called this method.
     */
    public void submitTrial(@NotNull Context context) {
        if (experiment.getType() == ExperimentType.COUNT) {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context).run();
        } else if (experiment.getType() == ExperimentType.INTEGER_COUNT) {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context, count).run();
        } else if (experiment.getType() == ExperimentType.BINOMIAL) {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context, success).run();
        } else {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context, measurement).run();
        }
    }

    /**
     * Creates a bitmap with bounding whitespace such that it fits nicely on a standard piece of
     * A4 paper.
     * @return
     *     The bitmap representation of this scannable that we want to print.
     */
    public Bitmap printableBitmap() {
        int borderSize = 800;
        Bitmap bitmap = show();
        Bitmap bmpWithBorder = Bitmap.createBitmap(bitmap.getWidth() + borderSize * 2, bitmap.getHeight() + borderSize * 2, bitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    /**
     * Used to display the trial outcome that this scannable represents for use in a list view.
     * @return
     *     A string representation of the trial result that this scannable will submit when scanned.
     */
    public String getOutcome() {
        if (experiment.getType() == ExperimentType.COUNT) {
            return "1";
        } else if (experiment.getType() == ExperimentType.INTEGER_COUNT) {
            return String.format(Locale.CANADA, "%d", count);
        } else if (experiment.getType() == ExperimentType.BINOMIAL) {
            return success ? "True" : "False";
        } else {
            return String.format(Locale.CANADA, "%f", measurement);
        }
    }

    /**
     * Used to display the trial type (binomial, measurement, count) that this scannable represents
     * for use in a list view.
     * @return
     *     A string representation of the type of trial that this scannable will submit when scanned.
     */
    public String getType() {
        if (experiment.getType() == ExperimentType.BINOMIAL) {
            return "Outcome";
        } else if (experiment.getType() == ExperimentType.MEASUREMENT) {
            return "Measurement";
        } else {
            return "Count";
        }
    }

    public String getTitle() {
        return experiment.getTitle();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
