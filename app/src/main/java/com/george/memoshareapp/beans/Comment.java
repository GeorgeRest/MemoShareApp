package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

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
public class Comment extends LitePalSupport {
    private Post post;
    private String commentContent;
    private String commentTime;
    private String commentUserPhoneNumber;

    private List<Comment> subComments;    // 子评论列表
    private String commentUserName;
    private int commentUserPhoto;


    public Comment() {
    }


    public Comment(Post post, String commentContent, String commentTime, String commentUserPhoneNumber, List<Comment> subComments, String commentUserName, int commentUserPhoto) {
        this.post = post;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.commentUserPhoneNumber = commentUserPhoneNumber;
        this.subComments = subComments;
        this.commentUserName = commentUserName;
        this.commentUserPhoto = commentUserPhoto;

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

    public List<Comment> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<Comment> subComments) {
        this.subComments = subComments;
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

}
