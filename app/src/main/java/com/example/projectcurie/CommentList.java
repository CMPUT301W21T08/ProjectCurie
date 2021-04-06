package com.example.projectcurie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
/**
 * This class is an ArrayAdapter meant to contain both the comment body and author of a comment.
 * An onclicklistener is set to listen to when a user clicks on the author name which uses the
 * FetchUserCommand to pull up the author profile.
 * Base code taken from Abdul Ali Bangash, "Lab 3", 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/mod/resource/view.php?id=4829655
 */
public class CommentList extends ArrayAdapter<Comment> {
    public ArrayList<Comment> comments;
    private final Context context;

    public CommentList(Context context, ArrayList<Comment> comments) {
        super(context,0, comments);
        this.comments = comments;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.commentlist,parent,false);
        }

        /*Body and author are assigned text.*/
        Comment comment = comments.get(position);
        TextView comment_item = view.findViewById(R.id.comment);
        TextView btn = view.findViewById(R.id.profile_btn);
        comment_item.setText(comment.getBody());
        btn.setText(comment.getPoster());

        /**
         * Listener to check if an author name is pressed. Activates UserProfileActivity on click.
         * Toast sent to the user if profile does not exist.
         */
        btn.setOnClickListener(v -> {
            FetchUserCommand fetchUserCommand = new FetchUserCommand(comment.getPoster());
            fetchUserCommand.addCallback(() -> {
                User user = fetchUserCommand.fetchData();
                if (user != null) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra("user", user);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "User Profile Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }).run();
        });

        return view;
    }
}
