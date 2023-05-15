package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.CommentAdapter;
import com.george.memoshareapp.beans.Comment;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView rv_comment;
    private List<Comment> commentlist;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        initData();
        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, commentlist);
        rv_comment.setAdapter(commentAdapter);

        loadComments();
    }

    private void loadComments() {
        // 获取评论列表，并设置到 Adapter 中
    }

    private void initData() {
        commentlist = new ArrayList<>();

    }

    private void initView() {
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
    }
}