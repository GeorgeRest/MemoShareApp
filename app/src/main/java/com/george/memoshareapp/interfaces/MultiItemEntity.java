package com.george.memoshareapp.interfaces;

import java.util.Date;

public abstract class MultiItemEntity {
    public final static int SELF=1;
    public final static int OTHER=2;

    public abstract int getItemShowType();

    public abstract void setUserSideType(int UserSide);

    public abstract int getUserSideType();

    public abstract void setItemContent(String content);

    public abstract String getItemContent();

    public abstract void setUserName(String name);

    public abstract String getUserName();

    public abstract void setItemDate(Date date);

    public abstract Date getItemDate();

}
