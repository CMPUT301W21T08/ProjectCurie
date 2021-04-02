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

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This DialogFragment allows a user to enter new values for the editable fields of their profile.
 * @author Joshua Billson
 */
public class EditProfileDialogFragment extends DialogFragment {

    private UserProfileActivity listener;
    private EditText emailEditText;
    private EditText aboutEditText;
    private User user;

    /** Obligatory Empty Constructor */
    public EditProfileDialogFragment() {
    }

    /**
     * Use this for making new instances of EditUserDialogFragment. It binds the user whose profile
     * we want to edit to this fragment.
     * @param user
     *     The user whose details we want to edit.
     * @return
     *     A initialized EditUserDialogFragment.
     */
    public static EditProfileDialogFragment newInstance(User user) {
        EditProfileDialogFragment fragment = new EditProfileDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    /** Check that the activity who owns this fragment implements the EditUserCallbackListener interface. */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserProfileActivity){
            listener = (UserProfileActivity) context;
        } else {
            throw new RuntimeException("Must Be Attached To The UserProfileActivity Activity!");
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
        emailEditText = view.findViewById(R.id.editTextEmailAddress);
        aboutEditText = view.findViewById(R.id.editTextDescription);

        /* Set UI Fields */
        emailEditText.setText(user.getEmail());
        aboutEditText.setText(user.getAbout());

        /* Create The Alert Dialog & Set On Click Listeners */
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Edit Profile")
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
