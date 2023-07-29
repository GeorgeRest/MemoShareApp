package com.george.memoshareapp.beans;

public class ChatRoomMember {
    private int chatRoomId;
    private int userId;
    private String lastReadAt;
    private int isAdmin;
    private String createdAt;
    private String updatedAt;

    public ChatRoomMember() {
    }
    public ChatRoomMember(int chatRoomId,int userId,int isAdmin) {
        this.chatRoomId=chatRoomId;
        this.userId=userId;
        this.isAdmin=isAdmin;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(String lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public int isAdmin() {
        return isAdmin;
    }

    public void setAdmin(int admin) {
        isAdmin = admin;
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
// Add constructors, getters, setters, etc. as needed
}
