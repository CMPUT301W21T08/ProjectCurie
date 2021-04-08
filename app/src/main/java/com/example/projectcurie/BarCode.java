package com.example.projectcurie;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.InvalidParameterException;

/**
 * This class implements the functionality for a barcode which, when scanned, submits an associated
 * trial result to the database.
 * @author Joshua Billson
 */
public class BarCode extends Scannable {

    /**
     * Constructor for a barcode associated with a count experiment.
     * @param code
     *     The numerical code (represented as a string) of the barcode.
     * @param experiment
     *     The experiment to which this barcode should submit trials.
     * @throws InvalidParameterException
     *     If this constructor is called with a non count experiment, it should throw an error.
     */
    public BarCode(String code, Experiment experiment) throws InvalidParameterException {
        if (! (experiment.getType() == ExperimentType.COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Count With This Constructor!");
        } else if (invalidCode(code)) {
            throw new InvalidParameterException("Invalid Barcode Type! Accepted Formats Are UPC-A, UPC-E, EAN-8, & EAN-13");
        } else {
            setCode(code);
            setExperiment(experiment);
            setCount(1);
        }
    }

    /**
     * Constructor for a barcode associated with an integer count experiment.
     * @param code
     *     The numerical code (represented as a string) of the barcode.
     * @param experiment
     *     The experiment to which this barcode should submit trials.
     * @param count
     *     The integer count that scanning the barcode should submit.
     * @throws InvalidParameterException
     *     If this constructor is called with a non integer-count experiment, it should throw an error.
     */
    public BarCode(String code, Experiment experiment, int count) throws InvalidParameterException {
        if (! (experiment.getType() == ExperimentType.INTEGER_COUNT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Integer Count With This Constructor!");
        } else if (invalidCode(code)) {
            throw new InvalidParameterException("Invalid Barcode Type! Accepted Formats Are UPC-A, UPC-E, EAN-8, & EAN-13");
        } else {
            setCode(code);
            setExperiment(experiment);
            setCount(count);
        }
    }

    /**
     * Constructor for a barcode associated with a binomial experiment.
     * @param code
     *     The numerical code (represented as a string) of the barcode.
     * @param experiment
     *     The experiment to which this barcode should submit trials.
     * @param success
     *     The trial result (pass or fail) that scanning the barcode should submit.
     * @throws InvalidParameterException
     *     If this constructor is called with a non binomial experiment, it should throw an error.
     */
    public BarCode(String code, Experiment experiment, boolean success) throws InvalidParameterException {
        if (! (experiment.getType() == ExperimentType.BINOMIAL)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Binomial With This Constructor!");
        } else if (invalidCode(code)) {
            throw new InvalidParameterException("Invalid Barcode Type! Accepted Formats Are UPC-A, UPC-E, EAN-8, & EAN-13");
        } else {
            setCode(code);
            setExperiment(experiment);
            setSuccess(success);
        }
    }

    /**
     * Constructor for a barcode associated with a measurement experiment.
     * @param code
     *     The numerical code (represented as a string) of the barcode.
     * @param experiment
     *     The experiment to which this barcode should submit trials.
     * @param measurement
     *     The real-valued measurement that scanning this barcode should submit.
     * @throws InvalidParameterException
     *     If this constructor is called with a non measurement experiment, it should throw an error.
     */
    public BarCode(String code, Experiment experiment, double measurement) throws InvalidParameterException {
        if (! (experiment.getType() == ExperimentType.MEASUREMENT)) {
            throw new InvalidParameterException("Must Pass An Experiment Of Type Measurement With This Constructor!");
        } else if (invalidCode(code)) {
            throw new InvalidParameterException("Invalid Barcode Type! Accepted Formats Are UPC-A, UPC-E, EAN-8, & EAN-13");
        } else {
            setCode(code);
            setExperiment(experiment);
            setMeasurement(measurement);
        }
    }

    @Override
    public Bitmap show() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            switch (getCode().length()) {
                case 6:
                    return barcodeEncoder.encodeBitmap(getCode(), BarcodeFormat.UPC_E, 800, 400);
                case 8:
                    return barcodeEncoder.encodeBitmap(getCode(), BarcodeFormat.EAN_8, 800, 400);
                case 12:
                    return barcodeEncoder.encodeBitmap(getCode(), BarcodeFormat.UPC_A, 800, 400);
                case 13:
                    return barcodeEncoder.encodeBitmap(getCode(), BarcodeFormat.EAN_13, 800, 400);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Confirms that a given code is of a valid format (UPC-E, EAN-8, UPC-A, or EAN-13).
     * @param code
     *     The code we want to validate.
     * @return
     *     True if code is invalid, false otherwise.
     */
    public static boolean invalidCode(String code) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            switch (code.length()) {
                case 6:
                    barcodeEncoder.encodeBitmap(code, BarcodeFormat.UPC_E, 800, 400);
                    return false;
                case 8:
                    barcodeEncoder.encodeBitmap(code, BarcodeFormat.EAN_8, 800, 400);
                    return false;
                case 12:
                    barcodeEncoder.encodeBitmap(code, BarcodeFormat.UPC_A, 800, 400);
                    return false;
                case 13:
                    barcodeEncoder.encodeBitmap(code, BarcodeFormat.EAN_13, 800, 400);
                    return false;
            }
        } catch (WriterException e) {
            Log.e("Error", "Invalid Barcode Entered!");
        }
        return true;
    }
}
