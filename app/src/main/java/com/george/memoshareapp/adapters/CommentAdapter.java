package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments = new ArrayList<>();
    private Context context;
    private CommentAdapter commentAdapter;


    public CommentAdapter() {
    }

    public CommentAdapter(Context context, List<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
    //    holder.tv_comment_username
    //    holder.iv_comment_userphoto
        holder.tv_comment_content.setText(comment.getCommentContent());
        holder.rv_sub_comments.setAdapter(commentAdapter);
        // 设置子评论的 RecyclerView
        CommentAdapter subCommentAdapter = new CommentAdapter();
        holder.rv_sub_comments.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.subComments.setAdapter(subCommentAdapter);
        subCommentAdapter.setComments(comment.getSubComments());
    }



    class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_comment_userphoto;
        TextView tv_comment_username;
        TextView tv_comment_content;
        RecyclerView rv_sub_comments;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_comment_userphoto = itemView.findViewById(R.id.iv_comment_userphoto);
            this.tv_comment_username = itemView.findViewById(R.id.tv_comment_username);
            this.tv_comment_content = itemView.findViewById(R.id.tv_comment_content);
            this.rv_sub_comments = itemView.findViewById(R.id.rv_sub_comments);

        }
    }
}
