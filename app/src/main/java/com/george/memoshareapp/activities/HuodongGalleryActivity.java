package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.GalleryAdapter;
import com.george.memoshareapp.adapters.OutHuodongViewPagerAdapter;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.HuodongDataListener;
import com.george.memoshareapp.interfaces.OuterHuodongClickListener;
import com.george.memoshareapp.manager.HuodongManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HuodongGalleryActivity extends AppCompatActivity implements HuodongDataListener<List<InnerActivityBean>> {


    private GalleryAdapter galleryAdapter;
    private ViewPager2 view_pager_activity;

    private List<InnerActivityBean> innerActivityBeans = new ArrayList<>();
//    private int[] images = {R.mipmap.gallery_test_1, R.mipmap.gallery_test_2, R.mipmap.gallery_test_3,
//            R.mipmap.gallery_test_4, R.mipmap.gallery_test_5, R.mipmap.gallery_test_6, R.mipmap.gallery_test_7,
//            R.mipmap.gallery_test_8, R.mipmap.gallery_test_9, R.mipmap.gallery_test_10};//测试
    private HuodongManager huodongManager;

    private boolean isNoMore;
    private int huodongPageNum = 1;

    //测试数据

//    private List<InnerActivityBean> list = new ArrayList<>();
    private FloatingActionButton btn_add_activity;
    private String phoneNumber;
    private int followId;
    private int currentPosition;
    private ImageView ivActivityImage;
    private float DownX, DownY = 0;
    private float moveX, moveY = 0;
    private View view_click;
    private OutHuodongViewPagerAdapter outHuodongViewPagerAdapter;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong_gallery);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

//        initTestData();

        initData();

        initView();

    }

//    private void initTestData() {
//
//        //测试数据
//        InnerActivityBean innerActivity1 = new InnerActivityBean("小明",
//                "https://tse2-mm.cn.bing.net/th/id/OIP-C.GC_ugX-TzPVR26SSxI1kZwHaE9?rs=1&pid=ImgDetMain",
//                "https://img.zcool.cn/community/018e865e16a3a1a8012165180a1435.jpg@1280w_1l_2o_100sh.jpg",
//                "这里风景真好", 1, "中国北京", "2023/12/01 08:09");
//        InnerActivityBean innerActivity2 = new InnerActivityBean("小红",
//                "https://x0.ifengimg.com/cmpp/fck/2019_43/abc5ff29b234b2f_w3888_h2592.jpg",
//                "https://ts1.cn.mm.bing.net/th/id/R-C.7659533d96bc656db1f7cfd981c0b37d?rik=ObQr6n9fPQzvMQ&riu=http%3a%2f%2fpic.bizhi360.com%2fbbpic%2f18%2f5518.jpg&ehk=WSzl3qUFCFdDbOoamYgnbQuCj5UoxXV7SvZ62UHXOhk%3d&risl=&pid=ImgRaw&r=0",
//                "真是个美好的地方", 2, "中国上海", "2023/12/02 09:15");
//        InnerActivityBean innerActivity3 = new InnerActivityBean("张三",
//                "https://ts1.cn.mm.bing.net/th/id/R-C.61e10abb31be7153915f6550ad077542?rik=oukmhTAyezTNDw&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2014%2f12%2f22%2f289_093449_8ced3.jpg&ehk=mci1AffXHcwklYpn7cpFWSvZz3evIeDYflmll9wiy44%3d&risl=&pid=ImgRaw&r=0",
//                "https://clubimg.club.vmall.com/data/attachment/forum/202006/03/210216pnzsu3qfkaq09pv2.jpg",
//                "欢迎大家来参加", 3, "中国广州", "2023/12/03 10:22");
//        InnerActivityBean innerActivity4 = new InnerActivityBean("李四",
//                "https://boot-img.xuexi.cn/image/1006/process/b5e3a02d382649ff8565323f48320b36.jpg",
//                "https://pic1.zhimg.com/v2-e915686432f3bb45dd43705ec445352d_r.jpg",
//                "美丽的风景尽在眼前", 4, "中国成都", "2023/12/04 11:30");
//        InnerActivityBean innerActivity5 = new InnerActivityBean("王五",
//                "https://img.zcool.cn/community/01a15855439bdf0000019ae9299cce.jpg@1280w_1l_2o_100sh.jpg",
//                "https://ts1.cn.mm.bing.net/th/id/R-C.541d315de4b11e79e0bcb84f35ccf30b?rik=V96LdUGfUZJu5g&riu=http%3a%2f%2fimage.qianye88.com%2fpic%2f812f74e3a664e1c2b1e0e8cde0480e8c&ehk=o55Cw49jjhzmX%2bO%2fvYRXQsoMo3R%2f%2fuXTqpx%2bM88E63M%3d&risl=&pid=ImgRaw&r=0",
//                "大连的美景等你发现", 5, "中国大连", "2023/12/05 12:45");
//        list.add(innerActivity1);
//        list.add(innerActivity2);
//        list.add(innerActivity3);
//        list.add(innerActivity4);
//        list.add(innerActivity5);
//    }

    private void initData() {
        huodongManager = new HuodongManager(this);
        //TODO
        //给Inner赋值
        Intent intent = getIntent();
        InnerActivityBean firstHuoDong = (InnerActivityBean) intent.getSerializableExtra("firstHuoDong");
        phoneNumber = intent.getStringExtra("phoneNumber");
        followId = firstHuoDong.getActivityId();
        Log.d("zxtest", " initData: followId = " + followId);

        innerActivityBeans.add(firstHuoDong);

        //测试
//        innerActivityBeans.addAll(list);

        galleryAdapter = new GalleryAdapter(innerActivityBeans,this);
        outHuodongViewPagerAdapter = new OutHuodongViewPagerAdapter(this,innerActivityBeans);
//        正式
        huodongManager.getInnerHuoDongListByPage(followId,1,8,galleryAdapter.getCount(),this);



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

        btn_add_activity = (FloatingActionButton) findViewById(R.id.btn_add_activity);


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

                if(innerActivityBeans.size()>0&&innerActivityBeans.get(position)!=null){

//                    正式
                    //获取下一页数据
                    if(position != 0 && position == innerActivityBeans.size()-1 && !isNoMore){
                        //TODO
                        //获取下一页数据
                        huodongPageNum++;
                        huodongManager.getInnerHuoDongListByPage(innerActivityBeans.get(position).getActivityId(), huodongPageNum, 8, galleryAdapter.getCount(), new HuodongDataListener<List<InnerActivityBean>>() {
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

}