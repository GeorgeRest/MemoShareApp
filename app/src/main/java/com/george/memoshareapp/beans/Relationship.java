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
    private String initiatorNumber;
    private String targetNumber;
    private int relationshipStatus;     //关系类型，如关注、粉丝、朋友等
    public static final int ATTENTION_STATUS = 1;
    public static final int FANS_STATUS = 2;
    public static final int FRIEND_STATUS = 3;

    public Relationship() {
    }

    public Relationship(String initiatorNumber, String targetNumber) {
        this.initiatorNumber = initiatorNumber;
        this.targetNumber = targetNumber;
    }

    public Relationship(String initiatorNumber, String targetNumber, int relationshipStatus) {
        this.initiatorNumber = initiatorNumber;
        this.targetNumber = targetNumber;
        this.relationshipStatus = relationshipStatus;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInitiatorNumber() {
        return initiatorNumber;
    }

    public void setInitiatorNumber(String initiatorNumber) {
        this.initiatorNumber = initiatorNumber;
    }

    public String getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(String targetNumber) {
        this.targetNumber = targetNumber;
    }

    public int getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(int relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }
}
