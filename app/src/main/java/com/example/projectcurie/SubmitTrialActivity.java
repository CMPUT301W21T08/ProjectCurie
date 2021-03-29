package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

/**
 * This activity allows the user to submit a trial results to a given experiment. The UI which is
 * presented to the user depends on the type of experiment (Binomial, Count, Integer Count, Measurement).
 */
public class SubmitTrialActivity extends AppCompatActivity implements SubmitTrialActivityInterface {

    private FrameLayout fragmentLayout;
    private Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trial);
        fragmentLayout = findViewById(R.id.trialFragmentLayout);

        /* Grab Data From Intent */
        this.experiment = (Experiment) getIntent().getSerializableExtra("experiment");

        /* Display Appropriate Fragment Depending On Experiment Type */
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (experiment.getType().ordinal()) {
            case (0):
                fragmentTransaction.replace(fragmentLayout.getId(), new CountTrialFragment());
                break;
            case (1):
                fragmentTransaction.replace(fragmentLayout.getId(), new IntegerCountTrialFragment());
                break;
            case (2):
                fragmentTransaction.replace(fragmentLayout.getId(), new MeasurementTrialFragment());
                break;
            case (3):
                fragmentTransaction.replace(fragmentLayout.getId(), new BinomialTrialFragment());
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void uploadTrial() {
        new SubmitTrialCommand(experiment, App.getUser().getUsername(), this).run();
    }

    @Override
    public void uploadTrial(int value) {
        new SubmitTrialCommand(experiment, App.getUser().getUsername(), this, value).run();
    }

    @Override
    public void uploadTrial(boolean value) {
        new SubmitTrialCommand(experiment, App.getUser().getUsername(), this, value).run();
    }

    @Override
    public void uploadTrial(double value) {
        new SubmitTrialCommand(experiment, App.getUser().getUsername(), this, value).run();
    }

    @Override
    public void addBarcode(String barcodeString) {
        try {
            BarCode barCode = new BarCode(barcodeString, experiment);
            if (App.addBarcode(barCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added Barcode To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Barcode Is Already In Use!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBarcode(String barcodeString, int value) {
        try {
            BarCode barCode = new BarCode(barcodeString, experiment, value);
            if (App.addBarcode(barCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added Barcode To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Barcode Is Already In Use!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBarcode(String barcodeString, boolean value) {
        try {
            BarCode barCode = new BarCode(barcodeString, experiment, value);
            if (App.addBarcode(barCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added Barcode To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Barcode Is Already In Use!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBarcode(String barcodeString, double value) {
        try {
            BarCode barCode = new BarCode(barcodeString, experiment, value);
            if (App.addBarcode(barCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added Barcode To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Barcode Is Already In Use!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addQR() {
        try {
            QRCode qrCode = new QRCode(experiment);
            if (App.addQR(qrCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added QR To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                addQR();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addQR(int value) {
        try {
            QRCode qrCode = new QRCode(experiment, value);
            if (App.addQR(qrCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added QR To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                addQR();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addQR(boolean value) {
        try {
            QRCode qrCode = new QRCode(experiment, value);
            if (App.addQR(qrCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added QR To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                addQR();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addQR(double value) {
        try {
            QRCode qrCode = new QRCode(experiment, value);
            if (App.addQR(qrCode)) {
                Toast.makeText(this, String.format(Locale.CANADA, "Added QR To %s!", experiment.getTitle()), Toast.LENGTH_SHORT).show();
            } else {
                addQR();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}