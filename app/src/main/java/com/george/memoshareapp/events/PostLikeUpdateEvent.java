package com.george.memoshareapp.events;

import com.george.memoshareapp.beans.Post;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.events
 * @className: PostLikeUpdateEvent
 * @author: George
 * @description: TODO
 * @date: 2023/6/10 18:25
 * @version: 1.0
 */
public class PostLikeUpdateEvent {
    private Post post;
    private boolean isLiked;

    public PostLikeUpdateEvent(Post post, boolean isLiked) {
        this.post = post;
        this.isLiked = isLiked;
    }

    public Post getPost() {
        return post;
    }

    public boolean isLiked() {
        return isLiked;
    }
}
