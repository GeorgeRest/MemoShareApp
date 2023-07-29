package com.george.memoshareapp.beans;


import java.util.Date;

/**
 * @projectName: MemoShare
 * @package: com.george.memoshare.bean
 * @className: chatRoom
 * @author: George
 * @description: TODO
 * @date: 2023/7/20 16:56
 * @version: 1.0
 */

public class ChatRoom {
    private int id;
    private String type;
    private String name;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
    public ChatRoom() {
    }

    public ChatRoom(int id, String type, String name, String avatar, Date createdAt, Date updatedAt) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
