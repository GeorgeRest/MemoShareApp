package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class PhotoInAlbum extends LitePalSupport implements Serializable {
    private  long album_id;
    private String photo_path;
    private int firstPhoto;
    public PhotoInAlbum(long album_id, String photo_path, int firstPhoto) {
        this.album_id=album_id;
        this.photo_path=photo_path;
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
}
