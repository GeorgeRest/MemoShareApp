package com.george.memoshareapp.beans;



import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.MessageType;

import java.util.Date;

public class VoiceMessageItem extends MultiItemEntity {

    private String VoiceMsgPath;
    private Date date;
    private int UserSide;
    private int progress;
    private String fileName;
    private User user;
    // 语音消息内容等相关属性和方法

    @Override
    public int getItemShowType() {
        return MessageType.VOICE;
    }

    @Override
    public String getItemContent() {

        return VoiceMsgPath;
    }


    public VoiceMessageItem(String voiceMsgPath, Date date, int userSide, User user) {
        VoiceMsgPath = voiceMsgPath;
        this.date = date;
        UserSide = userSide;
        this.user = user;
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
        this.UserSide =UserSide;
    }

    @Override
    public int getUserSideType() {
        return UserSide;
    }

    @Override
    public void setUserInfo(User user) {
        this.user = user;
    }

    @Override
    public User getUserInfo() {
        return user;
    }
}
