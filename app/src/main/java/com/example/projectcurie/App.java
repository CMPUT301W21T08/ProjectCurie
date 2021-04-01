package com.example.projectcurie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides a global state for the application. State can be accessed from any Activity ot Fragment
 * bu calling its static getter methods.
 * @author Joshua Billson
 */
public class App extends Application {
    private static User user;
    private static App instance;
    private static SharedPreferences.Editor editor;
    private static ArrayList<QRCode> qrCodes = new ArrayList<>();
    private static ArrayList<BarCode> barCodes = new ArrayList<>();
    private static final ArrayList<Scannable> scannables = new ArrayList<>();


    /**
     * Called upon app startup. Implements a singleton design pattern by binding the constructed
     * instance to the static instance attribute.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            instance = this;
            editor = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE).edit();
            qrCodes = getQRCodes();
            barCodes = getBarcodes();
            scannables.addAll(barCodes);
            scannables.addAll(qrCodes);
        } catch (IOException e) {
            Log.e("Error", "Could Not Deserialize Scannables!");
            e.printStackTrace();
        }
    }

    /**
     *  Get the current user of the app.
     */
    public static User getUser() {
        return user;
    }

    /**
     * Set the current user of the app. Typically called after login/registration.
     * @param user
     *     The user of the app.
     */
    public static void setUser(User user) {
        App.user = user;
    }

    /**
     * Given a code, retrieve a matching scannable if it exists. If a match is not found, returns null.
     * @param code
     *     The unique identifying code associated with the barcode or QR.
     * @return
     *     The matching scannable if found, null otherwise.
     */
    public static Scannable getScannable(String code) {
        for (Scannable scannable : scannables) {
            Log.i("Bar Code Number", scannable.getCode());
            if (scannable.isEqual(code)) {
                return scannable;
            }
        }
        return null;
    }

    /**
     * Add a barcode or QR code for submitting a trial to this app. The code must be unique;
     * if the given code is already associated with a given trial it will be rejected. If
     * successful, the scannable will be saved to shared preference for persistence.
     * @param scannable
     *     The barcode we want to register.
     * @return
     *     Returns true if the barcode is not already registered, false otherwise.
     * @throws IOException
     *     Thrown if there is an error serializing the barcodes when saving to shared preferences.
     */
    public static boolean addScannable(Scannable scannable) throws IOException {
        if (getScannable(scannable.getCode()) == null) {
            scannables.add(scannable);
            if (scannable instanceof BarCode) {
                instance.saveBarcode((BarCode) scannable);
            } else {
                instance.saveQR((QRCode) scannable);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove a scannable from the app.
     * @param code
     *     The unique identifying code associated with the scannable.
     * @throws IOException
     *     Thrown if there is an error serializing the barcodes when updating shared preferences.
     */
    public static void removeScannable(String code) throws IOException {
        Scannable scannable = getScannable(code);
        assert scannable != null;
        scannables.remove(scannable);
        if (scannable instanceof BarCode) {
            instance.deleteBarcode((BarCode) scannable);
        } else {
            instance.deleteQR((QRCode) scannable);
        }

    }

    /**
     * Get a list of all scannables registered in this app.
     * @return
     *     The list of registered scannables.
     */
    public static ArrayList<Scannable> getScannables() {
        return scannables;
    }

    /* Helper Method For Retrieving Barcodes From Shared Preferences */
    private ArrayList<BarCode> getBarcodes() throws IOException {
        SharedPreferences sharedPreferences = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);
        String serialString = sharedPreferences.getString("BarCodes", ObjectSerializer.serialize(new ArrayList<BarCode>()));
        return (ArrayList<BarCode>) ObjectSerializer.deserialize(serialString);
    }

    /* Helper Method For Retrieving QR Codes From Shared Preferences */
    private ArrayList<QRCode> getQRCodes() throws IOException {
        SharedPreferences sharedPreferences = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);
        String serialString = sharedPreferences.getString("QRCodes", ObjectSerializer.serialize(new ArrayList<QRCode>()));
        return (ArrayList<QRCode>) ObjectSerializer.deserialize(serialString);
    }

    /* Helper Method For Saving A Barcode To Shared Preferences */
    private void saveBarcode(BarCode barCode) throws IOException {
        barCodes.add(barCode);
        editor.remove("BarCodes").apply();
        editor.putString("BarCodes", ObjectSerializer.serialize(barCodes)).apply();
    }

    /* Helper Method For Saving A QR Code To Shared Preferences */
    private void saveQR(QRCode qrCode) throws IOException {
        qrCodes.add(qrCode);
        editor.remove("QRCodes").apply();
        editor.putString("QRCodes", ObjectSerializer.serialize(qrCodes)).apply();
    }

    /* Helper Method For Deleting A Barcode From Shared Preferences */
    private void deleteBarcode(BarCode barcode) throws IOException {
        barCodes.remove(barcode);
        editor.remove("BarCodes").apply();
        editor.putString("BarCodes", ObjectSerializer.serialize(barCodes)).apply();
    }

    /* Helper Method For Deleting A QR Code From Shared Preferences */
    private void deleteQR(QRCode qrCode) throws IOException {
        qrCodes.remove(qrCode);
        editor.remove("QRCodes").apply();
        editor.putString("QRCodes", ObjectSerializer.serialize(qrCodes)).apply();
    }

}
