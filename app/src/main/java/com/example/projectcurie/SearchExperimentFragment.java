package com.example.projectcurie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This Dialog Fragment presents the user with a simple text bar into which they can enter one or
 * more keywords by which to search for matching experiments. Upon submitting a search query, the
 * the user will be shown the results.
 */
public class SearchExperimentFragment extends DialogFragment {
    private EditText search_keyword;
    private DatabaseListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DatabaseListener){
            listener = (DatabaseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DatabaseListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_experiment, null);
        search_keyword = view.findViewById(R.id.search_keyword_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Search Experiments")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    String keyword = search_keyword.getText().toString().toLowerCase();
                    DatabaseController.getInstance().searchExperiments(keyword, listener);
                }).create();
    }
}