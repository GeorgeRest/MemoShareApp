package com.george.memoshareapp.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.MyAdapter;
import com.george.memoshareapp.utils.ZoomOutPageTransformer;

public class AlbumActivity extends BaseActivity {
    private ViewPager mViewPager;
    private LinearLayout ll_layout;

    //向导界面的图片
    private int[] mPics = new int[]{R.mipmap.picture1, R.mipmap.picture2, R.mipmap.picture3, R.mipmap.picture4, R.mipmap.picture5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createdalbum);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_layout = (LinearLayout) findViewById(R.id.ll_layout);

        //设置适配器
        mViewPager.setAdapter(new MyAdapter(this, mPics));
        mViewPager.setPageMargin(20);
        mViewPager.setOffscreenPageLimit(mPics.length);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());//设置画廊模式

        //左右都有图
        mViewPager.setCurrentItem(0);

        //viewPager左右两边滑动无效的处理
        ll_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mViewPager.dispatchTouchEvent(motionEvent);
            }
        });
    }
}
