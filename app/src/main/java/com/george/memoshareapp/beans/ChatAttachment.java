package com.george.memoshareapp.beans;


import java.util.Date;

/**
 * @projectName: MemoShare
 * @package: com.george.memoshare.bean
 * @className: ChatAttachment
 * @author: George
 * @description: TODO
 * @date: 2023/7/20 17:09
 * @version: 1.0
 */
public class ChatAttachment {
    private int id;
    private int messageId;
    private String filePath;
    private String fileType;
    private String createdAt;
    private String updatedAt;

    public ChatAttachment() {

    }

    public ChatAttachment(int id, int messageId, String filePath, String fileType, String createdAt, String updatedAt) {
        this.id = id;
        this.messageId = messageId;
        this.filePath = filePath;
        this.fileType = fileType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ChatAttachment(String filePath, String fileType) {
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public ChatAttachment(int messageId, String filePath, String fileType) {
        this.messageId = messageId;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    @Override
    public String toString() {
        return "ChatAttachment{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
