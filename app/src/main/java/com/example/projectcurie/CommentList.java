package com.example.projectcurie;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class CommentList extends ArrayAdapter<Comment> {
    public ArrayList<Comment> comments;
    private Context context;

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

        Comment comment = comments.get(position);
        TextView comment_item = view.findViewById(R.id.comment);
        TextView btn = view.findViewById(R.id.profile_btn);
        comment_item.setText(comment.getBody());
        btn.setText(comment.getPoster());

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
