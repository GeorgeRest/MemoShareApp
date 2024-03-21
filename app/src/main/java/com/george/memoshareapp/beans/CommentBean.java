package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private String commentTime;
    private String commentUserPhoneNumber;      //回复人
    private String postPhoneNumber;                    //被回复人
    private List<ReplyBean> replyCommentList = new ArrayList<>() ;      //回复内容列表
    private User user;

    public String getPostPhoneNumber() {
        return postPhoneNumber;
    }

    public void setPostPhoneNumber(String postPhoneNumber) {
        this.postPhoneNumber = postPhoneNumber;
    }

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

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
//        Date parse=null;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        Date date = new Date();
//        String format = dateFormat.format(date);
//        try {
//            parse = dateFormat.parse(format);
//
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        this.commentTime = commentTime == null ? format : "commentTime";
        this.commentTime = commentTime;
    }

    public String getCommentUserPhoneNumber() {
        return commentUserPhoneNumber;
    }

    public void setCommentUserPhoneNumber(String commentUserPhoneNumber) {
        this.commentUserPhoneNumber = commentUserPhoneNumber;
    }


    public List<ReplyBean> getReplyCommentList() {
        return replyCommentList;
    }

    public void setReplyCommentList(List<ReplyBean> replyCommentList) {
        this.replyCommentList = replyCommentList;
    }
}
