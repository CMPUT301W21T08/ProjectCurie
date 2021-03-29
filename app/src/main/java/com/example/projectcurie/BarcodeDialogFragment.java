package com.example.projectcurie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;

/**
 * This class implements the fragment for taking in a username to be searched.
 * The username is case sensitive.
 * Currently has a bug where if you type nothing in the box, app will crash.
 * @author Klyde Pausang
 */
public class BarcodeDialogFragment extends DialogFragment {
    private BarCode barCode;
    private QRCode qrCode;

    public BarcodeDialogFragment() { }

    public static BarcodeDialogFragment newInstance(BarCode barCode) {
        BarcodeDialogFragment fragment = new BarcodeDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("barcode", barCode);
        fragment.setArguments(args);
        return fragment;
    }

    public static BarcodeDialogFragment newInstance(QRCode qrCode) {
        BarcodeDialogFragment fragment = new BarcodeDialogFragment();
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
    public Dialog onCreateDialog(@NotNull Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_barcode_dialog, null);
        ImageView barcodeImageView = view.findViewById(R.id.barcodeImageView);
        TextView barcodeNumberTextView = view.findViewById(R.id.barcodeNumberTextView);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
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
