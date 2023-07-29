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
    private long id;
    private Post post;
    private String commentContent;
    private Date commentTime;
    private String commentUserPhoneNumber;
    private String commentUserName;         //(后期修改)
    private int commentUserPhoto;           //(后期修改)
    private List<ReplyBean> replyBeanList = new ArrayList<>();      //回复内容列表


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public int getCommentUserPhoto() {
        return commentUserPhoto;
    }

    public void setCommentUserPhoto(int commentUserPhoto) {
        this.commentUserPhoto = commentUserPhoto;
    }

    public List<ReplyBean> getReplyList() {
        return replyBeanList;
    }


    public void setReplyList(List<ReplyBean> replyBeanList) {
        this.replyBeanList = replyBeanList;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "id=" + id +
                ", post=" + post +
                ", commentContent='" + commentContent + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", commentUserPhoneNumber='" + commentUserPhoneNumber + '\'' +
                ", commentUserName='" + commentUserName + '\'' +
                ", commentUserPhoto=" + commentUserPhoto +
                ", replyBeanList=" + replyBeanList +
                '}';
    }
}
