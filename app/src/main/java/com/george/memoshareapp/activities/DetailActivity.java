package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.CommentAdapter;
import com.george.memoshareapp.beans.Comment;
import com.george.memoshareapp.beans.Post;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView rv_comment;
    private CommentAdapter commentAdapter;
    private List<Comment> comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        comments = getComments();
        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, comments);
        rv_comment.setAdapter(commentAdapter);
    }

    private void loadComments() {
        // 获取评论列表，并设置到 Adapter 中
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

        //创建1条评论
        Comment comment2 = new Comment();
        comment2.setCommentUserName("Bessie Cooper");
        comment2.setCommentContent("在其中去之前作曲者庆中秋掌权  2小时前");
        comment2.setCommentUserPhoto(R.mipmap.photo_2);
        //comment.setDate(new Date());
        comments.add(comment2);

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

        //创建1条评论
        Comment comment5 = new Comment();
        comment5.setCommentUserName("Darlene Robertson");
        comment5.setCommentContent("在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者在其中去  昨天 12:30");
        comment5.setCommentUserPhoto(R.mipmap.photo_8);
        //comment.setDate(new Date());
        comments.add(comment5);


        //创建1条评论
        Comment comment6 = new Comment();
        comment6.setCommentUserName("Ronald Richards");
        comment6.setCommentContent("在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者在其中去之前作曲者庆中秋掌权者  05/27 13:39");
        comment6.setCommentUserPhoto(R.mipmap.photo_9);
        //comment.setDate(new Date());
        comments.add(comment6);

        return comments;
    }


    private void initView() {
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
    }
}