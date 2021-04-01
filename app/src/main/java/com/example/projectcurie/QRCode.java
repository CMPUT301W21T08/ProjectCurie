package com.example.projectcurie;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Random;

public class QRCode extends Scannable {

    public QRCode(Experiment experiment) {
        if (! (experiment.getType() == ExperimentType.COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Count With This Constructor!");
        } else {
            setCode(generateCode());
            setExperiment(experiment);
            setCount(1);
        }
    }

    public QRCode(Experiment experiment, int count) {
        if (! (experiment.getType() == ExperimentType.INTEGER_COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Integer Count With This Constructor!");
        } else {
            setCode(generateCode());
            setExperiment(experiment);
            setCount(count);
        }
    }

    public QRCode(Experiment experiment, boolean success) {
        if (! (experiment.getType() == ExperimentType.BINOMIAL)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Binomial With This Constructor!");
        } else {
            setCode(generateCode());
            setExperiment(experiment);
            setSuccess(success);
        }
    }

    public QRCode(Experiment experiment, double measurement) {
        if (! (experiment.getType() == ExperimentType.MEASUREMENT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Measurement With This Constructor!");
        } else {
            setCode(generateCode());
            setExperiment(experiment);
            setMeasurement(measurement);
        }
    }

    @Override
    public Bitmap show() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.encodeBitmap(getCode(), BarcodeFormat.QR_CODE, 600, 600);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
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
