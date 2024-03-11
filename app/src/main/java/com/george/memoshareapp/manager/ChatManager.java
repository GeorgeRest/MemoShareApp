package com.george.memoshareapp.manager;

import android.content.Context;

import com.george.memoshareapp.beans.ChatAttachment;
import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.ChatRoomMember;
import com.george.memoshareapp.beans.ChatRoomRequest;
import com.george.memoshareapp.beans.ImageMessageItem;
import com.george.memoshareapp.beans.TextMessageItem;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.beans.VoiceMessageItem;
import com.george.memoshareapp.http.api.ChatRoomApi;
import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.TimeUtil;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.manager
 * @className: ChatManager
 * @author: George
 * @description: TODO
 * @date: 2023/11/5 0:10
 * @version: 1.0
 */
public class ChatManager {

    private Context context;
    private UserManager userManager;
    private User selfUser;
    private ChatRoomApi chatRoomApi;


    public ChatManager(Context context) {
        this.context = context;
        userManager = new UserManager(context);
        selfUser = userManager.findUserByPhoneNumber(UserManager.getSelfPhoneNumber(context));
        chatRoomApi = RetrofitManager.getInstance().create(ChatRoomApi.class);
    }

    public void saveOrUpdateChatRoomAndMember(ChatRoomRequest chatRoomRequest) {
        for (ChatRoom chatRoomServer : chatRoomRequest.getChatRooms()) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChatRoomId(chatRoomServer.getId());
            chatRoom.setType(chatRoomServer.getType());
            chatRoom.setName(chatRoomServer.getName());
            chatRoom.setAvatar(chatRoomServer.getAvatar());
            chatRoom.setCreatedAt(chatRoomServer.getCreatedAt());
            chatRoom.setUpdatedAt(chatRoomServer.getUpdatedAt());
            chatRoom.saveOrUpdate("chatRoomId = ?", String.valueOf(chatRoomServer.getId()));
        }


