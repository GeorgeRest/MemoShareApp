package com.george.memoshareapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.manager.DisplayManager;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private  int like_number;
    private ImageView userIcon;
    private TextView userName;
    private TextView publishTime;
    private TextView location;
    private TextView content;
    private ImageView back;
    private ImageView share;
    private ImageView comment;
    private ImageView like;
    private Intent intent;
    private Post post;
    private List<String> photoPath;
    private RecyclerView recyclerView;
    private List<String> commentList = new ArrayList<>();
    private TextView comment_count;
    private int commentNumber;
    private DisplayManager displayManager;
    private boolean IS_LIKE = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int shareNumber;
    private TextView detail_tv_share_number;
    private TextView detail_tv_like_number;
    private TextView detail_tv_comment_number;
    private List<String> ceShiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
//        intent = getIntent();
//        post = (Post) intent.getSerializableExtra("key");/////////改key
//
        putParameter2View();//传参
        detail_tv_share_number.setText(shareNumber+"");
        detail_tv_like_number.setText(like_number+"");
        detail_tv_comment_number.setText(commentNumber+"");

    }

    private void initData() {
        ceShiList = new ArrayList<>();
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQzMjk=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQzMjg=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQzMzA=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQ0ODU=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQ0ODQ=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQ0ODM=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQwMzE=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzQwMzA=");
        ceShiList.add("/storage/emulated/0/Android/data/com.george.memoshareapp/files/Pictures/photoCache/Y29udGVudDovL2NvbS5hbmRyb2lkLnByb3ZpZGVycy5tZWRpYS5kb2N1bWVudHMvZG9jdW1lbnQvaW1hZ2UlM0EyMzYzODI=");

        userIcon.setImageResource(R.mipmap.touxiangceshi);
        userName.setText("9476");
        publishTime.setText("2023-5-18");
        location.setText("大连市甘井子区");
        content.setText("这是一条测试数据");
//        photoPath = ceShiList;


    }


    private void putParameter2View() {
        initData();
//       userIcon.setImageResource((Integer) intent.getExtras().get("userIcon"));
//        userName.setText(post.getPhoneNumber().substring(0,4));
//        publishTime.setText(post.getPublishedTime());
//        location.setText(post.getLocation());
//        content.setText(post.getPublishedText());
//        photoPath = (List<String>)intent.getExtras().get("photoPathCache");


//        if (intent.getExtras().get("like").equals("true")){
//            like.setImageResource(R.mipmap.like_press);
//            IS_LIKE =true;
//            like_number++;
//            if (like_number>10000){
//                double converted = Math.floor((double) like_number / 10000 * 10) / 10;
//
//                detail_tv_like_number.setText(converted+"万");
//            }
//            detail_tv_like_number.setText(like_number+"");
//        }
        //如果传进来的评论不为空，那么就把评论加进来
//        if (intent.getExtras().get("comment")!=null){
//            commentList = (List<String>) intent.getExtras().get("comment");
//            commentNumber = commentList.size();
//            if (commentNumber>10000){
//                double converted = Math.floor((double) commentNumber / 10000 * 10) / 10;
//
//                comment_count.setText(converted+"万");
//            }
//            comment_count.setText(commentNumber);
//        }

        displayManager.showPhoto(recyclerView,ceShiList,DetailActivity.this);


    }


    private void init() {
        sharedPreferences = getSharedPreferences("commentNumberAndLikeNumber", DetailActivity.MODE_PRIVATE);

        shareNumber = sharedPreferences.getInt("shareNumber", 0);
        commentNumber = sharedPreferences.getInt("commentNumber", 0);
        like_number = sharedPreferences.getInt("likeNumber", 0);


        editor = sharedPreferences.edit();
        displayManager = new DisplayManager();
        photoPath = new ArrayList<>();

        userName = (TextView) findViewById(R.id.detail_tv_username);
        publishTime = (TextView) findViewById(R.id.detail_tv_publish_time);
        location = (TextView) findViewById(R.id.detail_tv_location);
        content = (TextView) findViewById(R.id.detail_tv_content);
        back = (ImageView) findViewById(R.id.detail_iv_back);
        userIcon = (ImageView)findViewById(R.id.detail_iv_user_icon);
        share = (ImageView) findViewById(R.id.detail_iv_share);
        comment = (ImageView) findViewById(R.id.detail_iv_comment);
        like = (ImageView) findViewById(R.id.detail_iv_like);
        comment_count = (TextView) findViewById(R.id.detail_tv_comment_count);
        detail_tv_share_number = (TextView) findViewById(R.id.detail_tv_share_number);
        detail_tv_like_number = (TextView) findViewById(R.id.detail_tv_like_number);
        detail_tv_comment_number = (TextView) findViewById(R.id.detail_tv_comment_number);
        recyclerView = findViewById(R.id.recycler_view);
        back.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        share.setOnClickListener(this);
        comment.setOnClickListener(this);
        like.setOnClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_iv_back:
                finish();
                break;
            case R.id.detail_iv_user_icon:
                //进入个人主页
                break;
            case R.id.detail_iv_share:
                showBottomDialog();
                break;
            case R.id.detail_iv_comment:
                //点击评论，弹出评论框
                //将评论的用户名，评论内容，本机用户头像传入，显示在评论框中
                //点击评论框中的发送按钮，将评论内容，评论时间，用户名，本机用户头像传入，显示在评论列表中
                commentNumber++;
                if (commentNumber>10000){
                    double converted = Math.floor((double) commentNumber / 10000 * 10) / 10;

                    detail_tv_like_number.setText(converted+"万");
                }
                detail_tv_comment_number.setText(commentNumber+"");


                break;
//          case R.id.list中的某条评论:
//                点击评论，弹出评论框，回复某条评论，
//                将评论的用户名，评论内容，本机用户头像传入，显示在评论框中
//                点击评论框中的发送按钮，将评论内容，评论时间，用户名，本机用户头像传入，显示在评论列表中
//                评论数量+1
//
//                break;
            case R.id.detail_iv_like:
                if (IS_LIKE ==true){
                    like.setImageResource(R.mipmap.like);
                    IS_LIKE = false;
                    like_number--;
                    if (like_number>10000){
                        double converted = Math.floor((double) like_number / 10000 * 10) / 10;

                        detail_tv_like_number.setText(converted+"万");
                    }
                    detail_tv_like_number.setText(like_number+"");
                    break;
                }
                like.setImageResource(R.mipmap.like_press);
                IS_LIKE = true;
                like_number++;
                if (like_number>10000){
                    double converted = Math.floor((double) like_number / 10000 * 10) / 10;

                    detail_tv_like_number.setText(converted+"万");
                }
                detail_tv_like_number.setText(like_number+"");
                break;
            default:
                break;
        }

    }
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.activity_detail_share, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();//获取dialog的window对象
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.detail_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到联系人中
                //跳转到联系人界面，最上面有最近的联系人，下面是所有联系人
                //将链接发送到联系人中
                shareNumber++;
                if (shareNumber>10000){
                    double converted = Math.floor((double) shareNumber / 10000 * 10) / 10;
                    detail_tv_share_number.setText(converted+"万");
                }
                detail_tv_share_number.setText(shareNumber+"");

                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.detail_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.detail_tv_share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putInt("shareNumber",shareNumber);
        editor.putInt("commentNumber",commentNumber);
        editor.putInt("likeNumber", like_number);
        editor.apply();

    }
}