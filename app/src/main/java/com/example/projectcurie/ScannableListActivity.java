package com.example.projectcurie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

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
                BarcodeDialogFragment.newInstance((BarCode) scannables.get(position)).show(getSupportFragmentManager(), "SHOW BARCODE DIALOG FRAGMENT");
            } else {
                BarcodeDialogFragment.newInstance((QRCode) scannables.get(position)).show(getSupportFragmentManager(), "SHOW BARCODE DIALOG FRAGMENT");
            }
        });
    }
}