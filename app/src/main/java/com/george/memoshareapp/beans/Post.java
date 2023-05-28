package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;


import java.io.Serializable;
import java.util.ArrayList;
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
public class Post extends LitePalSupport implements Serializable {
    private long id;
    private String phoneNumber;
    private String publishedText;
    private List<String> photoCachePath;
    private List<Recordings> recordings;
    private List<String> contacts;
    private String location;
    private double longitude;
    private double latitude;
    private int isPublic;
    private String publishedTime;
    private String memoryTime;
    private long like;
    private long share;
    private List<CommentBean> commentBeans;


    public Post() {
        commentBeans = new ArrayList<>();
    }


    public Post(String phoneNumber, String publishedText, List<String> photoCachePath, List<Recordings> recordings, List<String> contacts, String location, double longitude, double latitude, int isPublic, String publishedTime, String memoryTime) {
        this.phoneNumber = phoneNumber;
        this.publishedText = publishedText;
        this.photoCachePath = photoCachePath;
        this.recordings = recordings;
        this.contacts = contacts;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isPublic = isPublic;
        this.publishedTime = publishedTime;
        this.memoryTime = memoryTime;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber (String phoneNumber){
            this.phoneNumber = phoneNumber;
        }

        public String getPublishedText () {
            return publishedText;
        }

        public void setPublishedText (String publishedText){
            this.publishedText = publishedText;
        }

        public List<String> getPhotoCachePath () {
            return photoCachePath;
        }

        public void setPhotoCachePath (List < String > photoCachePath) {
            this.photoCachePath = photoCachePath;
        }

        public List<Recordings> getRecordings () {
            return recordings;
        }

        public void setRecordings (List < Recordings > recordings) {
            this.recordings = recordings;
        }

        public List<String> getContacts () {
            return contacts;
        }

        public void setContacts (List < String > contacts) {
            this.contacts = contacts;
        }

        public String getLocation () {
            return location;
        }

        public void setLocation (String location){
            this.location = location;
        }

        public double getLongitude () {
            return longitude;
        }

        public void setLongitude ( double longitude){
            this.longitude = longitude;
        }

        public double getLatitude () {
            return latitude;
        }

        public void setLatitude ( double latitude){
            this.latitude = latitude;
        }

        public int getIsPublic () {
            return isPublic;
        }

        public void setIsPublic ( int isPublic){
            this.isPublic = isPublic;
        }

        public String getPublishedTime () {
            return publishedTime;
        }

        public void setPublishedTime (String publishedTime){
            this.publishedTime = publishedTime;
        }

        public String getMemoryTime () {
            return memoryTime;
        }

        public void setMemoryTime (String memoryTime){
            this.memoryTime = memoryTime;
        }

//        public List<CommentBean> getComments () {
//            if (commentBeans == null) {
//                commentBeans = new ArrayList<>();
//            }
//            return commentBeans;
//        }
//
//        public void setComments (List < CommentBean > commentBeans) {
//            this.commentBeans = commentBeans;
//        }


        public long getLike () {
            return like;
        }

        public void setLike ( long like){
            this.like = like;
        }

        public long getShare () {
            return share;
        }

        public void setShare ( long share){
            this.share = share;
        }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", publishedText='" + publishedText + '\'' +
                ", photoCachePath=" + photoCachePath +
                ", recordings=" + recordings +
                ", contacts=" + contacts +
                ", location='" + location + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", isPublic=" + isPublic +
                ", publishedTime='" + publishedTime + '\'' +
                ", memoryTime='" + memoryTime + '\'' +
                ", like=" + like +
                ", share=" + share +
                ", commentBeans=" + commentBeans +
                '}';
    }
}