        List<ChatRoomMember> chatRoomMemberList = chatRoomRequest.getChatRoomMembers();
        for (ChatRoomMember chatRoomMemberServer : chatRoomMemberList) {
            ChatRoomMember chatRoomMember = new ChatRoomMember();
            chatRoomMember.setChatRoomId(chatRoomMemberServer.getChatRoomId());
            chatRoomMember.setUserId(chatRoomMemberServer.getUserId());
            chatRoomMember.setLastReadAt(chatRoomMemberServer.getLastReadAt());
            chatRoomMember.setIsAdmin(chatRoomMemberServer.getIsAdmin());
            chatRoomMember.setCreatedAt(chatRoomMemberServer.getCreatedAt());
            chatRoomMember.setUpdatedAt(chatRoomMemberServer.getUpdatedAt());
            chatRoomMember.saveOrUpdate("chatRoomId = ? and userId = ?", String.valueOf(chatRoomMemberServer.getChatRoomId()), String.valueOf(chatRoomMemberServer.getUserId()));
        }
        for (User user : chatRoomRequest.getUsers()) {
            user.saveOrUpdate("phoneNumber = ?", user.getPhoneNumber());
        }
    }

    public void saveOrUpdateUserChatData() {
        ChatRoomApi chatRoomApi = RetrofitManager.getInstance().create(ChatRoomApi.class);
        chatRoomApi.getChatRoomAndMembers(UserManager.getSelfPhoneNumber(context)).enqueue(new Callback<ChatRoomRequest>() {
            @Override
            public void onResponse(Call<ChatRoomRequest> call, Response<ChatRoomRequest> response) {
                if (response.isSuccessful()) {
                    ChatRoomRequest chatRoomRequest = response.body();
                    saveOrUpdateChatRoomAndMember(chatRoomRequest);
                } else {
                    Logger.d("saveOrUpdateUserChatData fail: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ChatRoomRequest> call, Throwable t) {
                Logger.d("saveOrUpdateUserChatData fail: " + t.getMessage());
            }
        });
    }

    public void saveChatMessage(ChatMessage serverChatMessage) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(serverChatMessage.getChatRoomId());
        chatMessage.setSenderId(serverChatMessage.getSenderId());
        chatMessage.setContent(serverChatMessage.getContent());
        chatMessage.setMessageType(serverChatMessage.getMessageType());
        chatMessage.setCreatedAt(serverChatMessage.getCreatedAt());
        chatMessage.setUpdatedAt(serverChatMessage.getUpdatedAt());
        chatMessage.setChatMessageId(serverChatMessage.getId());
        if (serverChatMessage.getAttachment() != null) {
            ChatAttachment chatAttachment = new ChatAttachment();
            chatAttachment.setMessageId(serverChatMessage.getId());
            chatAttachment.setFileType(serverChatMessage.getAttachment().getFileType());
            chatAttachment.setFilePath(serverChatMessage.getAttachment().getFilePath());
            chatAttachment.setCreatedAt(serverChatMessage.getCreatedAt());
            chatAttachment.setUpdatedAt(serverChatMessage.getUpdatedAt());
            chatAttachment.save();
            chatMessage.setAttachment(chatAttachment);
        }
        chatMessage.save();
        if (serverChatMessage.getUser() != null) {
            User user = new User();
            user.setName(serverChatMessage.getUser().getName());
            user.setPhoneNumber(serverChatMessage.getUser().getPhoneNumber());
            user.setRegion(serverChatMessage.getUser().getRegion());
            user.setSignature(serverChatMessage.getUser().getRegion());
            user.setHeadPortraitPath(serverChatMessage.getUser().getHeadPortraitPath());
            user.setGender(serverChatMessage.getUser().getGender());
            user.setBirthday(serverChatMessage.getUser().getBirthday());
            user.setBackGroundPath(serverChatMessage.getUser().getBackGroundPath());
            user.saveOrUpdate("phoneNumber = ?", serverChatMessage.getUser().getPhoneNumber());
        }

        saveOrUpdateChatRoom(serverChatMessage);
    }

    public List<MultiItemEntity> getMessageFromDB(String chatRoomId, long currentTimeMillis) {
        ArrayList<MultiItemEntity> multiItemEntityList = new ArrayList<>();
        List<ChatMessage> chatMessageList = new ArrayList<>();
        if (currentTimeMillis == 0) {
            chatMessageList = LitePal.where("chatRoomId = ?", chatRoomId).find(ChatMessage.class, true);
        } else {
            String dateString = convertMillisToDateString(currentTimeMillis);
            chatMessageList = LitePal.where("chatRoomId = ? and createdAt > ?", String.valueOf(chatRoomId), dateString)
                    .order("createdAt")
                    .find(ChatMessage.class, true);
        }
        for (ChatMessage chatMessage : chatMessageList) {
            User userInfo = userManager.findUserByPhoneNumber(chatMessage.getSenderId());
            int userSide = userInfo.getPhoneNumber().equals(UserManager.getSelfPhoneNumber(context)) ? MultiItemEntity.SELF : MultiItemEntity.OTHER;
            chatMessage.setUser(userInfo);
            String createdAt = chatMessage.getCreatedAt();
            Date messageDate = convertStringToDate(createdAt);
            switch (chatMessage.getMessageType()) {
                case "文本":
                    multiItemEntityList.add(new TextMessageItem(chatMessage.getContent(), messageDate, userSide, chatMessage.getUser()));
                    break;
                case "图片":
                    String filePath = AppProperties.SERVER_MEDIA_URL + chatMessage.getAttachment().getFilePath();
                    multiItemEntityList.add(new ImageMessageItem(filePath, messageDate, userSide, chatMessage.getUser()));
                    break;
                case "语音":
                    String voicePath = AppProperties.SERVER_MEDIA_URL + chatMessage.getAttachment().getFilePath();
                    multiItemEntityList.add(new VoiceMessageItem(voicePath, messageDate, userSide, chatMessage.getUser()));
                    break;
            }

        }

        Collections.sort(multiItemEntityList, new Comparator<MultiItemEntity>() {
            @Override
            public int compare(MultiItemEntity o1, MultiItemEntity o2) {
                Date date1 = o1.getItemDate();
                Date date2 = o2.getItemDate();
                return date1.compareTo(date2);
            }
        });

        return multiItemEntityList;
    }

    public void saveOrUpdateChatRoom(ChatMessage chatMessage) {
        if (chatMessage.getChatRoom() == null) return;
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(chatMessage.getChatRoomId());
        chatRoom.setType(chatMessage.getChatRoom().getType());
        chatRoom.setAvatar(chatMessage.getChatRoom().getAvatar());
        chatRoom.setName(chatMessage.getChatRoom().getName());
        chatRoom.setUpdatedAt(chatMessage.getChatRoom().getUpdatedAt());
        chatRoom.setCreatedAt(chatMessage.getChatRoom().getCreatedAt());

        chatRoom.setLastMessage(chatMessage.getContent());
        chatRoom.setLastMessageTime(chatMessage.getCreatedAt());
        chatRoom.setLastMessageType(chatMessage.getMessageType());
        ChatRoom existingChatRoom = LitePal.where("chatRoomId = ?", String.valueOf(chatMessage.getChatRoomId()))
                .findFirst(ChatRoom.class);
        if (existingChatRoom == null) {
            saveOrUpdateUserChatData();
        } else {
            chatRoom.saveOrUpdate("chatRoomId = ?", String.valueOf(chatMessage.getChatRoomId()));
        }
    }

    public void setLastViewTime(long currentTimeMillis) {
        context.getSharedPreferences("last_view_time_", Context.MODE_PRIVATE).edit().putLong("last_view_time_", currentTimeMillis).apply();
    }

    public long getLastViewTime() {
        return context.getSharedPreferences("last_view_time_", Context.MODE_PRIVATE).getLong("last_view_time_", 0);
    }


    private String convertMillisToDateString(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(millis));
    }

    public Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ChatRoom> getChatRoomList() {
        List<ChatRoom> chatRoomList =LitePal.order("lastMessageTime desc").find(ChatRoom.class);
        for (ChatRoom chatRoom : chatRoomList) {
            if (chatRoom.getType().equals("单人")) {
                ChatRoomMember chatRoomMember = getSingleChatRoomAvatar(chatRoom.getChatRoomId());
                chatRoom.setChatRoomMember(chatRoomMember);

            }
        }
        return chatRoomList;
    }

    public ChatRoomMember getSingleChatRoomAvatar(int chatRoomId) {
        ChatRoomMember chatRoomMember = null;
        List<ChatRoomMember> chatRoomMemberList = LitePal.where("chatRoomId = ?", String.valueOf(chatRoomId)).find(ChatRoomMember.class);
        String userId1 = chatRoomMemberList.get(0).getUserId() + "";
        String userId2 = chatRoomMemberList.get(1).getUserId() + "";

        if (userId1.equals(UserManager.getSelfPhoneNumber(context))) {
            chatRoomMember = chatRoomMemberList.get(1);
        } else {
            chatRoomMember = chatRoomMemberList.get(0);
        }
        return chatRoomMember;
    }

    public ChatRoom areUsersInSameSingleChatRoom(String myUserId, String targetUserId) {
        // 查询当前用户所在的所有单人聊天室
        List<ChatRoomMember> myChatRooms = LitePal
                .where("userId = ?", myUserId)
                .find(ChatRoomMember.class);

        for (ChatRoomMember myRoom : myChatRooms) {
            // 获取当前聊天室的详细信息
            ChatRoom chatRoom = LitePal
                    .where("chatRoomId = ?", myRoom.getChatRoomId() + "")
                    .findFirst(ChatRoom.class);
            // 检查聊天室类型是否为单人
            if ("单人".equals(chatRoom.getType())) {
                // 查询目标用户是否也在这个聊天室中
                int count = LitePal
                        .where("chatRoomId = ? and userId = ?",
                                String.valueOf(chatRoom.getChatRoomId()),
                                String.valueOf(targetUserId))
                        .count(ChatRoomMember.class);
                // 如果计数大于0，意味着两个用户在同一个单人聊天室中
                if (count > 0) {
                    return chatRoom;
                }
            }
        }
        // 如果没有找到共同的单人聊天室，则返回null
        return null;
    }

    public void updateLastReadTimeInThread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<ChatRoom> chatRoomList = getChatRoomList();
                for (ChatRoom chatRoom : chatRoomList) {
                    Call<Void> call = chatRoomApi.updateLastReadAt(selfUser.getPhoneNumber(), chatRoom.getChatRoomId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });
                }
            }
        }.start();
    }

    public List<ChatAttachment> getChatPictureList(String chatRoomId) {
        List<ChatAttachment> chatPictureList = new ArrayList<>();
        List<ChatMessage> chatMessageList = LitePal.where("chatRoomId = ? and messageType = ?", chatRoomId, "图片").find(ChatMessage.class,true);
        for (ChatMessage chatMessage : chatMessageList) {
            if(chatMessage.getAttachment()!= null) {
                chatMessage.getAttachment().setCreatedAt(TimeUtil.convertToTimestamp(chatMessage.getAttachment().getCreatedAt()) + "");
                chatPictureList.add(chatMessage.getAttachment());
            }
        }
        return chatPictureList;
    }

}
