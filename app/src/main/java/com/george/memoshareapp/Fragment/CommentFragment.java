package com.george.memoshareapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Comment;


public class CommentFragment extends DialogFragment {


    private EditText et_comment_input;
    private Button btn_submit_comment;
    private OnCommentPostedListener mListener;

    public interface OnCommentPostedListener {
        void onCommentPosted();
    }

    public void setOnCommentPostedListener(OnCommentPostedListener listener) {
        mListener = listener;
    }

    public static CommentFragment newInstance(int commentId) {
        CommentFragment fragment = new CommentFragment();

        // Pass commentId as an argument
        Bundle args = new Bundle();
        args.putInt("comment_id", commentId);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_comment_input = (EditText) view.findViewById(R.id.et_comment_input);
        btn_submit_comment = (Button) view.findViewById(R.id.btn_submit_comment);

        btn_submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = et_comment_input.getText().toString();
                if (!commentText.isEmpty()) {
                    // This assumes you have a method to post the comment to your server
                    postComment(getArguments().getInt("comment_id", -1), commentText);  //如果找不到id就返回-1,即新建一个评论
                }
            }
        });
    }


    private void postComment(int commentId, String commentText) {
        if (commentId == -1) {
            // This is a new comment, not a reply to an existing comment
            // TODO: Post the new comment to your server
            Comment comment = new Comment();
            comment.setCommentContent(commentText);
    //        comment.setCommentTime();
            comment.setCommentUserName("seven");
            comment.setCommentUserPhoto(R.mipmap.photo_10);

            //todo 把post传到fragment，然后将comment保存到post
            // 当评论发布成功后，调用 mListener 的 onCommentPosted 方法

        } else {
            // This is a reply to an existing comment
            // TODO: Post the reply to your server
        }
        if (mListener != null) {
            mListener.onCommentPosted();
        }
    }

}