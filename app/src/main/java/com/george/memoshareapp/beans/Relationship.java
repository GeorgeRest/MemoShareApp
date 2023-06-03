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
    private long userId;
    private long followingId;

    public Relationship() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(long followingId) {
        this.followingId = followingId;
    }
}
