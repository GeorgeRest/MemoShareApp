package com.george.memoshareapp.beans;

import java.util.List;

public class ChatRoomRequest {
    private ChatRoom chatRoom;
    private List<ChatRoomMember> chatRoomMembers;

    public ChatRoomRequest(ChatRoom chatRoom, List<ChatRoomMember> chatRoomMembers) {
        this.chatRoom = chatRoom;
        this.chatRoomMembers = chatRoomMembers;
    }
}