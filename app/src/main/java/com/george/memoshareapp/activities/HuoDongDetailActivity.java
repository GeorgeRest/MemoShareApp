package com.george.memoshareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ViewPagerAdapter;
import com.george.memoshareapp.beans.Danmu;
import com.george.memoshareapp.beans.DanmuData;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.interfaces.DanmuUploadListener;
import com.george.memoshareapp.interfaces.HuoDongImageClickListener;
import com.george.memoshareapp.interfaces.HuoDongImageDataListener;
import com.george.memoshareapp.interfaces.HuodongLikeListener;
import com.george.memoshareapp.manager.DanmuLoadListener;
import com.george.memoshareapp.manager.HuodongManager;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.ImageDownLoadTask;
import com.george.memoshareapp.utils.Vp2IndicatorView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.orient.tea.barragephoto.adapter.BarrageAdapter;
import com.orient.tea.barragephoto.ui.BarrageView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HuoDongDetailActivity extends BaseActivity {

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
    private BarrageView barrageView;
    private BarrageAdapter<DanmuData> mAdapter;
    private String SEED[] = {"景色还不错啊", "小姐姐真好看～", "我也要去！带我去～", "门票多少啊？", "厉害啦！"};
    private String name[] = {"zx", "ds", "fe", "rg", "ny"};
    private boolean isInformationVisible = true;
    private ImageView iv_back;
    private ImageView iv_huodong_like;
    private TextView tv_danmu_switch;
    private TextView et_danmu_input;
    private boolean etShow = false;
    private RelativeLayout rl_danmu;
    private EditText et_danmu;
    private TextView tv_danmu_send;
    private TextView tv_publish_tag;
    private ConstraintLayout cl_et_zone;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huo_dong_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        phoneNumber = sp.getString("phoneNumber", "");

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
        tv_danmu_switch = (TextView) findViewById(R.id.tv_danmu_switch);
        tv_publish_location = findViewById(R.id.tv_publish_location);
        rl_information = (RelativeLayout) findViewById(R.id.rl_information);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_huodong_like = (ImageView) findViewById(R.id.iv_huodong_like);

        if (activityInfo == null) {
            Toasty.error(this, "获取活动信息失败", Toasty.LENGTH_SHORT).show();
            finish();
        }
        Log.d("zxactivityId", "initData: activityId = " + activityId);
        huodongManager.getLikeByActivityId(activityId,phoneNumber, new HuodongLikeListener() {
            @Override
            public void onLikeSuccess(boolean like) {
                Log.d("zxactivityId", "onLikeSuccess: like = " + like);
                iv_huodong_like.setImageResource(like ? R.drawable.like_press : R.drawable.like);
            }

            @Override
            public void onLikeFail(String error) {
                Toasty.info(HuoDongDetailActivity.this, "活动点赞信息获取失败").show();
                Log.d("zxhuodonglike", "onLikeFail: error : " + error);
            }
        });

        iv_huodong_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huodongManager.updateLikeState(activityId,phoneNumber, new HuodongLikeListener() {
                    @Override
                    public void onLikeSuccess(boolean like) {
                        iv_huodong_like.setImageResource(like ? R.drawable.like_press : R.drawable.like);
                    }

                    @Override
                    public void onLikeFail(String error) {
                        Toasty.info(HuoDongDetailActivity.this, "点赞状态更新失败").show();
                        Log.d("zxhuodonglike", "onLikeFail: error : " + error);
                    }
                });
            }
        });





        if(null!=activityInfo.getHeadPortraitPath()){
            Glide.with(this).load(AppProperties.SERVER_MEDIA_URL + activityInfo
                    .getHeadPortraitPath()).placeholder(R.drawable.huodong_pic_default).into(iv_avatar);
        }else {
            Glide.with(this).load("https://lajiao.toodiancao.com/wp-content/uploads/2023/12/632.jpg").placeholder(R.drawable.huodong_pic_default).into(iv_avatar);
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

        tv_publish_tag = (TextView) findViewById(R.id.tv_publish_tag);

        if(activityInfo.getTag() != null && activityInfo.getTag().length() > 0){
            tv_publish_tag.setText("#" + activityInfo.getTag());
        } else {
            tv_publish_tag.setText("#无标签");
        }

        tv_publish_location.setText(activityInfo.getLocation());

        rl_information.setVisibility(View.VISIBLE);

        barrageView = findViewById(R.id.barrage_view);
        BarrageView.Options options = new BarrageView.Options()
                .setGravity(BarrageView.GRAVITY_TOP) // 设置弹幕的位置
                .setInterval(50) // 设置弹幕的发送间隔
                .setSpeed(200,29) // 设置速度和波动值
                .setModel(BarrageView.MODEL_COLLISION_DETECTION)     // 设置弹幕模式 随机生成or碰撞检测
                .setClick(false); // 设置弹幕是否可以点击
        barrageView.setOptions(options);

        barrageView.setAdapter(mAdapter = new BarrageAdapter<DanmuData>(null,this) {
            @Override
            protected BarrageViewHolder<DanmuData> onCreateViewHolder(View root, int type) {
                return new ViewHolder(root);
            }

            @Override
            public int getItemLayout(DanmuData danmuData) {
                return R.layout.item_danmu;
            }
        });

        tv_danmu_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                huodongManager.getDanmuList(activityId, new DanmuLoadListener() {
                    @Override
                    public void onDanmuListLoadSuccess(List<Danmu> danmuList) {
                        Log.d("zxLoadDanmu", "onDanmuListLoadSuccess: danmuList.size() = " +danmuList.size() );
                        List<DanmuData> dataList = new ArrayList<>();
                        for (int i = 0; i < SEED.length; i++) {
                            DanmuData danmuData = new DanmuData(SEED[i], name[i]);
                            dataList.add(danmuData);
                        }
                        for (Danmu danmu:danmuList) {
                            Log.d("zxDanmuLog", "onDanmuListLoadSuccess: danmu_content : " + danmu.getDanmu_content() );
                            DanmuData danmuData = new DanmuData(danmu.getDanmu_content(),danmu.getName());
                            dataList.add(danmuData);
                        }
                        mAdapter.addList(dataList);
                    }

                    @Override
                    public void onDanmuListLoadFail(String error) {
                        Toasty.info(HuoDongDetailActivity.this,"弹幕加载失败").show();
                        Log.d("zxLoadDanmu", "onDanmuListLoadFail: error: " + error);
                    }
                });
            }
        });

        et_danmu_input = (TextView) findViewById(R.id.et_danmu_input);

        rl_danmu = (RelativeLayout) findViewById(R.id.rl_danmu);
        cl_et_zone = (ConstraintLayout) findViewById(R.id.cl_et_zone);
        et_danmu_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etShow = etShow ? false : true;

                if(etShow){
                    et_danmu_input.setHint("取消输入");
                    cl_et_zone.setVisibility(View.VISIBLE);
                } else {
                    et_danmu_input.setHint("输入弹幕");
                    cl_et_zone.setVisibility(View.GONE);
                }

            }
        });

        et_danmu = (EditText) findViewById(R.id.et_danmu);

        tv_danmu_send = (TextView) findViewById(R.id.tv_danmu_send);

        tv_danmu_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_danmu.getText().toString().trim();
                if(text.length()<=0){
                    Toasty.info(HuoDongDetailActivity.this,"请输入文字内容").show();
                    et_danmu.setText("");
                }else if(text.length()>15){
                    Toasty.info(HuoDongDetailActivity.this,"请输入15字以内的文字内容").show();
                }else{
                    huodongManager.uploadDanmu(activityId, text, phoneNumber, new DanmuUploadListener() {
                        @Override
                        public void danmuUploadSuccess(String content) {
                            Log.d("zxTdanmu", "danmuUploadSuccess: content = " + content);
                            mAdapter.add(new DanmuData(content,"用户"));
                        }

                        @Override
                        public void danmuUploadFail(String error) {
                            Log.d("zxTdanmu", "danmuUploadFail:  " + error);
                        }
                    });
                    et_danmu.setText("");
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                                rl_information.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,
                                        R.anim.anim_detail_photo_exit));
                                iv_back.setVisibility(View.GONE);
                                iv_back.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,
                                        R.anim.anim_detail_back_exit));
                                iv_huodong_like.setVisibility(View.GONE);
                                iv_huodong_like.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,
                                        R.anim.anim_detail_back_exit));
                                isInformationVisible = false;
                            }else {
                                rl_information.setVisibility(View.VISIBLE);
                                rl_information.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,R.anim.anim_detail_photo_enter));
                                iv_back.setVisibility(View.VISIBLE);
                                iv_back.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,R.anim.anim_detail_back_enter));
                                iv_huodong_like.setVisibility(View.VISIBLE);
                                iv_huodong_like.setAnimation(AnimationUtils.loadAnimation(HuoDongDetailActivity.this,R.anim.anim_detail_back_enter));
                                isInformationVisible = true;
                            }
                        }

                        @Override
                        public void onImageLongClick(String url) {
                            //TODO
                            // 弹窗下载
                            new XPopup.Builder(HuoDongDetailActivity.this).
                                    asBottomList("", new String[]{"保存图片","高德定位","搜一搜："+activityInfo.getLocation()}, new OnSelectListener() {
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

    class ViewHolder extends BarrageAdapter.BarrageViewHolder<DanmuData> {

        private TextView mName;
        private TextView mContent;

        ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_danmu_name);
            mContent = itemView.findViewById(R.id.tv_danmu_content);
        }

        @Override
        protected void onBind(DanmuData data) {
            mName.setText(data.getUserName() + "：");
            mContent.setText(data.getContent());
        }
    }
    
}

