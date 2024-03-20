package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Album extends LitePalSupport implements Serializable {
    private int id;



    private String phoneNumber;
    private String albumName;
    private  String albumDescription;
    private String createdTime;
    public Album(String phoneNumber, String albumName,String albumDescription,String createdTime) {
        this.albumName =albumName;
        this.phoneNumber=phoneNumber;
        this.createdTime=createdTime;
        this.albumDescription=albumDescription;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Album(int id,String phoneNumber, String albumName,String albumDescription,String createdTime) {
        this.albumName =albumName;
        this.phoneNumber=phoneNumber;
        this.createdTime=createdTime;
        this.albumDescription=albumDescription;
        this.id=id;
    }
    public String getAlbumDescription() {
        return albumDescription;
    }
    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }
    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", albumName='" + albumName + '\'' +
                ", albumDescription='" + albumDescription + '\'' +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }
}
