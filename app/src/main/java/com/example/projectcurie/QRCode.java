package com.example.projectcurie;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Random;

/**
 * This class implements the functionality for a QR code which, when scanned, submits an associated
 * trial result to the database.
 * @author Joshua Billson
 */
public class QRCode extends Scannable {

    /**
     * Constructor for a QR code associated with a count experiment.
     * @param experiment
     *     The experiment to which this QR code should submit trials.
     * @throws InvalidParameterException
     *     If this constructor is called with a non count experiment, it should throw an error.
     */
    public QRCode(Experiment experiment) {
        if (! (experiment.getType() == ExperimentType.COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Count With This Constructor!");
        } else {
            setCode(generateCode());
            setExperiment(experiment);
            setCount(1);
        }
    }

    /**
     * Constructor for a QR code associated with an integer-count experiment.
     * @param experiment
     *     The experiment to which this QR code should submit trials.
     * @param count
     *     The integer count that scanning this QR code should submit.
     * @throws InvalidParameterException
     *     If this constructor is called with a non integer-count experiment, it should throw an error.
     */
    public QRCode(Experiment experiment, int count) {
        if (! (experiment.getType() == ExperimentType.INTEGER_COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Integer Count With This Constructor!");
        } else {
            setCode(generateCode());
            setExperiment(experiment);
            setCount(count);
        }
    }

    /**
     * Constructor for a QR code associated with a binomial experiment.
     * @param experiment
     *     The experiment to which this QR code should submit trials.
     * @param success
     *     The trial outcome (pass or fail) that scanning this QR code should submit.
     * @throws InvalidParameterException
     *     If this constructor is called with a non binomial experiment, it should throw an error.
     */
    public QRCode(Experiment experiment, boolean success) {
        if (! (experiment.getType() == ExperimentType.BINOMIAL)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Binomial With This Constructor!");
        } else {
            setCode(generateCode());
            setExperiment(experiment);
            setSuccess(success);
        }
    }

    /**
     * Constructor for a QR code associated with a measurement experiment.
     * @param experiment
     *     The experiment to which this QR code should submit trials.
     * @param measurement
     *     The real-valued measurement that scanning this QR code should submit.
     * @throws InvalidParameterException
     *     If this constructor is called with a non measurement experiment, it should throw an error.
     */
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

    /* Helper that generates a new random and unique QR code */
    private String generateCode() {
        StringBuilder code = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 16; i++) {
            code.append(String.format(Locale.CANADA, "%d", rand.nextInt(10)));
        }
        return code.toString();
    }
}
