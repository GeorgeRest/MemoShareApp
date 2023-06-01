package com.george.memoshareapp.beans;

public class UserRelationship {
    private long id;
    private User user;//当前用户
    private User targetUser;//目标用户
    private int relationshipType;//关系类型，如关注、粉丝、朋友等

    public UserRelationship() {
    }

    public UserRelationship(long id, User user, User targetUser, int relationshipType) {
        this.id = id;
        this.user = user;
        this.targetUser = targetUser;
        this.relationshipType = relationshipType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public int getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        this.relationshipType = relationshipType;
    }
}
