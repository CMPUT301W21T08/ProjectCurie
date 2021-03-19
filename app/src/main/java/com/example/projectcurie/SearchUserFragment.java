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

import org.jetbrains.annotations.NotNull;

/**
 * This class implements the fragment for taking in a username to be searched.
 * The username is case sensitive.
 * Currently has a bug where if you type nothing in the box, app will crash.
 * @author Klyde Pausang
 */
public class SearchUserFragment extends DialogFragment {
    private EditText search_keyword;
    private SearchUserFragment.SearchUserFragmentInteractionListener listener;

    /**
     * Implemented by this Fragment's parent Activity to conduct a database search of a given
     * user and display the results.
     */
    public interface SearchUserFragmentInteractionListener {
        void goSearchUser(@NotNull String keywords);
    }

    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchUserFragment.SearchUserFragmentInteractionListener){
            listener = (SearchUserFragment.SearchUserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NotNull Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_user, null);
        search_keyword = view.findViewById(R.id.search_keyword_editText1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Search User")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    String keyword = search_keyword.getText().toString();
                    listener.goSearchUser(keyword);
                }).create();
    }
}
