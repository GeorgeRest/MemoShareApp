package com.george.memoshareapp.events;

import com.george.memoshareapp.beans.ChatMessage;

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
   public final   ChatMessage chatMessage;

    public ChatMessageEvent(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

}
