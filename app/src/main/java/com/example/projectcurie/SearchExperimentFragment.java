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
    private SearchExperimentFragmentInteractionListener listener;

    /**
     * Implemented by this Fragment's parent Activity to conduct a database search for experiments
     * whose searchable fields match some given keywords.
     */
    public interface SearchExperimentFragmentInteractionListener {
        void goSearchExperiment(String keywords);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchExperimentFragmentInteractionListener){
            listener = (SearchExperimentFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
                    listener.goSearchExperiment(keyword);
                }).create();
    }
}