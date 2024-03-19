package com.george.memoshareapp.events;

public class HuoDongReleaseEvent {

    private boolean isFollowing;

    public HuoDongReleaseEvent(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public boolean getHuoDongEvent() {
        return isFollowing;
    }

    public void setHuoDongEvent(boolean following) {
        isFollowing = following;
    }
}
