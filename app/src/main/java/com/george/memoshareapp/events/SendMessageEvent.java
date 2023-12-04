package com.george.memoshareapp.events;

import com.george.memoshareapp.beans.ChatMessage;

public class SendMessageEvent {

    public final ChatMessage message;

    public SendMessageEvent(ChatMessage message) {
        this.message = message;
    }


}