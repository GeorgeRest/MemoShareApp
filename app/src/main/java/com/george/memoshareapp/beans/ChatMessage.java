package com.george.memoshareapp.beans;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

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
    private int ChatMessageId;
    private String senderId;
    private String content;
    private String messageType;
    private String createdAt;
    private String updatedAt;
    @Column(ignore = true)
    private User user;
    private ChatAttachment attachment;

    @Column(ignore = true)
    private ChatRoom chatRoom;

    public ChatMessage() {
    }

    public ChatMessage(int id, int chatRoomId, String senderId, String content, String messageType, String createdAt, String updatedAt, ChatAttachment attachment) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attachment = attachment;
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

    public ChatAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(ChatAttachment attachment) {
        this.attachment = attachment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public int getChatMessageId() {
        return ChatMessageId;
    }

    public void setChatMessageId(int chatMessageId) {
        ChatMessageId = chatMessageId;
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", chatRoomId=" + chatRoomId +
                ", ChatMessageId=" + ChatMessageId +
                ", senderId='" + senderId + '\'' +
                ", content='" + content + '\'' +
                ", messageType='" + messageType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                ", attachment=" + attachment +
                ", chatRoom=" + chatRoom +
                '}';
    }
}
