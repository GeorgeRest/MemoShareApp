package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class PersonPageInfo extends LitePalSupport implements Serializable {
    private long id;
    private String HeadPortraitPath;
    private String UserName;
    private String personalSignature;
    private String sex;
    private String birthday;
    private String BackGroundPath;
    private String region;

    public PersonPageInfo() {
    }

    public PersonPageInfo(long id, String headPortraitPath, String userName, String personalSignature, String sex, String birthday, String backGroundPath, String region) {
        this.id = id;
        HeadPortraitPath = headPortraitPath;
        UserName = userName;
        this.personalSignature = personalSignature;
        this.sex = sex;
        this.birthday = birthday;
        BackGroundPath = backGroundPath;
        this.region = region;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeadPortraitPath() {
        return HeadPortraitPath;
    }

    public void setHeadPortraitPath(String headPortraitPath) {
        HeadPortraitPath = headPortraitPath;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBackGroundPath() {
        return BackGroundPath;
    }

    public void setBackGroundPath(String backGroundPath) {
        BackGroundPath = backGroundPath;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "PersonPageInfo{" +
                "id=" + id +
                ", HeadPortraitPath='" + HeadPortraitPath + '\'' +
                ", UserName='" + UserName + '\'' +
                ", personalSignature='" + personalSignature + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", BackGroundPath='" + BackGroundPath + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
