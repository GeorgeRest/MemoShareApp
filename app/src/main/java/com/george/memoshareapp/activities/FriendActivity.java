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
    private TextView tv_following;
    private TextView tv_fans;
    private TextView tv_friend;
    private SearchView sv_search;
    private ViewPager vp_friend;
    private List<Fragment> fragmentList;
    private FriendListPagerAdapter pagerAdapter;

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
                break;
            case 1:
                tv_fans.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                tv_friend.setTextColor(getResources().getColor(R.color.black));
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

        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
    }


    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_following = (TextView) findViewById(R.id.tv_following);
        tv_fans = (TextView) findViewById(R.id.tv_fans);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        sv_search = (SearchView) findViewById(R.id.sv_search);
        vp_friend = (ViewPager) findViewById(R.id.vp_friend);
        iv_back.setOnClickListener(this);
        tv_following.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_friend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_following:
                vp_friend.setCurrentItem(0);

                break;
            case R.id.tv_fans:
                vp_friend.setCurrentItem(1);

                break;
            case R.id.tv_friend:
                vp_friend.setCurrentItem(2);

                break;
        }

    }
    private void clearSelection() {
        tv_following.setTextColor(getResources().getColor(R.color.friend_normal));
        tv_fans.setTextColor(getResources().getColor(R.color.friend_normal));
        tv_friend.setTextColor(getResources().getColor(R.color.friend_normal));
    }
}