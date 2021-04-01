package com.example.projectcurie;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.InvalidParameterException;

public class BarCode extends Scannable {

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

    public static boolean invalidCode(String code) {
        int upc_e_length = 6;
        int ean_8_length = 8;
        int upc_a_length = 12;
        int ean_13_length = 13;
        int len = code.length();
        return len != upc_e_length && len != ean_8_length && len != upc_a_length && len != ean_13_length;
    }
}
