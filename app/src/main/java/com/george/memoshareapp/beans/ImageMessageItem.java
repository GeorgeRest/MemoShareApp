package com.george.memoshareapp.beans;



import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.MessageType;

import java.util.Date;

public class ImageMessageItem extends MultiItemEntity {
    // 图片消息内容等相关属性和方法
    private String ImagePath;
    private Date date;
    private int UserSide;
    private int progress;
    private String fileName;
    private User user;

    @Override
    public int getItemShowType() {
        return MessageType.IMAGE;
    }

    @Override
    public void setUserSideType(int UserSide) {
        this.UserSide =UserSide;
    }

    @Override
    public int getUserSideType() {
        return UserSide;
    }

    @Override
    public String getItemContent() {

        return ImagePath;
    }

    @Override
    public void setUserInfo(User user) {
        this.user = user;
    }

    @Override
    public User getUserInfo() {
        return user;
    }


    @Override
    public void setItemContent(String imagePath) {
        ImagePath = imagePath;
    }


    @Override
    public void setItemDate(Date date) {
        this.date = date;
    }

    @Override
    public Date getItemDate() {

        return date;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }


    public ImageMessageItem(String imagePath, Date date, int userSide, User user) {
        ImagePath = imagePath;
        this.date = date;
        UserSide = userSide;
        this.user = user;
    }

    @Override
    public String toString() {
        return "ImageMessageItem{" +
                "ImagePath='" + ImagePath + '\'' +
                ", date=" + date +
                ", UserSide=" + UserSide +
                ", progress=" + progress +
                ", fileName='" + fileName + '\'' +
                ", user=" + user +
                '}';
    }
}
