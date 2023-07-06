package com.george.memoshareapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.CommentAdapter;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.events.updateLikeState;
import com.george.memoshareapp.interfaces.getLikeCountListener;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.DateFormat;
import com.george.memoshareapp.view.NoScrollListView;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {


    private int like_number;
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
    private List<ImageParameters> imageParameters;
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

    private EditText commentEdit;            //评论输入框
    private NoScrollListView commentList;   //评论数据列表
    private LinearLayout bottomLinear;        //底部分享、评论等线性布局
    private LinearLayout commentLinear;        //评论输入框线性布局

    private int position;				    //记录回复评论的索引
    private int commentPosition;             //记录回复子评论属于的评论索引
    private int replyPosition;                //记录回复子评论的索引
    private int submit_case = 0;			//评论，回复评论，回复子评论
    private int count;                        //记录评论ID
    private boolean isReply;                //是否是回复
    private String text = "";            //记录对话框中的内容
    private CommentAdapter commentAdapter;
    private List<CommentBean> list;
    private ImageView submitComment;           //发送按钮
    private TextView set_comments_number;
    private boolean has_like;
    private SharedPreferences sharedPreferences1;
    private SharedPreferences.Editor editor1;
    private long likesCount;
    private String phoneNumber;
    private ScrollView scrollView;

    private DisplayManager manager;
    private User postUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        sharedPreferences1 = getSharedPreferences("User", MODE_PRIVATE);
        phoneNumber = sharedPreferences1.getString("phoneNumber", "");
        editor1 = sharedPreferences1.edit();

//        likesCount = post.getLike();

        has_like = sharedPreferences1.getBoolean(this.post.getId() + ":" + phoneNumber, false);
        if (has_like) {
            like.setImageResource(R.mipmap.like_press);
        } else {
            like.setImageResource(R.mipmap.like);
        }
        putParameter2View();//传参
        commentNumber = commentAdapter.getCount();
        detail_tv_share_number.setText(shareNumber + "");
//        detail_tv_like_number.setText(likesCount + "");
        detail_tv_comment_number.setText(commentNumber + "");
        set_comments_number.setText("共" + commentNumber + "条评论");
        getLikeCount();
        //设置文本监听
        submitComment.setEnabled(false);
        submitComment.setImageResource(R.drawable.button_send_click);

        commentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When the text in the EditText changes, check if it's empty or not
                if(s.toString().trim().length()==0){
                    submitComment.setEnabled(false);
                    submitComment.setImageResource(R.drawable.button_send_click);
                } else {
                    submitComment.setEnabled(true);
                    submitComment.setImageResource(R.drawable.button_send);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    private void getLikeCount() {
        new DisplayManager().getLikeCount(post.getId(), new getLikeCountListener() {
            @Override
            public void onSuccess(int likeCount) {
                System.out.println("likeCount:" + likeCount);
                detail_tv_like_number.setText(String.valueOf(likeCount));
            }

        });
    }

    private void putParameter2View() {


        postUser = post.getUser();
        userName.setText(postUser.getName());
        if(postUser.getHeadPortraitPath()!=null){
            Glide.with(this).load(postUser.getHeadPortraitPath())
                    .thumbnail(Glide.with(this).load(R.drawable.photo_loading))
                    .error(R.drawable.ic_close)
                    .into(userIcon);
        }else{
            userIcon.setImageResource(R.mipmap.app_icon);
        }

        publishTime.setText(DateFormat.getCurrentDateTime(post.getPublishedTime()));

        location.setText(post.getLocation());
        content.setText(post.getPublishedText());
        imageParameters = post.getImageParameters();

        displayManager.showPhoto(recyclerView, imageParameters, DetailActivity.this);


    }


    private void init() {
        displayManager = new DisplayManager();
        imageParameters = new ArrayList<>();
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        userName = (TextView) findViewById(R.id.detail_tv_username);
        publishTime = (TextView) findViewById(R.id.detail_tv_publish_time);
        location = (TextView) findViewById(R.id.detail_tv_location);
        content = (TextView) findViewById(R.id.detail_tv_content);
        back = (ImageView) findViewById(R.id.detail_iv_back);
        userIcon = (ImageView) findViewById(R.id.detail_iv_user_icon);
        share = (ImageView) findViewById(R.id.detail_iv_share);
        comment = (ImageView) findViewById(R.id.detail_iv_comment);
        like = (ImageView) findViewById(R.id.detail_iv_like);
        detail_tv_share_number = (TextView) findViewById(R.id.detail_tv_share_number);
        detail_tv_like_number = (TextView) findViewById(R.id.detail_tv_like_number);
        detail_tv_comment_number = (TextView) findViewById(R.id.detail_tv_comment_number);
        recyclerView = findViewById(R.id.recycler_view);
        commentEdit = (EditText) findViewById(R.id.commentEdit);
        commentList = (NoScrollListView) findViewById(R.id.commentList);
        bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
        commentLinear = (LinearLayout) findViewById(R.id.commentLinear);
        set_comments_number = (TextView) findViewById(R.id.tv_comments_number);
        submitComment = (ImageView) findViewById(R.id.submitComment);
        back.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        share.setOnClickListener(this);
        comment.setOnClickListener(this);
        like.setOnClickListener(this);
        submitComment.setOnClickListener(this);
        intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        manager = new DisplayManager();
        commentAdapter = new CommentAdapter(this,getCommentData(),R.layout.item_comment,handler);
        commentList.setAdapter(commentAdapter);

        boolean shouldCheckComments = getIntent().getBooleanExtra("shouldCheckComments", false);
        if (shouldCheckComments) {
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
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_iv_back:
                finish();
                break;
            case R.id.detail_iv_user_icon:
                //进入个人主页
                Intent intent1 = new Intent(this, NewPersonPageActivity.class);

                intent1.putExtra("user", postUser);
                startActivity(intent1);
                break;
            case R.id.detail_iv_share:
                showBottomDialog();
                break;

            case R.id.detail_iv_comment:        //底部评论按钮
                submit_case = 0;
                commentLinear.setVisibility(View.VISIBLE);
                bottomLinear.setVisibility(View.GONE);
                onFocusChange(true);
                break;

            case R.id.submitComment:    //发表评论按钮
                if(isEditEmply()) {
                    switch (submit_case) {
                        case 0:
                            publishComment();
                            break;
                        case 10:
                            replyComment();
                            break;
                        case 1:
                            replySubComment();
                            break;
                    }
                    bottomLinear.setVisibility(View.VISIBLE);
                    commentLinear.setVisibility(View.GONE);
                    onFocusChange(false);
                }
                break;

            case R.id.detail_iv_like:
                new DisplayManager().updateLikeCount((int) post.getId(), phoneNumber, !has_like, new getLikeCountListener() {
                    @Override
                    public void onSuccess(int likeCount) {
                        if (has_like) {
                            like.setImageResource(R.mipmap.like);
                            has_like = false;
                            System.out.println("取消点赞");
                        } else {
                            like.setImageResource(R.mipmap.like_press);
                            has_like = true;
                        }

                        editor1.putBoolean(post.getId() + ":" + phoneNumber, has_like);
                        editor1.apply();
                        EventBus.getDefault().post(new updateLikeState(post.getId(), has_like));
                        if (likeCount > 10000) {
                            double converted = Math.floor((double) likeCount / 10000 * 10) / 10;
                            detail_tv_like_number.setText(converted + "万");
                        } else {
                            detail_tv_like_number.setText(String.valueOf(likeCount));
                        }
                    }

                });
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
                if (shareNumber > 10000) {
                    double converted = Math.floor((double) shareNumber / 10000 * 10) / 10;
                    detail_tv_share_number.setText(converted + "万");
                }
                detail_tv_share_number.setText(shareNumber + "");

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

    private void updateHomepageLikeState() {

    }




    /**
     * 获取评论列表数据
     */
    private List<CommentBean> getCommentData() {
        list = new ArrayList<CommentBean>();

        List<CommentBean> commentBeans = LitePal.where("post_id = ?", String.valueOf(post.getId())).find(CommentBean.class);
        for (CommentBean commentBean : commentBeans) {
            List<ReplyBean> replyBeans = LitePal.where("commentbean_id = ?", String.valueOf(commentBean.getId())).find(ReplyBean.class);
            commentBean.setReplyList(replyBeans);
            list.add(commentBean);
        }
        return list;
    }



    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        commentEdit.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply() {
        text = commentEdit.getText().toString().trim();
        if (text.equals("")) {
            Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        commentEdit.setText("");
        return true;
    }

    /**
     * 发表评论
     */
    private void publishComment() {
        CommentBean bean = new CommentBean();
        bean.setCommentUserPhoto(R.mipmap.touxiangceshi);
        bean.setCommentUserName("seven");
        bean.setCommentTime(null);
        bean.setCommentUserPhoneNumber("12345");
        bean.setCommentContent(text);
        bean.save();
        SQLiteDatabase db = LitePal.getDatabase();
        String sql = "UPDATE CommentBean SET post_id = ? WHERE id = ?";
        db.execSQL(sql, new Object[]{post.getId(), bean.getId()});
        list.add(bean);
        commentAdapter.notifyDataSetChanged();
        commentNumber++;
        detail_tv_comment_number.setText(String.valueOf(commentNumber));
        set_comments_number.setText("共" + commentNumber + "条评论");
    }

    /**
     * 回复评论
     */
    private void replyComment() {
        ReplyBean bean = new ReplyBean();
        bean.setReplyUserPhoto(R.mipmap.touxiangceshi);
        bean.setReplyNickname("Aven");
        bean.setReplyTime(null);
        bean.setCommentNickname(list.get(position).getCommentUserName());
        bean.setReplyContent(text);
        bean.save();
        SQLiteDatabase db = LitePal.getDatabase();
        String sql = "UPDATE ReplyBean SET commentbean_id = ? WHERE id = ?";
        db.execSQL(sql, new Object[]{list.get(position).getId(), bean.getId()});

        commentAdapter.getReplyComment(bean, position);
        commentAdapter.notifyDataSetChanged();

    }
    /**
     * 回复子评论
     */
    private void replySubComment() {
        List<ReplyBean> rList = list.get(commentPosition).getReplyList();
        ReplyBean bean = new ReplyBean();
        bean.setReplyUserPhoto(R.mipmap.touxiangceshi);
        bean.setReplyNickname("Seven");
        bean.setReplyTime(null);
        bean.setCommentNickname(rList.get(replyPosition).getReplyNickname());
        bean.setReplyContent(text);
        bean.save();
        SQLiteDatabase db = LitePal.getDatabase();
        String sql = "UPDATE ReplyBean SET commentbean_id = ? WHERE id = ?";
        db.execSQL(sql, new Object[] {list.get(commentPosition).getId(), bean.getId()});
        rList.add(rList.size(), bean);
        commentAdapter.notifyDataSetChanged();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10) {
                submit_case = 10;
                position = (Integer) msg.obj;
                commentLinear.setVisibility(View.VISIBLE);
                bottomLinear.setVisibility(View.GONE);
                onFocusChange(true);
            } else if (msg.what ==1) {
                submit_case = 1;
                replyPosition = msg.arg1;
                commentPosition = msg.arg2;
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
        if (commentLinear.getVisibility() == View.VISIBLE) {
            commentLinear.setVisibility(View.GONE);
            bottomLinear.setVisibility(View.VISIBLE);
        }
    }

}