package com.example.projectcurie;


public interface SubmitTrialActivityInterface {
    void uploadTrial();

    void uploadTrial(int value);

    void uploadTrial(double value);

    void uploadTrial(boolean value);

    void addBarcode(String barcodeString);

    void addBarcode(String barcodeString, int value);

    void addBarcode(String barcodeString, double value);

    void addBarcode(String barcodeString, boolean value);

    void addQR();

    void addQR(int value);

    void addQR(double value);

    void addQR(boolean value);
}
