package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: Comment
 * @author: George
 * @description: TODO
 * @date: 2023/5/14 15:18
 * @version: 1.0
 */


public class CommentBean extends LitePalSupport implements Serializable {
    private int id;
    private int postId;
    private String commentContent;
    private Date commentTime;
    private String commentUserPhoneNumber;
    private List<ReplyBean> replyBeanList = new ArrayList<>();      //回复内容列表


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime == null ? new Date() : commentTime;
    }

    public String getCommentUserPhoneNumber() {
        return commentUserPhoneNumber;
    }

    public void setCommentUserPhoneNumber(String commentUserPhoneNumber) {
        this.commentUserPhoneNumber = commentUserPhoneNumber;
    }


    public List<ReplyBean> getReplyList() {
        return replyBeanList;
    }


    public void setReplyList(List<ReplyBean> replyBeanList) {
        this.replyBeanList = replyBeanList;
    }

}
