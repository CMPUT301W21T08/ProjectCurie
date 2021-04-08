package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Implements an activity for viewing, deleting, and printing all QR codes and barcodes that the
 * user has registered with the app.
 * @author Joshua Billson
 */
public class ScannableListActivity extends AppCompatActivity {

    ArrayList<Scannable> scannables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannable_list);


        /* Initialize The Scannable List View */
        scannables = App.getScannables();
        ListView listView = findViewById(R.id.scannableListView);
        ScannableArrayAdapter arrayAdapter = new ScannableArrayAdapter(this, scannables);
        listView.setAdapter(arrayAdapter);

        /* Set An On Click Listener For Viewing/Printing A Barcode Or QR Code When It Is Tapped */
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (scannables.get(position) instanceof BarCode) {
                ScannableDialogFragment.newInstance((BarCode) scannables.get(position)).show(getSupportFragmentManager(), "SHOW BARCODE DIALOG FRAGMENT");
            } else {
                ScannableDialogFragment.newInstance((QRCode) scannables.get(position)).show(getSupportFragmentManager(), "SHOW BARCODE DIALOG FRAGMENT");
            }
        });

        /* Set An On Click Listener For Deleting A Barcode Or QR Code When It Is Long-Pressed */
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Scannable scannable = scannables.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setIcon(android.R.drawable.ic_delete)
                    .setMessage(String.format(Locale.CANADA, "Delete This %s?", (scannable instanceof BarCode) ? "Barcode" : "QR Code"))
                    .setPositiveButton("Delete", (dialog, which) -> {
                        try {
                            App.removeScannable(scannable.getCode());
                            scannables = App.getScannables();
                            arrayAdapter.notifyDataSetChanged();
                            Toast.makeText(this,
                                    String.format(Locale.CANADA, "Deleted %s!", (scannable instanceof BarCode) ? "Barcode" : "QR Code"),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();

            return true;
        });
    }
}