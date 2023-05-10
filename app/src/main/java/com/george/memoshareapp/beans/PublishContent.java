package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: ContentPublic
 * @author: George
 * @description: TODO
 * @date: 2023/5/9 11:06
 * @version: 1.0
 */
public class PublishContent extends LitePalSupport implements Serializable {
    private int id;
    private int phoneNumber;
    private String publishedText;
    private List<String> phoneCachePath;
    private List<String> voiceCachePath;
    private List<String> contacts;
    private String location;
    private double longitude;
    private double latitude;
    private int isPublic;
    private String publishedTime;
    private String MemoryTime;

    public PublishContent() {
    }

    public PublishContent(int id, int phoneNumber, String publishedText, List<String> phoneCachePath, List<String> voiceCachePath, List<String> contacts, String location, double longitude, double latitude, int isPublic, String publishedTime, String memoryTime) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.publishedText = publishedText;
        this.phoneCachePath = phoneCachePath;
        this.voiceCachePath = voiceCachePath;
        this.contacts = contacts;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isPublic = isPublic;
        this.publishedTime = publishedTime;
        MemoryTime = memoryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPublishedText() {
        return publishedText;
    }

    public void setPublishedText(String publishedText) {
        this.publishedText = publishedText;
    }

    public List<String> getPhoneCachePath() {
        return phoneCachePath;
    }

    public void setPhoneCachePath(List<String> phoneCachePath) {
        this.phoneCachePath = phoneCachePath;
    }

    public List<String> getVoiceCachePath() {
        return voiceCachePath;
    }

    public void setVoiceCachePath(List<String> voiceCachePath) {
        this.voiceCachePath = voiceCachePath;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getMemoryTime() {
        return MemoryTime;
    }

    public void setMemoryTime(String memoryTime) {
        MemoryTime = memoryTime;
    }

    @Override
    public String toString() {
        return "PublishContent{" +
                "id=" + id +
                ", phoneNumber=" + phoneNumber +
                ", publishedText='" + publishedText + '\'' +
                ", phoneCachePath=" + phoneCachePath +
                ", voiceCachePath=" + voiceCachePath +
                ", contacts=" + contacts +
                ", location='" + location + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", isPublic=" + isPublic +
                ", publishedTime='" + publishedTime + '\'' +
                ", MemoryTime='" + MemoryTime + '\'' +
                '}';
    }
}