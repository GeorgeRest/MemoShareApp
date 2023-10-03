package com.george.memoshareapp.manager;

import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.ChatRoomMember;
import com.george.memoshareapp.beans.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomManager {

    private List<ChatMessage> messageList;

    // 根据聊天室名称查询最新一条消息内容
    public  String getLatestMessageContentByChatRoomName(String chatRoomName) {
        ChatMessage latestMessage = LitePal
                .where("chatRoomName = ?",chatRoomName)
                .order("createdAt desc")
                .findFirst(ChatMessage.class);

        if (latestMessage != null) {
            return latestMessage.getContent();
        } else {
            return "创建群聊成功，快来发一条消息吧~"; // 或者您可以根据需要返回其他默认值
        }
    }
    public Date getLatestMessageTimeByChatRoomName(String chatRoomName) {
        ChatMessage latestMessage = LitePal
                .where("chatRoomName = ?",chatRoomName)
                .order("createdAt desc")
                .findFirst(ChatMessage.class);

        if (latestMessage != null) {
            return latestMessage.getCreatedAt();
        } else {
            Date date = new Date();
            return date; // 或者您可以根据需要返回其他默认值
        }
    }
    public  List<ChatRoom> getLast20ChatRoom() {
        messageList = new ArrayList<>();
//        // 使用LitePal查询获取最后20条ChatRoom记录，如果不足20条则返回所有可用的记录
        List<ChatRoom> chatRooms = LitePal.order("id desc").limit(20).find(ChatRoom.class);
//        for (ChatRoom c:chatRooms) {
//            List<ChatRoom> list = new ArrayList<>();
//
//            ChatMessage chatMessage = LitePal.where("chatRoomId = ?", c.getId() + "")
//                    .order("createdAt asc")
//                    .find(chatRooms)
//                    .get(0);
//            messageList.add(chatMessage);
//        }
//        for (ChatMessage m :messageList) {
//            Date d=m.getCreatedAt();
//
//
//        }

//        List<ChatRoom> chatRoomList = new ArrayList<>();
//        chatRoomList = chatRooms.size() <= 20 ? chatRooms : chatRooms.subList(0, chatRooms.size());
//        // 如果记录不足20条，直接返回chatRooms，否则只返回前20条记录
//        List<ChatRoom> chatRooms = LitePal.order("updatedAt desc").find(ChatRoom.class);

        return chatRooms;
    }

    public ChatRoom getChatRoomByChatRoomName(String  chatRoomName) {
        return LitePal.where("name = ?", chatRoomName)
                .findFirst(ChatRoom.class);
    }
    public List<User> getMembersByChatRoomNameId(int chatRoomId) {
        List<ChatRoomMember> chatRoomMemberList = LitePal.where("chatRoomId = ?", String.valueOf(chatRoomId))
                .find(ChatRoomMember.class);
        List<User> userlist = new ArrayList<>();
        for (ChatRoomMember c:chatRoomMemberList) {
            User user = new User(c.getUserId());
            userlist.add(user);
        }
        return userlist;

    }
    public List<ChatMessage> getChatRoomMessages(String chatroomName) {
        // 在这里查询数据库或从服务器获取聊天室的消息数据
        // 示例代码，您需要根据实际情况替换
        List<ChatMessage> chatMessages = new ArrayList<>();
        // 假设 chatRoomId 是当前聊天室的 ID

            chatMessages = LitePal
                    .where("chatroomName = ?",chatroomName)
                    .order("createdAt asc") // 根据消息时间升序排列，您可以根据需要调整排序方式
                    .find(ChatMessage.class);

        return chatMessages;
    }



}
