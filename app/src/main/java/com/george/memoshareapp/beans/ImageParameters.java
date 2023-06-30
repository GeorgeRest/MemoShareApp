package com.george.memoshareapp.beans;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: ImageParameters
 * @author: George
 * @description: TODO
 * @date: 2023/6/2 12:10
 * @version: 1.0
 */
public class ImageParameters extends LitePalSupport implements Serializable {
    private long id;
    private String photoCachePath;
    private int width;
    private int height;
    private Post post;
    public ImageParameters() {

    }

    public ImageParameters(String photoCachePath, int width, int height) {
        this.photoCachePath = photoCachePath;
        this.width = width;
        this.height = height;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPhotoCachePath(String photoCachePath) {
        this.photoCachePath = photoCachePath;
    }

    public String getPhotoCachePath() {
        return photoCachePath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
