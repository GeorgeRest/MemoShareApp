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
import com.george.memoshareapp.utils.DateFormat;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

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
    private int shareNumber;
    private TextView detail_tv_share_number;
    private TextView detail_tv_like_number;
    private TextView detail_tv_comment_number;
    private boolean has_like;
    private SharedPreferences sharedPreferences1;
    private SharedPreferences.Editor editor1;
    private int likesCount;
    private boolean homePageAlreadyPressedLike=false;
    private boolean isliked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");

        sharedPreferences1 = getSharedPreferences("User", MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();

        int likesCount = sharedPreferences1.getInt(post.getId() + "_likes_count", 0);
        has_like = sharedPreferences1.getBoolean(post.getId() + ":" + sharedPreferences1.getString(post.getPhoneNumber(),""), false);

        detail_tv_like_number.setText(String.valueOf(likesCount));
        if (has_like) {
            like.setImageResource(R.mipmap.like_press);
            homePageAlreadyPressedLike=true;
        }else {
            homePageAlreadyPressedLike=false;
        }
        if (homePageAlreadyPressedLike){
            detail_tv_like_number.setText(String.valueOf(likesCount+1));
        }else{
            detail_tv_like_number.setText(String.valueOf(likesCount));
        }



        putParameter2View();//传参


    }



    private void putParameter2View() {

        userIcon.setImageResource(R.mipmap.touxiangceshi);
        userName.setText(post.getPhoneNumber().substring(0,5));
        publishTime.setText(DateFormat.getCurrentDateTime(post.getPublishedTime()));

        location.setText(post.getLocation());
        content.setText(post.getPublishedText());
        photoPath = post.getPhotoCachePath();



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

        displayManager.showPhoto(recyclerView,photoPath,DetailActivity.this);


    }


    private void init() {

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
                likesCount = sharedPreferences1.getInt(post.getId() + "_likes_count", 0);
                if (has_like) {
                    has_like = false;
                    like.setImageResource(R.mipmap.like);
                    likesCount--;
                    isliked = false;
                } else {
                    has_like = true;
                    like.setImageResource(R.mipmap.like_press);
                    likesCount++;
                    isliked = true;
                }
                editor1.putInt(post.getId() + "_likes_count", likesCount);
                editor1.putBoolean(post.getId() + ":" + sharedPreferences1.getString(post.getPhoneNumber(),""), has_like);
                editor1.apply();
                if (likesCount >10000){
                    double converted = Math.floor((double) likesCount / 10000 * 10) / 10;

                    detail_tv_like_number.setText(converted+"万");
                }
                detail_tv_like_number.setText(String.valueOf(likesCount));
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

    }


}