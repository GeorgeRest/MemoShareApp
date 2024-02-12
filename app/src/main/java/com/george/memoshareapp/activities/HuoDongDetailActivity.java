package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ViewPagerAdapter;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.interfaces.HuoDongImageClickListener;
import com.george.memoshareapp.interfaces.HuoDongImageDataListener;
import com.george.memoshareapp.manager.HuodongManager;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.ImageDownLoadTask;
import com.george.memoshareapp.utils.Vp2IndicatorView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HuoDongDetailActivity extends AppCompatActivity {

//    private String phoneNumber;
    private int activityId;
    private ViewPager2 viewpager2;
    private HuodongManager huodongManager;
    private List<String> imageNames;
    private ViewPagerAdapter viewPagerAdapter;
    private Vp2IndicatorView indicator_view;
    private InnerActivityBean activityInfo;

    private ImageView iv_avatar;
    private TextView tv_publisher;
    private TextView tv_publish_text;
    private TextView tv_publish_time;
    private TextView tv_publish_location;
    private RelativeLayout rl_information;
    private boolean isInformationVisible = true;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huo_dong_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        viewpager2 = (ViewPager2) findViewById(R.id.viewpager2);
        indicator_view = (Vp2IndicatorView) findViewById(R.id.indicator_view);
        initData();


        initView();

    }

    private void initView() {

        iv_avatar = findViewById(R.id.iv_avatar);
        tv_publisher = findViewById(R.id.tv_publisher);
        tv_publish_text = findViewById(R.id.tv_publish_text);
        tv_publish_time = findViewById(R.id.tv_publish_time);
        tv_publish_location = findViewById(R.id.tv_publish_location);
        rl_information = (RelativeLayout) findViewById(R.id.rl_information);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        if (activityInfo == null) {
            Toasty.error(this, "获取活动信息失败", Toasty.LENGTH_SHORT).show();
            finish();
        }
        if(null!=activityInfo.getHeadPortraitPath()){
            Glide.with(this).load(AppProperties.SERVER_MEDIA_URL + activityInfo
                    .getHeadPortraitPath()).into(iv_avatar);
        }else {
            Glide.with(this).load("https://lajiao.toodiancao.com/wp-content/uploads/2023/12/632.jpg").into(iv_avatar);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_publisher.setText(activityInfo.getName());

        if (activityInfo.getPublishText() != null) {
            tv_publish_text.setText(activityInfo.getPublishText());
        } else {
            tv_publish_text.setText("");
        }

        tv_publish_time.setText(activityInfo.getPublishedTime());

        tv_publish_location.setText(activityInfo.getLocation());

        rl_information.setVisibility(View.VISIBLE);

    }


    private void initData() {
        Intent intent = getIntent();
//        phoneNumber = intent.getStringExtra("phoneNumber");
        activityId = intent.getIntExtra("activityId", 0);
        activityInfo = (InnerActivityBean)intent.getSerializableExtra("activityInfo");

        imageNames = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(imageNames,this);
        viewpager2.setAdapter(viewPagerAdapter);
        huodongManager = new HuodongManager(this);
        huodongManager.getImagesByActivityId(activityId, new HuoDongImageDataListener() {
            @Override
            public void onImageLoadSuccess(List<String> imageNames) {
                if(imageNames.size() > 0){
                    viewPagerAdapter = new ViewPagerAdapter(imageNames,HuoDongDetailActivity.this);
                    viewpager2.setAdapter(viewPagerAdapter);

                    viewPagerAdapter.setHuoDongImageClickListener(new HuoDongImageClickListener() {
                        @Override
                        public void onImageClick() {
                            if(isInformationVisible){
                                rl_information.setVisibility(View.GONE);
                                rl_information.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,R.anim.anim_detail_photo_exit));
                                iv_back.setVisibility(View.GONE);
                                iv_back.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,R.anim.anim_detail_back_exit));
                                isInformationVisible = false;
                            }else {
                                rl_information.setVisibility(View.VISIBLE);
                                rl_information.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,R.anim.anim_detail_photo_enter));
                                iv_back.setVisibility(View.VISIBLE);
                                iv_back.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,R.anim.anim_detail_back_enter));
                                isInformationVisible = true;
                            }
                        }

                        @Override
                        public void onImageLongClick(String url) {
                            //TODO
                            // 弹窗下载
                            new XPopup.Builder(HuoDongDetailActivity.this).
                                    asBottomList("", new String[]{"保存图片","高德定位","微信分享","搜一搜："+activityInfo.getLocation()}, new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String text) {
                                            switch (position){
                                                case 0:

                                                    new ImageDownLoadTask(HuoDongDetailActivity.this).execute(url);
                                                    break;
                                                case 1:
                                                    new XPopup.Builder(HuoDongDetailActivity.this)
                                                            .asConfirm("使用高德地图定位", "确认跳转到高德地图定位？", "取消", "确定", new OnConfirmListener() {
                                                                        @Override
                                                                        public void onConfirm() {
                                                                            Uri uri = Uri.parse("amapuri://route/plan/?dlat=" + activityInfo.getLatitude() + "&dlon=" + activityInfo.getLongitude() + "&dname=" + activityInfo.getLocation() + "&dev=0&t=0");
                                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                            if (checkAppInstalled(HuoDongDetailActivity.this, "com.autonavi.minimap")) {
                                                                                startActivity(intent);
                                                                                Toasty.info(HuoDongDetailActivity.this, "高德地图正在启动").show();
                                                                            } else {
                                                                                Toasty.info(HuoDongDetailActivity.this, "高德地图没有安装").show();
                                                                                Intent i = new Intent();
                                                                                i.setData(Uri.parse("http://daohang.amap.com/index.php?id=201&CustomID=C021100013023"));
                                                                                i.setAction(Intent.ACTION_VIEW);
                                                                                startActivity(i);
                                                                            }
                                                                        }
                                                                    },
                                                                    new OnCancelListener() {
                                                                        @Override
                                                                        public void onCancel() {

                                                                        }
                                                                    },false).show();

                                                    break;
                                                case 2:
                                                    Toasty.info(HuoDongDetailActivity.this,"微信分享").show();
                                                    //
                                                    break;
                                                case 3:
                                                    Intent intent1 = new Intent(HuoDongDetailActivity.this, WebSearchActivity.class);
                                                    intent1.putExtra("searchcontent",activityInfo.getLocation());
                                                    startActivity(intent1);

                                                    break;

                                                default:
                                                    break;
                                            }
                                        }
                                    }).show();
                        }
                    });
                    viewPagerAdapter.notifyDataSetChanged();
                    viewpager2.setOffscreenPageLimit(imageNames.size());
                    viewpager2.setLayoutDirection(ViewPager2.LAYOUT_DIRECTION_LTR);
                    indicator_view.attachToViewPager2(viewpager2);
                }
            }

            @Override
            public void onImageLoadFailed() {
                Toasty.info(HuoDongDetailActivity.this,"加载失败，请退出重试", Toasty.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkAppInstalled(Context context,String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装
        }
    }
    
}

