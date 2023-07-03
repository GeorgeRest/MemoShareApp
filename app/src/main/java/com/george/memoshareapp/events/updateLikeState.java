package com.george.memoshareapp.events;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.events
 * @className: LastClickedPositionEvent
 * @author: George
 * @description: TODO
 * @date: 2023/5/20 18:02
 * @version: 1.0
 */
public class updateLikeState {
    private int postId;
    private boolean isLiked;

    public updateLikeState(int postId, boolean isLiked) {
        this.postId = postId;
        this.isLiked = isLiked;
    }

    public int getPostId() {
        return postId;
    }

    public boolean isLiked() {
        return isLiked;
    }
}

