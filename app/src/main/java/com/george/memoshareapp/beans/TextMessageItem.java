package com.george.memoshareapp.beans;



import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.MessageType;

import java.util.Date;

public class TextMessageItem extends MultiItemEntity {
    // 文本消息内容等相关属性和方法
    private String textContent;
    private Date date;
    private int UserSide;
    private String name;
    private int progress;

    public TextMessageItem(String textContent, Date date, int userSide, String name) {
        this.textContent = textContent;
        this.date = date;
        UserSide = userSide;
        this.name = name;
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
    public void setUserName(String name) {
        this.name = name;
    }

    @Override
    public String getUserName() {
        return name;
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
    public void setUserSideType(int UserSide) {
        this.UserSide =UserSide;
    }

    @Override
    public int getUserSideType() {
        return UserSide;
    }
}
