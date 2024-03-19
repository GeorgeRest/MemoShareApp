package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.User;

import java.util.Date;

public abstract class MultiItemEntity {
    public final static int SELF=1;
    public final static int OTHER=2;

    public abstract int getItemShowType();

    public abstract void setUserSideType(int UserSide);

    public abstract int getUserSideType();

    public abstract void setItemContent(String content);

    public abstract String getItemContent();

    public abstract void setUserInfo(User user);

    public abstract User getUserInfo();

    public abstract void setItemDate(Date date);

    public abstract Date getItemDate();
    public abstract void setProgress(int progress);
    public abstract int getProgress();
    public abstract void setFileName(String  fileName);
    public abstract String getFileName();


}
