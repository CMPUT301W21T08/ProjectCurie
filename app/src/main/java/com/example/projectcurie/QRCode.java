package com.example.projectcurie;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Random;

public class QRCode extends Scannable {

    private final String code;
    private final Experiment experiment;

    private int count;
    private boolean success;
    private double measurement;

    public QRCode(Experiment experiment) {
        if (! (experiment.getType() == ExperimentType.COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Count With This Constructor!");
        } else {
            this.code = generateCode();
            this.experiment = experiment;
            this.count = 1;
        }
    }

    public QRCode(Experiment experiment, int count) {
        if (! (experiment.getType() == ExperimentType.INTEGER_COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Integer Count With This Constructor!");
        } else {
            this.code = generateCode();
            this.experiment = experiment;
            this.count = count;
        }
    }

    public QRCode(Experiment experiment, boolean success) {
        if (! (experiment.getType() == ExperimentType.BINOMIAL)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Binomial With This Constructor!");
        } else {
            this.code = generateCode();
            this.experiment = experiment;
            this.success = success;
        }
    }

    public QRCode(Experiment experiment, double measurement) {
        if (! (experiment.getType() == ExperimentType.MEASUREMENT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Measurement With This Constructor!");
        } else {
            this.code = generateCode();
            this.experiment = experiment;
            this.measurement = measurement;
        }
    }

    @Override
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

    @Override
    public boolean isEqual(String code) {
        return this.code.equals(code);
    }

    @Override
    public Bitmap show() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 600, 600);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
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

    @Override
    public String getTitle() {
        return experiment.getTitle();
    }

    @Override
    public String getType() {
        if (experiment.getType() == ExperimentType.BINOMIAL) {
            return "Outcome";
        } else if (experiment.getType() == ExperimentType.MEASUREMENT) {
            return "Measurement";
        } else {
            return "Count";
        }
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 16; i++) {
            code.append(String.format(Locale.CANADA, "%d", rand.nextInt(10)));
        }
        return code.toString();
    }
}
