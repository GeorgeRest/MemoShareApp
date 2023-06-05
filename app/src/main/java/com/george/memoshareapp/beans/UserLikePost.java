package com.george.memoshareapp.beans;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: UserLikePost
 * @author: George
 * @description: TODO
 * @date: 2023/6/5 19:47
 * @version: 1.0
 */
public class UserLikePost extends LitePalSupport {
        private User user;
        private Post post;
        private Date likeTime;

        public UserLikePost() {
        }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(Date likeTime) {
        this.likeTime = likeTime;
    }
}
