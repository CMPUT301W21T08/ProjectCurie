package com.example.projectcurie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

        return view;
    }
}
