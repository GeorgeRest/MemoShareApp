package com.george.memoshareapp.beans;


import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.MessageType;

import java.util.Date;

public class TextMessageItem extends MultiItemEntity {
    // 文本消息内容等相关属性和方法
    private String textContent;
    private Date date;
    private int UserSide;
    private User user;
    private int progress;
    private String fileName;

    public TextMessageItem(String textContent, Date date, int userSide, User user) {
        this.textContent = textContent;
        this.date = date;
        UserSide = userSide;
        this.user = user;
    }

    @Override
    public int getItemShowType() {
        return MessageType.TEXT;
    }

    @Override
    public String getItemContent() {

        return textContent;
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
    public void setItemContent(String textContent) {
        this.textContent = textContent;
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

    @Override
    public void setUserSideType(int UserSide) {
        this.UserSide = UserSide;
    }

    @Override
    public int getUserSideType() {
        return UserSide;
    }

    @Override
    public String toString() {
        return "TextMessageItem{" +
                "textContent='" + textContent + '\'' +
                ", date=" + date +
                ", UserSide=" + UserSide +
                ", user=" + user +
                ", progress=" + progress +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
