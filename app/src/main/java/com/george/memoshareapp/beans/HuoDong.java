package com.george.memoshareapp.beans;

import java.sql.Date;

public class HuoDong {
    private String userId;
    private String userName;
    private String imagePath;
    private Date date;
    private String avatar;

    public HuoDong(String userId, String userName, String imagePath, Date date, String avatar) {
        this.userId = userId;
        this.userName = userName;
        this.imagePath = imagePath;
        this.date = date;
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
