package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class ReplyBean extends LitePalSupport {
    private int id;					    //内容ID
    private int commentBeanId;          //回复对应的评论
    private String replyPhoneNumber;	//回复人账号
    private String commentPhoneNumber;	//被回复人账号
    private String replyContent;	    //回复的内容
    private String replyTime;             //回复时间
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentBeanId() {
        return commentBeanId;
    }

    public void setCommentBeanId(int commentBeanId) {
        this.commentBeanId = commentBeanId;
    }

    public String getReplyPhoneNumber() {
        return replyPhoneNumber;
    }

    public void setReplyPhoneNumber(String replyPhoneNumber) {
        this.replyPhoneNumber = replyPhoneNumber;
    }

    public String getCommentPhoneNumber() {
        return commentPhoneNumber;
    }

    public void setCommentPhoneNumber(String commentPhoneNumber) {
        this.commentPhoneNumber = commentPhoneNumber;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }
}
