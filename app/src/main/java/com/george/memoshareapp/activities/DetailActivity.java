package com.george.memoshareapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.Fragment.CommentFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.CommentAdapter;
import com.george.memoshareapp.beans.Comment;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.manager.DisplayManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

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
    private int commentNumber = 0;
    private DisplayManager displayManager;
    private boolean IS_LIKE = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int shareNumber;
    private TextView detail_tv_share_number;
    private TextView detail_tv_like_number;
    private TextView detail_tv_comment_number;
    private List<String> ceShiList;

    private RecyclerView rv_comment;
    private CommentAdapter commentAdapter;
    private List<Comment> comments;
    private TextView tv_comments_number;
    private String TAG = "DetailActivity---------------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //commentNumber = commentAdapter.getItemCount();
        init();
        intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        putParameter2View();//传参
        detail_tv_share_number.setText(shareNumber+"");
        detail_tv_like_number.setText(like_number+"");
        detail_tv_comment_number.setText(commentNumber+"");

        comments = getComments();
        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, comments);
        rv_comment.setAdapter(commentAdapter);
       // tv_comments_number.setText("共"+commentNumber+"条评论");

    }



    private void putParameter2View() {

//       userIcon.setImageResource((Integer) intent.getExtras().get("userIcon"));
        userIcon.setImageResource(R.mipmap.touxiangceshi);
        userName.setText(post.getPhoneNumber().substring(0,4));
        publishTime.setText(post.getPublishedTime());
        location.setText(post.getLocation());
        content.setText(post.getPublishedText());
        photoPath = post.getPhotoCachePath();


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

        displayManager.showPhoto(recyclerView,photoPath,DetailActivity.this);


    }


    private void init() {
        sharedPreferences = getSharedPreferences("commentNumberAndLikeNumber", DetailActivity.MODE_PRIVATE);

        shareNumber = sharedPreferences.getInt("shareNumber", 0);
    //    commentNumber = sharedPreferences.getInt("commentNumber", 0);
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
        detail_tv_share_number = (TextView) findViewById(R.id.detail_tv_share_number);
        detail_tv_like_number = (TextView) findViewById(R.id.detail_tv_like_number);
        detail_tv_comment_number = (TextView) findViewById(R.id.detail_tv_comment_number);
        recyclerView = findViewById(R.id.recycler_view);
        back.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        share.setOnClickListener(this);
        comment.setOnClickListener(this);
        like.setOnClickListener(this);

        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        tv_comments_number = (TextView) findViewById(R.id.tv_comments_number);

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
                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommentFragment commentFragment = CommentFragment.newInstance(-1);   // 假设 -1 表示新的评论，而不是对现有评论的回复
                        commentFragment.show(getSupportFragmentManager(), "CommentFragment");
                    }
                });
