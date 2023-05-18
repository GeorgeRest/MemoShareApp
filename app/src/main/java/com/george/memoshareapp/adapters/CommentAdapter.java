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

import com.george.memoshareapp.CustomLinearLayoutManager;
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
        holder.tv_comment_username.setText(comment.getCommentUserName());
        holder.iv_comment_userphoto.setImageResource(comment.getCommentUserPhoto());
        holder.tv_comment_content.setText(comment.getCommentContent());



        List<Comment> subComments = comment.getSubComments();
        if (subComments != null && !subComments.isEmpty()) {
            // 设置子评论的 RecyclerView
            CommentAdapter subCommentAdapter = new CommentAdapter(context, subComments);
            holder.rv_sub_comments.setLayoutManager(new CustomLinearLayoutManager(holder.itemView.getContext()));
            holder.rv_sub_comments.setAdapter(subCommentAdapter);
            subCommentAdapter.setComments(subComments);

            holder.rv_sub_comments.setVisibility(View.VISIBLE);  // 如果有子评论，就显示 RecyclerView
        }else{
            holder.rv_sub_comments.setVisibility(View.GONE);  // 如果没有子评论，就隐藏 RecyclerView
        }
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();    // 当评论列表改变时，通知 RecyclerView 更新界面
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

            this.rv_sub_comments.setVisibility(View.GONE);  // 默认设置为不可见
        }
    }
}
