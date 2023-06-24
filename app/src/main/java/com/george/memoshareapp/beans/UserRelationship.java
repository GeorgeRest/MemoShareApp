package com.george.memoshareapp.beans;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class UserRelationship extends LitePalSupport {
    private long id;
    private User initiator;
    private User target;
    private int relationshipStatus;
    @Column(ignore = true)
    public static final int ATTENTION_STATUS = 1;
    @Column(ignore = true)
    public static final int FANS_STATUS = 2;
    @Column(ignore = true)
    public static final int FRIEND_STATUS = 3;

    public UserRelationship() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public int getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(int relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }
}
