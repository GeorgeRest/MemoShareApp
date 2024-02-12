package com.george.memoshareapp.beans;

import java.io.Serializable;

public class InnerActivityBean implements Serializable {

    private int id;
    private String phoneNumber;
    private String publishedText;
    private String location;
    private double longitude;
    private double latitude;
    private int like;
    private boolean isFollowing;
    private int followId;
    private String name;
    private String headPortraitPath;
    private String firstImagePath;
    private String publishedTime;


    public InnerActivityBean(String name, String headPortraitPath, String firstImagePath, String publishedText, int id, String location, String publishedTime, double longitude, double latitude) {
        this.name = name;
        this.headPortraitPath = headPortraitPath;
        this.firstImagePath = firstImagePath;
        this.publishedText = publishedText;
        this.id = id;
        this.location = location;
        this.publishedTime = publishedTime;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPublishText() {
        return publishedText;
    }

    public void setPublishText(String publishedText) {
        this.publishedText = publishedText;
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

    public int getActivityId() {
        return id;
    }

    public void setActivity_id(int activity_id) {
        this.id = activity_id;
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