//                commentNumber++;
//                if (commentNumber>10000){
//                    double converted = Math.floor((double) commentNumber / 10000 * 10) / 10;
//
//                    detail_tv_like_number.setText(converted+"万");
//                    tv_comments_number.setText("共"+converted+"万条评论");
//                }else {
//                    tv_comments_number.setText("共" + commentNumber + "条评论");
//                    detail_tv_comment_number.setText(commentNumber + "");
//                }

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
    //    editor.putInt("commentNumber",commentNumber);
        editor.putInt("likeNumber", like_number);
        editor.apply();

    }

    private List<Comment> getComments() {
        comments = new ArrayList<>();

        //LitePal.findAll(Post.class,true,)
        //创建1条评论
        Comment comment1 = new Comment();
        comment1.setCommentUserName("Courtney Henry");
        comment1.setCommentContent("在其中去之前作曲者庆中秋掌权  2小时前");
        comment1.setCommentUserPhoto(R.mipmap.photo_1);
        //comment.setDate(new Date());
        comments.add(comment1);

        comment1.setPost(post);
        post.setComments(comments);
        post.save();
        comment1.save();


        //创建1条评论
        Comment comment2 = new Comment();
        comment2.setCommentUserName("Bessie Cooper");
        comment2.setCommentContent("在其中去之前作曲者庆中秋掌权  2小时前");
        comment2.setCommentUserPhoto(R.mipmap.photo_2);
        //comment.setDate(new Date());
        comments.add(comment2);

        comment2.setPost(post);
        post.setComments(comments);
        post.save();
        comment2.save();

        //创建1条评论
        Comment comment3 = new Comment();
        comment3.setCommentUserName("Ronald Richards");
        comment3.setCommentContent("在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者  2小时前");
        comment3.setCommentUserPhoto(R.mipmap.photo_3);
        //comment.setDate(new Date());
        //创建对应的子评论
        List<Comment> subComments3 = new ArrayList<>();
        Comment subComment = new Comment();
        subComment.setCommentUserName("Jacob Jones");
        subComment.setCommentContent("在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者  2小时前");
        subComment.setCommentUserPhoto(R.mipmap.photo_4);
        //subComment.setDate(new Date());
        subComments3.add(subComment);

        comment3.setSubComments(subComments3);
        comments.add(comment3);

        comment3.setPost(post);
        post.setComments(comments);
        post.save();
        comment3.save();

        //创建1条评论
        Comment comment4 = new Comment();
        comment4.setCommentUserName("Floyd Miles");
        comment4.setCommentContent("在其中去之前作曲者庆中秋掌权  2小时前");
        comment4.setCommentUserPhoto(R.mipmap.photo_5);
        //comment.setDate(new Date());
        //创建对应的子评论
        List<Comment> subComments4 = new ArrayList<>();
        Comment subComment1 = new Comment();
        subComment1.setCommentUserName("Ronald Richards");
        subComment1.setCommentContent("在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者  2小时前");
        subComment1.setCommentUserPhoto(R.mipmap.photo_6);
        //subComment.setDate(new Date());
        subComments4.add(subComment1);

        Comment subComment2 = new Comment();
        subComment2.setCommentUserName("Jacob Jones");
        subComment2.setCommentContent("回复Ronald Richards: 在其中去之前作曲者庆中秋掌权者在其中去  2小时前");
        subComment2.setCommentUserPhoto(R.mipmap.photo_7);
        //subComment.setDate(new Date());
        subComments4.add(subComment2);
        comment4.setSubComments(subComments4);
        comments.add(comment4);

        comment4.setPost(post);
        post.setComments(comments);
        post.save();
        comment4.save();

        //创建1条评论
        Comment comment5 = new Comment();
        comment5.setCommentUserName("Darlene Robertson");
        comment5.setCommentContent("在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者在其中去  昨天 12:30");
        comment5.setCommentUserPhoto(R.mipmap.photo_8);
        //comment.setDate(new Date());
        comments.add(comment5);

        comment5.setPost(post);
        post.setComments(comments);
        post.save();
        comment5.save();

        //创建1条评论
        Comment comment6 = new Comment();
        comment6.setCommentUserName("Ronald Richards");
        comment6.setCommentContent("在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者  05/27 13:39");
        comment6.setCommentUserPhoto(R.mipmap.photo_9);
        //comment.setDate(new Date());
        comments.add(comment6);

        comment6.setPost(post);
        post.setComments(comments);
        post.save();
        comment6.save();

        return comments;
    }

    public void postComment() {
        int comment_id = intent.getIntExtra("id", -1);
        String commentText = intent.getStringExtra("commentText");
        Log.d(TAG, "postComment: "+commentText);
        if (comment_id == -1) {
            // 创建一条新的评论

            Comment comment = new Comment();
            comment.setCommentContent(commentText);
            //comment.setCommentTime();
            comment.setCommentUserName("seven");
            comment.setCommentUserPhoto(R.mipmap.photo_10);
            comments.add(comment);

            comment.setPost(post);
    //        post.save();
            comment.save();

        } else {
            // 回复评论，创建子评论
            Comment comment1 = LitePal.where("id = ?", String.valueOf(comment_id)).findFirst(Comment.class);
            List<Comment> subCommentsList = new ArrayList<>();
            Comment subComment = new Comment();
            subComment.setCommentUserName("seven");
            subComment.setCommentContent(commentText);
            subComment.setCommentUserPhoto(R.mipmap.photo_10);
            //subComment.setDate(new Date());
            subComment.save();
            subCommentsList.add(subComment);
            comment1.setSubComments(subCommentsList);
            comment1.setPost(post);
            post.save();
            comment1.save();
        }
    }


    public void onCommentPosted() {
        int post_id = (int)post.getId();
        Post post1 = LitePal.where("id = ?", String.valueOf(post_id)).findFirst(Post.class,true);
        List<Comment> newcomments = post1.getComments();
        // 然后使用新的数据更新 Adapter
        commentAdapter.setComments(newcomments);
        rv_comment.setAdapter(commentAdapter);
    }
}