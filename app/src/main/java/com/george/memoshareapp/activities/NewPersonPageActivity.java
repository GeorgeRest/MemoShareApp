package com.george.memoshareapp.activities;

import android.os.Bundle;

import com.george.memoshareapp.Fragment.NewPersonPageFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.User;

public class NewPersonPageActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person_page);

        // 获取传递过来的参数
        User user = (User) getIntent().getSerializableExtra("user");
        Post newPost = (Post) getIntent().getSerializableExtra("newpost");



// 创建 Fragment
        NewPersonPageFragment newPersonPageFragment = new NewPersonPageFragment();
        if(newPost!=null){
            newPersonPageFragment = newPersonPageFragment.newInstance(user, newPost.getPhoneNumber());
        }else {
            newPersonPageFragment = newPersonPageFragment.newInstance(user);
        }



        // 注意这里的变化

        // 托管 Fragment
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragment_container, newPersonPageFragment)
            .commit();



    }
}
