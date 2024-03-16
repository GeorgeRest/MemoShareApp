package com.george.memoshareapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.george.memoshareapp.Fragment.FriendFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.FriendListPagerAdapter;
import com.george.memoshareapp.manager.UserManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends BaseActivity implements View.OnClickListener {

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
    private FrameLayout searchLayout;

    private ImageView ivCsGlass;
    private TextView tvCsSearch;
    private TextInputEditText etSearch;
    private RelativeLayout rl_text_before_layout;
    private View rootLayout;

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

        Bundle bundle1 = new Bundle();
        bundle1.putInt("choice",0);
        bundle1.putString("phoneNumber",phoneNumber);
        bundle1.putBoolean("isMe",isMe);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("choice",1);
        bundle2.putString("phoneNumber",phoneNumber);
        bundle2.putBoolean("isMe",isMe);

        Bundle bundle3 = new Bundle();
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
//        sv_search = (SearchView) findViewById(R.id.sv_search);
        vp_friend = (ViewPager) findViewById(R.id.vp_friend);

        searchLayout = findViewById(R.id.contact_search_layout_friend);
        View customSearchView = LayoutInflater.from(this).inflate(R.layout.custom_search_view, searchLayout, false);
        ivCsGlass = customSearchView.findViewById(R.id.iv_cs_glass);
        tvCsSearch = customSearchView.findViewById(R.id.tv_cs_search);
        etSearch = customSearchView.findViewById(R.id.et_search);
        rl_text_before_layout = customSearchView.findViewById(R.id.rl_text_before_layout);
        searchLayout.addView(customSearchView);

        ivCsGlass.setVisibility(View.VISIBLE);
        tvCsSearch.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.GONE);

        rootLayout = findViewById(android.R.id.content);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootLayout.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight < screenHeight * 0.15) {
                    ivCsGlass.setVisibility(View.VISIBLE);
                    tvCsSearch.setVisibility(View.VISIBLE);
                    etSearch.setVisibility(View.GONE);
                }else {
                    ivCsGlass.setVisibility(View.GONE);
                    tvCsSearch.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCsGlass.setVisibility(View.GONE);
                tvCsSearch.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });



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