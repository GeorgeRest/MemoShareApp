package com.george.memoshareapp.beans;



import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.MessageType;

import java.util.Date;

public class ImageMessageItem extends MultiItemEntity {
    // 图片消息内容等相关属性和方法
    private String ImagePath;
    private Date date;
    private int UserSide;
    private String name;

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
    public void setUserName(String name) {
        this.name = name;
    }

    @Override
    public String getUserName() {
        return name;
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

    public ImageMessageItem(String imagePath, Date date, int userSide, String name) {
        ImagePath = imagePath;
        this.date = date;
        UserSide = userSide;
        this.name = name;
    }
}
