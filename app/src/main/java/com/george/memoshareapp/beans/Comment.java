package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

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
public class Comment extends LitePalSupport implements Serializable {
    private Post post;
    private String commentContent;
    private String commentTime;
    private String commentUserPhoneNumber;


    public Comment() {
    }

    public Comment( String commentContent, String commentTime, String commentUserPhoneNumber) {
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.commentUserPhoneNumber = commentUserPhoneNumber;
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

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentUserPhoneNumber() {
        return commentUserPhoneNumber;
    }

    public void setCommentUserPhoneNumber(String commentUserPhoneNumber) {
        this.commentUserPhoneNumber = commentUserPhoneNumber;
    }


}
