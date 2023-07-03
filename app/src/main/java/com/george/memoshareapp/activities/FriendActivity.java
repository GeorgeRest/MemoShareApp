package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.george.memoshareapp.Fragment.FriendFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.FriendListPagerAdapter;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private LinearLayout ll_mine;
    private TextView tv_myfollowing;
    private TextView tv_myfans;
    private TextView tv_myfriend;
    private LinearLayout ll_friend;
    private TextView tv_following;
    private TextView tv_fans;
    private ViewPager vp_friend;
    private List<Fragment> fragmentList;
    private SearchView sv_search;
    private FriendListPagerAdapter pagerAdapter;
    private boolean isMe = false;
    private int choice = 1;
    private String phoneNumber;
    private FriendFragment fragment1;
    private FriendFragment fragment2;
    private FriendFragment fragment3;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private UserManager userManager;
    private UserServiceApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initView();
        initData();
        pagerAdapter = new FriendListPagerAdapter(manager,fragmentList);
        vp_friend.setAdapter(pagerAdapter);
        onViewPagerSelected(choice);
        vp_friend.setCurrentItem(choice);
        vp_friend.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                onViewPagerSelected(position);
                FriendFragment fragment = (FriendFragment) pagerAdapter.getItem(position);
                fragment.getPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void onViewPagerSelected(int position) {
        clearSelection();
        switch (position){
            case 0:
                tv_following.setTextColor(getResources().getColor(R.color.black));
                tv_myfollowing.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                tv_fans.setTextColor(getResources().getColor(R.color.black));
                tv_myfans.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                tv_myfriend.setTextColor(getResources().getColor(R.color.black));
                break;
            default:
                break;
        }
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Intent intent = getIntent();
        // 获取传递的参数
        phoneNumber = intent.getStringExtra("postPhoneNumber");
        choice = intent.getIntExtra("isFriend", 0);
        isMe = intent.getBooleanExtra("ismyself", false);

//        userManager = new UserManager(this);
//        List<User> followedUserList = userManager.getFollowedUser(phoneNumber);
//        List<User> fansUserList = userManager.getFansUser(phoneNumber);
//        List<User> friendUserList = userManager.getFriendUser(phoneNumber);

//        apiService = RetrofitManager.getInstance().create(UserServiceApi.class);
//        Call<HttpListData<User>> followedUserCall = apiService.getFollowedUser(phoneNumber);
//        Call<HttpListData<User>> fansUserCall = apiService.getFansUser(phoneNumber);
//        Call<HttpListData<User>> friendUserCall = apiService.getFriendUser(phoneNumber);
//
//        followedUserCall.enqueue(new Callback<HttpListData<User>>() {
//            @Override
//            public void onResponse(Call<HttpListData<User>> call, Response<HttpListData<User>> response) {
//                HttpListData<User> data = response.body();
//                List<User> items = data.getItems();
//            }
//
//            @Override
//            public void onFailure(Call<HttpListData<User>> call, Throwable t) {
//
//            }
//        });
//
        Bundle bundle1 = new Bundle();
        //bundle1.putSerializable("userList", (Serializable) followedUserList);
        bundle1.putInt("choice",0);
        bundle1.putString("phoneNumber",phoneNumber);
        bundle1.putBoolean("isMe",isMe);

        Bundle bundle2 = new Bundle();
        //bundle2.putSerializable("userList", (Serializable) fansUserList);
        bundle2.putInt("choice",1);
        bundle2.putString("phoneNumber",phoneNumber);
        bundle2.putBoolean("isMe",isMe);

        Bundle bundle3 = new Bundle();
        //bundle3.putSerializable("userList", (Serializable) friendUserList);
        bundle3.putInt("choice",2);
        bundle3.putString("phoneNumber",phoneNumber);
        bundle3.putBoolean("isMe",isMe);


        fragment1 = FriendFragment.newInstance();
        fragment1.setArguments(bundle1);

        fragment2 = FriendFragment.newInstance();
        fragment2.setArguments(bundle2);

        fragment3 = FriendFragment.newInstance();
        fragment3.setArguments(bundle3);


        if (isMe == true) {
            ll_mine.setVisibility(View.VISIBLE);
            ll_friend.setVisibility(View.GONE);
            fragmentList.add(fragment1);
            fragmentList.add(fragment2);
            fragmentList.add(fragment3);
        }else{
            ll_mine.setVisibility(View.GONE);
            ll_friend.setVisibility(View.VISIBLE);
            fragmentList.add(fragment1);
            fragmentList.add(fragment2);
        }
    }


    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);
        tv_myfollowing = (TextView) findViewById(R.id.tv_myfollowing);
        tv_myfans = (TextView) findViewById(R.id.tv_myfans);
        tv_myfriend = (TextView) findViewById(R.id.tv_myfriend);
        ll_friend = (LinearLayout) findViewById(R.id.ll_friend);
        tv_following = (TextView) findViewById(R.id.tv_following);
        tv_fans = (TextView) findViewById(R.id.tv_fans);
        sv_search = (SearchView) findViewById(R.id.sv_search);
        vp_friend = (ViewPager) findViewById(R.id.vp_friend);
        iv_back.setOnClickListener(this);
        tv_myfollowing.setOnClickListener(this);
        tv_myfans.setOnClickListener(this);
        tv_myfriend.setOnClickListener(this);
        tv_following.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_following:

            case R.id.tv_myfollowing:
                vp_friend.setCurrentItem(0);
                break;
            case R.id.tv_fans:

            case R.id.tv_myfans:
                vp_friend.setCurrentItem(1);
                break;
            case R.id.tv_myfriend:
                vp_friend.setCurrentItem(2);
                break;
        }

    }
    private void clearSelection() {
        tv_myfollowing.setTextColor(getResources().getColor(R.color.friend_normal));
        tv_myfans.setTextColor(getResources().getColor(R.color.friend_normal));
        tv_myfriend.setTextColor(getResources().getColor(R.color.friend_normal));
        tv_following.setTextColor(getResources().getColor(R.color.friend_normal));
        tv_fans.setTextColor(getResources().getColor(R.color.friend_normal));
    }
}