package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: Relationship
 * @author: George
 * @description: TODO
 * @date: 2023/6/3 16:42
 * @version: 1.0
 */
public class Relationship extends LitePalSupport implements Serializable {
    private long id;
    private User initiator;
    private User target;
    private int relationshipStatus;
    public static final int ATTENTION_STATUS = 1;
    public static final int FANS_STATUS = 2;
    public static final int FRIEND_STATUS = 3;

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

    public Relationship() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
