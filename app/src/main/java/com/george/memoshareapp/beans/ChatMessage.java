package com.george.memoshareapp.beans;


import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * @projectName: MemoShare
 * @package: com.george.memoshare.bean
 * @className: chatMessage
 * @author: George
 * @description: TODO
 * @date: 2023/7/20 16:57
 * @version: 1.0
 */

public class ChatMessage extends LitePalSupport {
    private int id;
    private int chatRoomId;
    private String ChatRoomName;
    private String senderId;
    private String content;
    private String messageType;
    private Date createdAt;
    private Date updatedAt;
    private ChatAttachment attachment;

    public String getChatRoomName() {
        return ChatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        ChatRoomName = chatRoomName;
    }

    public ChatMessage() {
    }

    public ChatMessage(int id, int chatRoomId, String senderId, String content, String messageType, Date createdAt, Date updatedAt, ChatAttachment attachment) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attachment = attachment;
    }

    public ChatMessage(Date createdAt, String ChatRoomName, int chatRoomId, String senderId, String content, String messageType) {
        this.chatRoomId = chatRoomId;
        this.ChatRoomName=ChatRoomName;
        this.createdAt=createdAt;
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
    }
    public ChatMessage(int chatRoomId, String senderId, String content, String messageType) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
    }
    public ChatMessage(int chatRoomId, String senderId, String content, String messageType, ChatAttachment attachment) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
        this.attachment = attachment;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
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

    public ChatAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(ChatAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", chatRoomId=" + chatRoomId +
                ", senderId='" + senderId + '\'' +
                ", content='" + content + '\'' +
                ", messageType='" + messageType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", attachment=" + attachment +
                '}';
    }
}
