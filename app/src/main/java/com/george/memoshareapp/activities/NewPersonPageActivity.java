package com.george.memoshareapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.Fragment.NewPersonPageFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.User;

public class NewPersonPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person_page);

        // 获取传递过来的参数
        User user = (User) getIntent().getSerializableExtra("user");
        Post newPost = (Post) getIntent().getSerializableExtra("newpost");



// 创建 Fragment
        NewPersonPageFragment newPersonPageFragment = new NewPersonPageFragment();
        newPersonPageFragment = newPersonPageFragment.newInstance(user, newPost);  // 注意这里的变化

        // 托管 Fragment
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragment_container, newPersonPageFragment)
            .commit();



    }
}
