package com.example.projectcurie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
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
    private Button submit_button;
    private Button cancel_button;
    private String experimentTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_comment, container, false);

        submit_button = view.findViewById(R.id.post_btn);
        cancel_button = view.findViewById(R.id.cancel_btn);
        comment_body = view.findViewById(R.id.comment_body);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (getArguments() != null) {
                    Experiment bundle = (Experiment) getArguments().getSerializable("experiment");
                    experimentTitle = bundle.getTitle();
                } else {

                }*/
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ProjectCurie", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("Username", null);
                String input = comment_body.getText().toString();
                if (input.matches("")) {
                    getDialog().dismiss();
                    Snackbar empty_input_warning = Snackbar.make(getActivity().findViewById(R.id.comments_fragment), "Post Failed: Empty Question/Comment.", Snackbar.LENGTH_LONG);
                    empty_input_warning.show();
                    return;
                }
                if (username.matches("")) {
                    getDialog().dismiss();
                    Snackbar empty_input_warning = Snackbar.make(getActivity().findViewById(R.id.comments_fragment), "Post Failed: Unable to Fetch Username.", Snackbar.LENGTH_LONG);
                    empty_input_warning.show();
                    return;
                }
                Question question;
                question = new Question(input, username);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("experiments").document("Bitcoin Vendors").collection("comments")
                        .add(question);
                getDialog().dismiss();
            }
        });
        return view;
    }
}