package com.george.memoshareapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.GalleryAdapter;
import com.george.memoshareapp.adapters.OutHuodongViewPagerAdapter;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.events.HuoDongReleaseEvent;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.HuodongDataListener;
import com.george.memoshareapp.interfaces.OuterHuodongClickListener;
import com.george.memoshareapp.manager.HuodongManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HuodongGalleryActivity extends BaseActivity implements HuodongDataListener<List<InnerActivityBean>> {


    private GalleryAdapter galleryAdapter;
    private ViewPager2 view_pager_activity;

    private List<InnerActivityBean> innerActivityBeans = new ArrayList<>();
    private HuodongManager huodongManager;

    private boolean isNoMore;
    private int huodongPageNum = 1;
    private ImageView btn_add_activity;
    private String phoneNumber;
    private int followId;
    private int currentPosition;
    private ImageView ivActivityImage;
    private float DownX, DownY = 0;
    private float moveX, moveY = 0;
    private View view_click;
    private OutHuodongViewPagerAdapter outHuodongViewPagerAdapter;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback;
    private TextView tv_publish_time;
    private boolean timeFirstShow = true;
    private InnerActivityBean firstHuoDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong_gallery);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Intent intent = getIntent();
        firstHuoDong = (InnerActivityBean) intent.getSerializableExtra("firstHuoDong");
        phoneNumber = intent.getStringExtra("phoneNumber");
        followId = firstHuoDong.getActivityId();
        Log.d("zxtest", " initData: followId = " + followId);
        
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

//        initTestData();

        initData();

        initView();

    }

    private void initData() {
        huodongManager = new HuodongManager(this);
        timeFirstShow = true;
        isNoMore = false;
        //TODO
        //给Inner赋值
        innerActivityBeans = new ArrayList<>();
        innerActivityBeans.add(firstHuoDong);

        //测试
//        innerActivityBeans.addAll(list);

        galleryAdapter = new GalleryAdapter(innerActivityBeans,this);
        outHuodongViewPagerAdapter = new OutHuodongViewPagerAdapter(this,innerActivityBeans);
//        正式
        huodongPageNum = 1;
        huodongManager.getInnerHuoDongListByPage(followId,huodongPageNum,8,galleryAdapter.getCount()-1,this);



    }

    private void initView() {

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        view_pager_activity = (ViewPager2) findViewById(R.id.iv_activity_image);
//        iv_activity_image.setImageResource(images[0]);//测试

        btn_add_activity = (ImageView) findViewById(R.id.btn_add_activity);

        tv_publish_time = (TextView) findViewById(R.id.tv_publish_time);

        view_pager_activity.setAdapter(outHuodongViewPagerAdapter);

        outHuodongViewPagerAdapter.setOuterHuodongClickListener(new OuterHuodongClickListener() {
            @Override
            public void onAvatarClick(String userId) {

            }

            @Override
            public void onHuodongClick(InnerActivityBean bean) {
                Intent intent = new Intent(HuodongGalleryActivity.this, HuoDongDetailActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("activityId", innerActivityBeans.get(currentPosition).getActivityId());
                intent.putExtra("activityInfo", innerActivityBeans.get(currentPosition));
                startActivity(intent);
            }
        });


        Gallery gallery = (Gallery) findViewById(R.id.gallery_activitys);

        gallery.setAdapter(galleryAdapter);

        gallery.setClickable(true);

        gallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case KeyEvent.ACTION_UP:
                        Animation animation = AnimationUtils.loadAnimation(HuodongGalleryActivity.this, R.anim.time_out_alpha);
                        tv_publish_time.startAnimation(animation);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                tv_publish_time.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        break;
                }
                return false;
            }
        });

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                galleryAdapter.setSelectItem(position);
//                iv_activity_image.setImageResource(images[position]);
                int currentItem = view_pager_activity.getCurrentItem();
                if(currentItem!=position){
                    view_pager_activity.setCurrentItem(position);
                }

                String publishedTime = innerActivityBeans.get(position).getPublishedTime();

                if(!timeFirstShow){
                    tv_publish_time.setText(publishedTime);
                    tv_publish_time.setVisibility(View.VISIBLE);
                } else {
                    timeFirstShow = false;
                }

                if(innerActivityBeans.size()>0&&innerActivityBeans.get(position)!=null){

//                    正式
                    //获取下一页数据
                    if(position != 0 && position == innerActivityBeans.size()-1 && !isNoMore){
                        //TODO
                        //获取下一页数据
                        huodongPageNum++;
                        huodongManager.getInnerHuoDongListByPage(followId, huodongPageNum, 8, galleryAdapter.getCount()-1, new HuodongDataListener<List<InnerActivityBean>>() {
                            @Override
                            public void onLoadSuccess(HttpListData<InnerActivityBean> data, String type) {
                                List<InnerActivityBean> newList = data.getItems();
                                if(newList.size() != 0) {
                                    innerActivityBeans.addAll(newList);
                                    galleryAdapter.notifyDataSetChanged();
                                    outHuodongViewPagerAdapter.notifyDataSetChanged();
//                                    galleryAdapter.setSelectItem(position);
                                }
                                isNoMore = data.isLastPage();
                                if(isNoMore){
                                    Toasty.info(HuodongGalleryActivity.this, "没有更多数据了", Toast.LENGTH_SHORT, true).show();
                                }
                            }

                            @Override
                            public void onLoadError(String errorMessage) {

                            }
                        });

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toasty.info(HuodongGalleryActivity.this, "出错了", Toast.LENGTH_SHORT, true).show();
            }
        });

        onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int selectedItemPosition = gallery.getSelectedItemPosition();
                if (selectedItemPosition != position) {
                    gallery.setSelection(position);
                }
            }
        };
        view_pager_activity.registerOnPageChangeCallback(onPageChangeCallback);


        btn_add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuodongGalleryActivity.this, AddHuoDongActivity.class);
                intent.putExtra("phoneNumber",phoneNumber);
                intent.putExtra("isFollowing",true);
                intent.putExtra("followId",followId);
                startActivity(intent);
            }
        });

//        iv_activity_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HuodongGalleryActivity.this, HuoDongDetailActivity.class);
//                intent.putExtra("phoneNumber",phoneNumber);
//                intent.putExtra("activityId",innerActivityBeans.get(currentPosition).getActivityId());
//                startActivity(intent);
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        view_pager_activity.unregisterOnPageChangeCallback(onPageChangeCallback);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //TODO 回来的时候让 isNoMore = false

    @Override
    public void onLoadSuccess(HttpListData<InnerActivityBean> data, String type) {
        List<InnerActivityBean> newList = data.getItems();
        if(newList.size() == 0){
            //用底部弹窗
            Toasty.info(this, "没有更多数据了", Toast.LENGTH_SHORT, true).show();
        }else {
            innerActivityBeans.addAll(newList);
            galleryAdapter.notifyDataSetChanged();
            outHuodongViewPagerAdapter.notifyDataSetChanged();
            view_pager_activity.setCurrentItem(0);
        }
        isNoMore = data.isLastPage();
    }

    @Override
    public void onLoadError(String errorMessage) {
        Toasty.info(this, "出错了", Toast.LENGTH_SHORT, true).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHuoDongReleaseEvent(HuoDongReleaseEvent event) {
        if(event.getHuoDongEvent()){
            initData();
            initView();
        }
    }

}