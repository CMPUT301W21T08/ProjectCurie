package com.example.projectcurie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.client.android.Intents;

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
    private static ArrayList<Scannable> scannables = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            instance = this;
            scannables.addAll(getBarcodes());
            scannables.addAll(getQRCodes());
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
     * Add a barcode for submitting a count trial to a given experiment. The barcode must be unique;
     * if the given code is already associated with a given trial it will be rejected.
     * @param barcode
     *     The barcode we want to register.
     * @return
     *     Returns true if the barcode is not already registered, false otherwise.
     * @throws IOException
     *     Thrown if there is an error serializing the barcodes when saving to shared preferences.
     */
    public static boolean addBarcode(BarCode barcode) throws IOException {
        if (getScannable(barcode.getCode()) == null) {
            scannables.add(barcode);
            instance.saveBarcode(barcode);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add a barcode for submitting a count trial to a given experiment. The barcode must be unique;
     * if the given code is already associated with a given trial it will be rejected.
     * @param qrCode
     *     The QR Code we want to register.
     * @return
     *     Returns true if the barcode is not already registered, false otherwise.
     * @throws IOException
     *     Thrown if there is an error serializing the barcodes when saving to shared preferences.
     */
    public static boolean addQR(QRCode qrCode) throws IOException {
        if (getScannable(qrCode.getCode()) == null) {
            scannables.add(qrCode);
            instance.saveQR(qrCode);
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<Scannable> getScannables() {
        return scannables;
    }

    private ArrayList<BarCode> getBarcodes() throws IOException {
        SharedPreferences sharedPreferences = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);
        String serialString = sharedPreferences.getString("BarCodes", ObjectSerializer.serialize(new ArrayList<BarCode>()));
        return (ArrayList<BarCode>) ObjectSerializer.deserialize(serialString);
    }

    private ArrayList<QRCode> getQRCodes() throws IOException {
        SharedPreferences sharedPreferences = this.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);
        String serialString = sharedPreferences.getString("QRCodes", ObjectSerializer.serialize(new ArrayList<QRCode>()));
        return (ArrayList<QRCode>) ObjectSerializer.deserialize(serialString);
    }

    private void saveBarcode(BarCode barCode) throws IOException {
        ArrayList<BarCode> barCodes = this.getBarcodes();
        barCodes.add(barCode);
        instance.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE).edit().putString("BarCodes", ObjectSerializer.serialize(barCodes)).apply();
    }

    private void saveQR(QRCode qrCode) throws IOException {
        ArrayList<QRCode> qrCodes = this.getQRCodes();
        qrCodes.add(qrCode);
        instance.getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE).edit().putString("QRCodes", ObjectSerializer.serialize(qrCodes)).apply();
    }

}
