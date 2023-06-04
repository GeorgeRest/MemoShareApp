package com.george.memoshareapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.UserPublishRecyclerAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.manager.DisplayManager;

import java.util.List;

public class TestUserPublishActivity extends AppCompatActivity {

    private List<Post> postList;
    private UserPublishRecyclerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_user_publish);
        postList = new DisplayManager().getPostList();
        adapter = new UserPublishRecyclerAdapter(this, postList);
        RecyclerView recyclerView = findViewById(R.id.test_publish_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
