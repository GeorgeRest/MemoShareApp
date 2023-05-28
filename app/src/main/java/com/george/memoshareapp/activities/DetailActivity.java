package com.george.memoshareapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.CommentAdapter;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.events.LastClickedPositionEvent;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.DateFormat;
import com.george.memoshareapp.view.NoScrollListView;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

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
    private int shareNumber;
    private TextView detail_tv_share_number;
    private TextView detail_tv_like_number;
    private TextView detail_tv_comment_number;
    private boolean has_like;
    private SharedPreferences sharedPreferences1;
    private SharedPreferences.Editor editor1;
    private long likesCount;
    private boolean homePageAlreadyPressedLike=false;
    private String phoneNumber;


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
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();




        sharedPreferences1 = getSharedPreferences("User", MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();

         likesCount = post.getLike();
        phoneNumber = sharedPreferences1.getString("phoneNumber", "");
        has_like = sharedPreferences1.getBoolean(post.getId() + ":" + phoneNumber, false);
        detail_tv_like_number.setText(String.valueOf(likesCount));
        if (has_like) {
            like.setImageResource(R.mipmap.like_press);
            detail_tv_like_number.setText(String.valueOf(likesCount));
        } else {
            like.setImageResource(R.mipmap.like);
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

        displayManager.showPhoto(recyclerView,photoPath,DetailActivity.this);

    }


    private void init() {
        intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
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

        if (list.size() < 9) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        } else {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 1200);
                }
            });
        }

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
                if (has_like) {
                    like.setImageResource(R.mipmap.like);
                    likesCount--;
                    has_like = false;
                } else {
                    like.setImageResource(R.mipmap.like_press);
                    likesCount++;
                    has_like = true;
                }
                editor1.putBoolean(post.getId() + ":" + phoneNumber, has_like);
                editor1.apply();
                update();
                if (likesCount >10000){
                    double converted = Math.floor((double) likesCount / 10000 * 10) / 10;

                    detail_tv_like_number.setText(converted+"万");
                }else {
                    detail_tv_like_number.setText(String.valueOf(likesCount));
                }

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





    private void update() {
        int totalCount = LitePal.count(Post.class);
        int id = totalCount - (int) post.getId();
        EventBus.getDefault().post(new LastClickedPositionEvent(id));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        post.setLike(likesCount);
        post.update(post.getId());
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