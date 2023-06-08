package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.george.memoshareapp.Fragment.FriendFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.FriendListPagerAdapter;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isMine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initView();
        initData();

        pagerAdapter = new FriendListPagerAdapter(getSupportFragmentManager(),fragmentList);
        vp_friend.setAdapter(pagerAdapter);
        vp_friend.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(FriendActivity.this, "1", Toast.LENGTH_SHORT).show();

                onViewPagerSelected(position);
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

        FriendFragment fragment1 = FriendFragment.newInstance("关注","");
        FriendFragment fragment2 = FriendFragment.newInstance("粉丝","");
        FriendFragment fragment3 = FriendFragment.newInstance("好友","");

        if (isMine == true) {
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_myfollowing:
                vp_friend.setCurrentItem(0);

                break;
            case R.id.tv_myfans:
                vp_friend.setCurrentItem(1);

                break;
            case R.id.tv_myfriend:
                vp_friend.setCurrentItem(2);

                break;
            case R.id.tv_following:
                vp_friend.setCurrentItem(0);

                break;
            case R.id.tv_fans:
                vp_friend.setCurrentItem(1);
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