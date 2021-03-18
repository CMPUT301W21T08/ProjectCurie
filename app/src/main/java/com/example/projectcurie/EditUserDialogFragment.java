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

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This DialogFragment allows a user to enter new values for the editable fields of their profile.
 * @author Joshua Billson
 */
public class EditUserDialogFragment extends DialogFragment {

    private EditUserCallbackListener listener;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText aboutEditText;
    private User user;

    /**
     * Callback interface for refreshing the UI of the user profile activity upon editing the
     * user's information
     */
    public interface EditUserCallbackListener {
        public void refreshProfile();
    }

    /** Obligatory Empty Constructor */
    public EditUserDialogFragment() {
    }

    /**
     * Use this for making new instances of EditUserDialogFragment. It binds the user whose profile
     * we want to edit to this fragment.
     * @param user
     *     The user whose details we want to edit.
     * @return
     *     A initialized EditUserDialogFragment.
     */
    public static EditUserDialogFragment newInstance(User user) {
        EditUserDialogFragment fragment = new EditUserDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    /** Check that the activity who owns this fragment implements the EditUserCallbackListener interface. */
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

        /* Grab Widgets */
        usernameEditText = view.findViewById(R.id.editTextPersonName);
        emailEditText = view.findViewById(R.id.editTextEmailAddress);
        aboutEditText = view.findViewById(R.id.editTextDescription);

        /* Set UI Fields */
        usernameEditText.setText(user.getUsername());
        emailEditText.setText(user.getEmail());
        aboutEditText.setText(user.getAbout());

        /* Create The Alert Dialog & Set On Click Listeners */
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
                            });

                })
                .create();
    }
}