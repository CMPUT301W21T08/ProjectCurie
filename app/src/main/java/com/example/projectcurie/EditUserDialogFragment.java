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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This fragment present the user with a number of text fields to update their profile.
 * @author Joshua Billson
 */
public class EditUserDialogFragment extends DialogFragment {

    private EditUserCallbackListener listener;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText aboutEditText;
    private User user;

    public interface EditUserCallbackListener {
        public void refreshProfile();
    }

    public EditUserDialogFragment() {
    }

    public static EditUserDialogFragment newInstance(User user) {
        EditUserDialogFragment fragment = new EditUserDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditUserCallbackListener){
            listener = (EditUserCallbackListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement EditUserCallbackListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user = (User) getArguments().getSerializable("user");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_user_dialog, null);
        usernameEditText = view.findViewById(R.id.editTextPersonName);
        emailEditText = view.findViewById(R.id.editTextEmailAddress);
        aboutEditText = view.findViewById(R.id.editTextDescription);

        usernameEditText.setText(user.getUsername());
        emailEditText.setText(user.getEmail());
        aboutEditText.setText(user.getAbout());

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Edit User Profile")
                .setNegativeButton("BACK", null)
                .setPositiveButton("SUBMIT", (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    user.setAbout(aboutEditText.getText().toString());
                    user.setEmail(emailEditText.getText().toString());
                    db.collection("users").document(user.getUsername())
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                App.setUser(user);
                                listener.refreshProfile();
                                Toast.makeText(getActivity().getApplicationContext(), "Profile Successfully Updated!", Toast.LENGTH_SHORT);
                            });

                })
                .create();
    }
}