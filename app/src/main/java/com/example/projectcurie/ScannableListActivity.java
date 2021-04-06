package com.example.projectcurie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.print.PrintHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ScannableListActivity extends AppCompatActivity {

    ArrayList<Scannable> scannables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannable_list);


        scannables = App.getScannables();
        ListView listView = findViewById(R.id.scannableListView);
        ScannableArrayAdapter arrayAdapter = new ScannableArrayAdapter(this, scannables);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (scannables.get(position) instanceof BarCode) {
                ScannableDialogFragment.newInstance((BarCode) scannables.get(position)).show(getSupportFragmentManager(), "SHOW BARCODE DIALOG FRAGMENT");
            } else {
                ScannableDialogFragment.newInstance((QRCode) scannables.get(position)).show(getSupportFragmentManager(), "SHOW BARCODE DIALOG FRAGMENT");
            }
        });

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