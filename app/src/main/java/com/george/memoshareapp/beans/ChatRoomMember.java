package com.george.memoshareapp.beans;


import org.litepal.crud.LitePalSupport;

import java.util.Date;


/**
 * @projectName: MemoShare
 * @package: com.george.memoshare.bean
 * @className: ChatRoomMember
 * @author: George
 * @description: TODO
 * @date: 2023/7/20 16:56
 * @version: 1.0
 */

public class ChatRoomMember extends LitePalSupport {
    private int chatRoomId;
    private String userId;
    private Date lastReadAt;
    private int isAdmin;
    private Date createdAt;
    private Date updatedAt;



    public ChatRoomMember(int chatRoomId, String userId, Date lastReadAt, int isAdmin, Date createdAt, Date updatedAt) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.lastReadAt = lastReadAt;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public ChatRoomMember(int chatRoomId, String userId, int isAdmin) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(Date lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
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
