package com.george.memoshareapp.beans;


import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

/**
 * @projectName: MemoShare
 * @package: com.george.memoshare.bean
 * @className: chatRoom
 * @author: George
 * @description: TODO
 * @date: 2023/7/20 16:56
 * @version: 1.0
 */

public class ChatRoom extends LitePalSupport {
    private int id;
    private int chatRoomId;
    private String type;
    private String name;
    private String avatar;
    private String createdAt;
    private String updatedAt;

    private String lastMessageType;
    private String lastMessage;

    private String lastMessageTime;
    @Column(ignore = true)
    private ChatRoomMember chatRoomMember;

    public ChatRoom() {
    }

    public ChatRoom(int id, String type, String name, String avatar, String createdAt, String updatedAt) {
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

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessageType() {
        return lastMessageType;
    }

    public void setLastMessageType(String lastMessageType) {
        this.lastMessageType = lastMessageType;
    }

    public ChatRoomMember getChatRoomMember() {
        return chatRoomMember;
    }

    public void setChatRoomMember(ChatRoomMember chatRoomMember) {
        this.chatRoomMember = chatRoomMember;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", chatRoomId=" + chatRoomId +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastMessageType='" + lastMessageType + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTime='" + lastMessageTime + '\'' +
                '}';
    }
}
