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
    private long like;
    private long share;
    private List<Comment> subComments;    // 子评论列表

    public Comment() {
    }

    public Comment(Post post, String commentContent, String commentTime, String commentUserPhoneNumber, long like, long share, List<Comment> subComments) {
        this.post = post;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.commentUserPhoneNumber = commentUserPhoneNumber;
        this.like = like;
        this.share = share;
        this.subComments = subComments;
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

    public long getLike() {
        return like;
    }

    public void setLike(long like) {
        this.like = like;
    }

    public long getShare() {
        return share;
    }

    public void setShare(long share) {
        this.share = share;
    }

    public List<Comment> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<Comment> subComments) {
        this.subComments = subComments;
    }
}
