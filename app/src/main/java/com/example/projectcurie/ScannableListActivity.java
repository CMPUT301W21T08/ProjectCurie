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

            return false;
        });
    }


    /**
     * Displays a barcode or QR code in a dialog fragment and gives the user the option to print the
     * barcode/QR to a piece of paper.
     * @author Joshua Billson
     */
    public static class ScannableDialogFragment extends DialogFragment {
        private BarCode barCode;
        private QRCode qrCode;

        /** Obligatory Empty Constructor */
        public ScannableDialogFragment() { }

        /**
         * Create a new instance of this dialog fragment with the intent of showing/printing a barcode.
         * @param barCode
         *     The barcode we want this dialog to display/print.
         * @return
         *     A new instance of this class.
         */
        public static ScannableDialogFragment newInstance(BarCode barCode) {
            ScannableDialogFragment fragment = new ScannableDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable("barcode", barCode);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Create a new instance of this dialog fragment with the intent of showing/printing a QR code.
         * @param qrCode
         *     The QR code we want this dialog to display/print.
         * @return
         *     A new instance of this class.
         */
        public static ScannableDialogFragment newInstance(QRCode qrCode) {
            ScannableDialogFragment fragment = new ScannableDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable("qrCode", qrCode);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.barCode = (BarCode) getArguments().getSerializable("barcode");
            this.qrCode = (QRCode) getArguments().getSerializable("qrCode");
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            /* Inflate Layout */
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_barcode_dialog, null);
            ImageView barcodeImageView = view.findViewById(R.id.barcodeImageView);
            TextView barcodeNumberTextView = view.findViewById(R.id.barcodeNumberTextView);

            /* Show A Bitmap Of The Barcode/QR Inside The Dialog Fragment */
            if (barCode != null) {
                barcodeImageView.setImageBitmap(barCode.show());
                barcodeNumberTextView.setText(barCode.getCode());
            } else if (qrCode != null) {
                barcodeImageView.setImageBitmap(qrCode.show());
                barcodeImageView.setPadding(0, 64, 0, 0);
                barcodeNumberTextView.setVisibility(View.GONE);
            } else {
                Log.e("Error", "Could Not Fetch Scannable From Dialog Arguments!");
            }

            /* Create Dialog Fragment */
            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setNegativeButton("Back", null)
                    .setPositiveButton("Print", (dialog, which) -> {
                        PrintHelper printHelper = new PrintHelper(getActivity());
                        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                        printHelper.setOrientation(PrintHelper.ORIENTATION_PORTRAIT);
                        printHelper.printBitmap("Print Barcode", (barCode != null) ? barCode.printableBitmap() : qrCode.printableBitmap());
                    })
                    .create();
        }
    }
}