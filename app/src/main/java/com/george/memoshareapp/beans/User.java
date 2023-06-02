package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

/**
 * @projectName: MemoShareApp
 * @package: com.george.memoshareApp.beans
 * @className: User
 * @author: George
 * @description: 用户表
 * @date: 2023/4/23 12:11
 * @version: 1.0
 */
public class User extends LitePalSupport {
    private long id;
    private String phoneNumber;
    private String password;
    private int followCount;//关注数
    private int fansCount;//粉丝数
    private int friendCount;//好友数

    public User(long id, String phoneNumber, String password, int followCount, int fansCount, int friendCount) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.followCount = followCount;
        this.fansCount = fansCount;
        this.friendCount = friendCount;
    }

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
