package com.example.projectcurie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class ScannableArrayAdapter extends ArrayAdapter<Scannable> {

    private final Context context;
    private final ArrayList<Scannable> scannables;

    public ScannableArrayAdapter(@NonNull Context context, @NonNull ArrayList<Scannable> scannables) {
        super(context, 0, scannables);
        this.context = context;
        this.scannables = scannables;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /* Inflate the view if necessary */
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.scannable_list_item, parent, false);
        }

        /* Grab Widgets */
        TextView titleTextView = view.findViewById(R.id.scannableTitleTextView);
        TextView outcomeTextView = view.findViewById(R.id.scannableOutcomeTextView);

        /* Set The Text Views To The Appropriate Experiment Fields */
        Scannable scannable = scannables.get(position);
        titleTextView.setText(scannable.getTitle());
        outcomeTextView.setText(String.format(Locale.CANADA, "%s: %s", scannable.getType(), scannable.getOutcome()));

        return view;
    }
}
