package com.example.projectcurie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import org.jetbrains.annotations.NotNull;

/**
 * This class implements the fragment for adding a new question and seeing a list of existing questions.
 * List of existing questions is fetched from the firestore database.
 * Newly submitted questions are added to the firestore database.
 *
 * @author Bo Cen
 */
public class AddCommentFragment extends DialogFragment {

    public AddCommentFragment() { }
    private EditText comment_body;
    AddCommentDialogFragmentListener listener;

    public interface AddCommentDialogFragmentListener {
        void addComment(String body);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCommentDialogFragmentListener){
            listener = (AddCommentDialogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddCommentDialogFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_comment, null);
        comment_body = view.findViewById(R.id.addQuestionEditText);

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add Questions")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Submit", (dialogInterface, i) -> {
                    listener.addComment(comment_body.getText().toString());
                }).create();
    }
}