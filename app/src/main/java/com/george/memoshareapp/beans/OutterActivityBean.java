package com.george.memoshareapp.beans;

import java.sql.Date;

public class OutterActivityBean {
    private String name;
    private String headPortraitPath;
    private String firstImagePath;
    private int activity_id;
    private String location;
    private String publishedTime;

    public OutterActivityBean(String name, String headPortraitPath, String firstImagePath, int activity_id, String location, String publishedTime) {
        this.name = name;
        this.headPortraitPath = headPortraitPath;
        this.firstImagePath = firstImagePath;
        this.activity_id = activity_id;
        this.location = location;
        this.publishedTime = publishedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPortraitPath() {
        return headPortraitPath;
    }

    public void setHeadPortraitPath(String headPortraitPath) {
        this.headPortraitPath = headPortraitPath;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }
}
