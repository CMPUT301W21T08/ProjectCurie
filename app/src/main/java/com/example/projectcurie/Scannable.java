package com.example.projectcurie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;

public abstract class Scannable implements Serializable {

    private String code;
    private Experiment experiment;

    private int count;
    private boolean success;
    private double measurement;


    abstract Bitmap show();

    public boolean isEqual(String code) {
        return this.code.equals(code);
    }

    public boolean submitTrial(@NotNull Context context) {
        if (experiment.getType() == ExperimentType.COUNT) {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context).run();
        } else if (experiment.getType() == ExperimentType.INTEGER_COUNT) {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context, count).run();
        } else if (experiment.getType() == ExperimentType.BINOMIAL) {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context, success).run();
        } else {
            new SubmitTrialCommand(experiment, App.getUser().getUsername(), context, measurement).run();
        }
        return true;
    }

    public Bitmap printableBitmap() {
        int borderSize = 800;
        Bitmap bitmap = show();
        Bitmap bmpWithBorder = Bitmap.createBitmap(bitmap.getWidth() + borderSize * 2, bitmap.getHeight() + borderSize * 2, bitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, borderSize, borderSize, null);
        return bmpWithBorder;
    }

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
