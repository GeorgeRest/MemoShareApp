package com.george.memoshareapp.beans;



import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.MessageType;

import java.util.Date;

public class VoiceMessageItem extends MultiItemEntity {

    private String VoiceMsgPath;
    private Date date;
    private int UserSide;
    private String name;
    // 语音消息内容等相关属性和方法

    @Override
    public int getItemShowType() {
        return MessageType.VOICE;
    }

    @Override
    public String getItemContent() {

        return VoiceMsgPath;
    }

    @Override
    public void setUserName(String name) {
        this.name = name;
    }

    public VoiceMessageItem(String voiceMsgPath, Date date, int userSide, String name) {
        VoiceMsgPath = voiceMsgPath;
        this.date = date;
        UserSide = userSide;
        this.name = name;
    }

    @Override
    public String getUserName() {
        return name;
    }

    @Override
    public void setItemContent(String voiceMsgPath) {
        this.VoiceMsgPath = voiceMsgPath;
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
    public void setUserSideType(int UserSide) {
        this.UserSide =UserSide;
    }

    @Override
    public int getUserSideType() {
        return UserSide;
    }

}
