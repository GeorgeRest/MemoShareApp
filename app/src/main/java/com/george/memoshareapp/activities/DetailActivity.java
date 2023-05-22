package com.george.memoshareapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.CommentAdapter;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.view.NoScrollListView;

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

    private EditText commentEdit;		    //评论输入框
    private NoScrollListView commentList;   //评论数据列表
    private LinearLayout bottomLinear;	    //底部分享、评论等线性布局
    private LinearLayout commentLinear;	    //评论输入框线性布局

    private int count;					    //记录评论ID
    private int position;				    //记录回复评论的索引
    private boolean isReply;			    //是否是回复
    private String commentText = "";		//记录对话框中的内容
    private CommentAdapter commentAdapter;
    private List<CommentBean> list;
    private ImageView submitComment;           //发送按钮
    private TextView set_comments_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();

        putParameter2View();//传参
        commentNumber = commentAdapter.getCount();
        detail_tv_share_number.setText(shareNumber+"");
        detail_tv_like_number.setText(like_number+"");
        detail_tv_comment_number.setText(commentNumber+"");
        set_comments_number.setText("共"+commentNumber+"条评论");

    }

    private void putParameter2View() {

//       userIcon.setImageResource((Integer) intent.getExtras().get("userIcon"));
        userIcon.setImageResource(R.mipmap.touxiangceshi);
        userName.setText(post.getPhoneNumber().substring(0,4));
        publishTime.setText(post.getPublishedTime());
        location.setText(post.getLocation());
        content.setText(post.getPublishedText());
        photoPath = post.getPhotoCachePath();

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
        submitComment = (ImageView) findViewById(R.id.submitComment);


        commentEdit = (EditText) findViewById(R.id.commentEdit);
        commentList = (NoScrollListView) findViewById(R.id.commentList);
        bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
        commentLinear = (LinearLayout) findViewById(R.id.commentLinear);
        set_comments_number = (TextView) findViewById(R.id.tv_comments_number);

        back.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        share.setOnClickListener(this);
        comment.setOnClickListener(this);
        like.setOnClickListener(this);
        submitComment.setOnClickListener(this);
        intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        commentAdapter = new CommentAdapter(this, getCommentData(),R.layout.comment_item,handler);
        commentList.setAdapter(commentAdapter);

//        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                isReply = true;
//                postion_reply = position;
//                commentLinear.setVisibility(View.VISIBLE);
//                bottomLinear.setVisibility(View.GONE);
//                onFocusChange(true);
//            }
//        });

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

            case R.id.detail_iv_comment:		//底部评论按钮
                isReply = false;
                commentLinear.setVisibility(View.VISIBLE);
                bottomLinear.setVisibility(View.GONE);
                onFocusChange(true);
                break;

            case R.id.submitComment:	//发表评论按钮
                if(isEditEmply()){		//判断用户是否输入内容
                    if(isReply){
                        replyComment();
                    }else{
                        publishComment();
                    }
                    bottomLinear.setVisibility(View.VISIBLE);
                    commentLinear.setVisibility(View.GONE);
                    onFocusChange(false);
                }
                break;

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


    /**
     * 获取评论列表数据
     */
    private List<CommentBean> getCommentData(){
        list = new ArrayList<CommentBean>();

        List<CommentBean> commentBeans = LitePal.where("post_id = ?", String.valueOf(post.getId())).find(CommentBean.class);
        for (CommentBean commentBean:commentBeans) {
            List<ReplyBean> replyBeans = LitePal.where("commentbean_id = ?", String.valueOf(commentBean.getId())).find(ReplyBean.class);
            commentBean.setReplyList(replyBeans);
            list.add(commentBean);
        }


//        CommentBean comment1 = new CommentBean();
//        comment1.setCommentUserPhoto(R.mipmap.photo_1);
//        comment1.setCommentUserName("Courtney Henry");
//        comment1.setCommentTime("11:30");
//        comment1.setCommentContent("在其中去之前作曲者庆中秋掌权");
//        comment1.setReplyList(getReplyData());
//        comment1.setPost(post);
//        post.getComments().add(comment1);
//        comment1.save();
//
//        post.save();
//        list.add(comment1);
//
////        // 你想要获取的Post对象的ID
////        long postId =post.getId();
////
////        // 加载Post对象及其所有的关联对象
////        Post post1 = LitePal.find(Post.class, postId, true);
//
////        post1.setComments(list);
//
//        CommentBean comment2 = new CommentBean();
//        comment2.setCommentUserPhoto(R.mipmap.photo_2);
//        comment2.setCommentUserName("Courtney Henry");
//        comment2.setCommentTime("11:40");
//        comment2.setCommentContent("在其中去之前作曲者庆中秋掌权在其中去之前作曲者庆中秋掌权 ");
//        comment2.setReplyList(getReplyData());
//        comment2.setPost(post);
//        comment2.save();
//        list.add(comment2);
//
//        CommentBean comment3 = new CommentBean();
//        comment3.setCommentUserPhoto(R.mipmap.photo_3);
//        comment3.setCommentUserName("Courtney Henry");
//        comment3.setCommentTime("12:30");
//        comment3.setCommentContent("在其中去之前作曲者庆中秋掌权 在其中去之前作曲者庆中秋掌权在其中去之前作曲者庆中秋掌权在其中去之前作曲者庆中秋掌权");
//        comment3.setReplyList(getReplyData());
//        comment3.setPost(post);
//        comment3.save();
//        list.add(comment3);
//
//        CommentBean comment4 = new CommentBean();
//        comment4.setCommentUserPhoto(R.mipmap.photo_4);
//        comment4.setCommentUserName("Courtney Henry");
//        comment4.setCommentTime("14:30");
//        comment4.setCommentContent("在其中去之前作曲者庆中秋掌权");
//        comment4.setReplyList(getReplyData());
//        comment4.setPost(post);
//        comment4.save();
//        list.add(comment4);
//
//        CommentBean comment5 = new CommentBean();
//        comment5.setCommentUserPhoto(R.mipmap.photo_5);
//        comment5.setCommentUserName("Courtney Henry");
//        comment5.setCommentTime("14:50");
//        comment5.setCommentContent("在其中去之前作曲者庆中秋掌权在其中去之前作曲者庆中秋掌权");
//        comment5.setReplyList(getReplyData());
//        comment5.setPost(post);
//        comment5.save();
//        list.add(comment5);
//
//
//        CommentBean comment6 = new CommentBean();
//        comment6.setCommentUserPhoto(R.mipmap.photo_6);
//        comment6.setCommentUserName("Courtney Henry");
//        comment6.setCommentTime("15:30");
//        comment6.setCommentContent("在其中去之前作曲者庆中秋掌权在其中去之前作曲者庆中秋掌权");
//        comment6.setReplyList(getReplyData());
//        comment6.setPost(post);
//        comment6.save();
//        list.add(comment6);
//
//        CommentBean comment7 = new CommentBean();
//        comment7.setCommentUserPhoto(R.mipmap.photo_7);
//        comment7.setCommentUserName("Courtney Henry");
//        comment7.setCommentTime("17:30");
//        comment7.setCommentContent("在其中去之前作曲者庆中秋掌权");
//        comment7.setReplyList(getReplyData());
//        comment7.setPost(post);
//        comment7.save();
//        list.add(comment7);

        return list;
    }

    /**
     * 获取回复列表数据
     */
    private List<ReplyBean> getReplyData(){
        List<ReplyBean> replyList = new ArrayList<ReplyBean>();
        return replyList;
    }


    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus){
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        commentEdit.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if(isFocus)  {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }else{
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(commentEdit.getWindowToken(),0);
                }
            }
        }, 100);
    }

    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply(){
        commentText = commentEdit.getText().toString().trim();
        if(commentText.equals("")){
            Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        commentEdit.setText("");
        return true;
    }

    /**
     * 发表评论
     */
    private void publishComment(){
        CommentBean bean = new CommentBean();
        bean.setCommentUserPhoto(R.mipmap.photo_10);
        bean.setCommentUserName("seven");
        bean.setCommentTime("13:30");
        bean.setCommentUserPhoneNumber("12345");
        bean.setCommentContent(commentText);
        bean.save();
        SQLiteDatabase db = LitePal.getDatabase();
        String sql = "UPDATE CommentBean SET post_id = ? WHERE id = ?";
        db.execSQL(sql, new Object[] {post.getId(), bean.getId()});
        list.add(bean);
        commentAdapter.notifyDataSetChanged();
        commentNumber++;
        detail_tv_comment_number.setText(String.valueOf(commentNumber));
        set_comments_number.setText("共"+commentNumber+"条评论");
    }

    /**
     * 回复评论
     */
    private void replyComment(){
        ReplyBean bean = new ReplyBean();
        bean.setReplyUserPhoto(R.mipmap.photo_10);
        bean.setReplyNickname("seven");
        bean.setReplyTime("11:10");
        bean.setCommentNickname(list.get(position).getCommentUserName());
        bean.setReplyContent(commentText);
        bean.save();
        SQLiteDatabase db = LitePal.getDatabase();
        String sql = "UPDATE ReplyBean SET commentbean_id = ? WHERE id = ?";
        db.execSQL(sql, new Object[] {list.get(position).getId(), bean.getId()});

        commentAdapter.getReplyComment(bean,position);
        commentAdapter.notifyDataSetChanged();

    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 10){
                isReply = true;
                position = (Integer)msg.obj;
                System.out.println("=================postion"+position);
                commentLinear.setVisibility(View.VISIBLE);
                bottomLinear.setVisibility(View.GONE);
                onFocusChange(true);
            }
        }
    };
    /**
     * 事件点击监听器
     */
//    private final class ClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.submitComment:	//发表评论按钮
//                    if(isEditEmply()){		//判断用户是否输入内容
//                        if(isReply){
//                            replyComment();
//                        }else{
//                            publishComment();
//                        }
//                        bottomLinear.setVisibility(View.VISIBLE);
//                        commentLinear.setVisibility(View.GONE);
//                        onFocusChange(false);
//                    }
//                    break;
//                case R.id.detail_iv_comment:		//底部评论按钮
//                    isReply = false;
//                    commentLinear.setVisibility(View.VISIBLE);
//                    bottomLinear.setVisibility(View.GONE);
//                    onFocusChange(true);
//                    break;
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //判断控件是否显示
        if(commentLinear.getVisibility() == View.VISIBLE){
            commentLinear.setVisibility(View.GONE);
            bottomLinear.setVisibility(View.VISIBLE);
        }
    }

}