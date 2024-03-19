package com.george.memoshareapp.beans;

import java.util.List;

public class ChatRoomRequest {
    private List<ChatRoom> chatRooms;
    private List<ChatRoomMember> chatRoomMembers;

    private List<User> users;

    public ChatRoomRequest(List<ChatRoom> chatRooms, List<ChatRoomMember> chatRoomMembers) {
        this.chatRooms = chatRooms;
        this.chatRoomMembers = chatRoomMembers;
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public List<ChatRoomMember> getChatRoomMembers() {
        return chatRoomMembers;
    }

    public void setChatRoomMembers(List<ChatRoomMember> chatRoomMembers) {
        this.chatRoomMembers = chatRoomMembers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ChatRoomRequest{" +
                "chatRoom=" + chatRooms +
                ", chatRoomMembers=" + chatRoomMembers +
                '}';
    }
}