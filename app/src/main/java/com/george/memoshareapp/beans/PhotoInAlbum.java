package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class PhotoInAlbum extends LitePalSupport implements Serializable {
    private  long album_id;
    private String photo_path;
    private String userPhoneNumber;
    private int firstPhoto;
    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }


    public PhotoInAlbum(long album_id, String userPhoneNumber,String photo_path, int firstPhoto) {
        this.album_id=album_id;
        this.photo_path=photo_path;
        this.userPhoneNumber=userPhoneNumber;
        this.firstPhoto=firstPhoto;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public int getFirstPhoto() {
        return firstPhoto;
    }

    public void setFirstPhoto(int firstPhoto) {
        this.firstPhoto = firstPhoto;
    }
    @Override
    public String toString() {
        return "PhotoInAlbum{" +
                "album_id=" + album_id +
                ", photo_path='" + photo_path + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", firstPhoto=" + firstPhoto +
                '}';
    }
}
