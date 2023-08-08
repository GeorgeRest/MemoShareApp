package com.george.memoshareapp.beans;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class ChatRoom extends LitePalSupport {

    @Column(unique = true, defaultValue = "unknown")
    private int id;
//    private int IDEChatRoomId;
//
//    public int getIDEChatRoomId() {
//        return IDEChatRoomId;
//    }
//
//    public void setIDEChatRoomId(int IDEChatRoomId) {
//        this.IDEChatRoomId = IDEChatRoomId;
//    }

    private String type;

    private String name;

    private Date createdAt;

    private Date updatedAt;

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