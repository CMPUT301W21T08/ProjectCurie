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
 * This class implements the fragment for inputting a new answer to a question.
 * User input for answer text fetched from this fragment and sent to addAnswer in AnswerListActivity.
 *
 * @author Bo Cen
 */
public class AddAnswerFragment extends DialogFragment {

    private EditText answer_body;
    AddAnswerDialogFragmentListener listener;

    /** Obligatory Empty Constructor */
    public AddAnswerFragment() { }

    /**
     * The activity which implements this interface agrees to post a new answer to the database given
     * a string returned from this DialogFragment.
     */
    public interface AddAnswerDialogFragmentListener {
        void addAnswer(String body);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof AddAnswerDialogFragmentListener){
            listener = (AddAnswerDialogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddAnswerDialogFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_answer, null);
        answer_body = view.findViewById(R.id.addAnswerEditText);
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add Answer")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Submit", (dialogInterface, i) -> {
                    listener.addAnswer(answer_body.getText().toString());
                }).create();
    }
}