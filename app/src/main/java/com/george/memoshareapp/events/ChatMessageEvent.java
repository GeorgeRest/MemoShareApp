package com.george.memoshareapp.events;

import com.george.memoshareapp.beans.ChatMessage;
import com.google.common.eventbus.Subscribe;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.events
 * @className: ChatMessageEvent
 * @author: George
 * @description: TODO
 * @date: 2023/7/28 17:49
 * @version: 1.0
 */
public class ChatMessageEvent {
    private ChatMessage chatMessage;

    public void onMessageEvent(ChatMessage message) {
        this.chatMessage = message;
    }
}
