package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

public class ReplyBean extends LitePalSupport {
    private int id;					    //内容ID
    private CommentBean commentBean;    //回复对应的评论
    private String replyPhoneNumber;	//回复人账号
    private String replyNickname;	    //回复人昵称（后期修改）
    private int replyUserPhoto;         //回复人头像（后期修改）
    private String commentPhoneNumber;	//被回复人账号
    private String commentNickname;	    //被回复人昵称（后期修改）
    private String replyContent;	    //回复的内容
    private String replyTime;         //回复时间


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CommentBean getCommentBean() {
        return commentBean;
    }

    public void setCommentBean(CommentBean commentBean) {
        this.commentBean = commentBean;
    }

    public String getReplyPhoneNumber() {
        return replyPhoneNumber;
    }

    public void setReplyPhoneNumber(String replyPhoneNumber) {
        this.replyPhoneNumber = replyPhoneNumber;
    }

    public String getReplyNickname() {
        return replyNickname;
    }

    public void setReplyNickname(String replyNickname) {
        this.replyNickname = replyNickname;
    }

    public int getReplyUserPhoto() {
        return replyUserPhoto;
    }

    public void setReplyUserPhoto(int replyUserPhoto) {
        this.replyUserPhoto = replyUserPhoto;
    }

    public String getCommentPhoneNumber() {
        return commentPhoneNumber;
    }

    public void setCommentPhoneNumber(String commentPhoneNumber) {
        this.commentPhoneNumber = commentPhoneNumber;
    }

    public String getCommentNickname() {
        return commentNickname;
    }

    public void setCommentNickname(String commentNickname) {
        this.commentNickname = commentNickname;
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
