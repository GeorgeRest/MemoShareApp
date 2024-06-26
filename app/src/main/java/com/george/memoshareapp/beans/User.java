package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: MemoShareApp
 * @package: com.george.memoshareApp.beans
 * @className: User
 * @author: George
 * @description: 用户表
 * @date: 2023/4/23 12:11
 * @version: 1.0
 */
public class User extends LitePalSupport implements Serializable {
    private long id;
    private String phoneNumber;
    private String password;
    private String name; //忆享用户+id 例如：忆享用户1  需重新注册
    private String headPortraitPath; //默认头像 如果用户没传头像 所有用户使用默认头像 背景同理
    private String signature;   //暂时还没有简介(这是一句话)
    private String gender; //男
    private String birthday;
    private String region; //中国
    private String BackGroundPath; //默认背景
    private ChatMessage ChatMessage;
    private int  isFriend;

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    private List<Post> likePosts = new ArrayList<>();



    public User(String phoneNumber, String name, String signature, String gender, String birthday, String region) {
        this.phoneNumber=phoneNumber;
        this.name = name;
        this.signature = signature;
        this.gender = gender;
        this.birthday = birthday;
        this.region = region;
    }

    public User() {
    }


    public String getHeadPortraitPath() {
        return headPortraitPath;
    }

    public void setHeadPortraitPath(String headPortraitPath) {
        this.headPortraitPath = headPortraitPath;
    }

    public String getBackGroundPath() {
        return BackGroundPath;
    }

    public void setBackGroundPath(String backGroundPath) {
        BackGroundPath = backGroundPath;
    }

    public long getId() {
        return id;
    }

    public List<Post> getLikePosts() {
        return likePosts;
    }

    public void setLikePosts(List<Post> likePosts) {
        this.likePosts = likePosts;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", headPortraitPath='" + headPortraitPath + '\'' +
                ", signature='" + signature + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", region='" + region + '\'' +
                ", BackGroundPath='" + BackGroundPath + '\'' +
                ", likePosts=" + likePosts +
                '}';
    }

    public com.george.memoshareapp.beans.ChatMessage getChatMessage() {
        return ChatMessage;
    }

    public void setChatMessage(com.george.memoshareapp.beans.ChatMessage chatMessage) {
        ChatMessage = chatMessage;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return phoneNumber != null ? phoneNumber.equals(user.phoneNumber) : user.phoneNumber == null;
    }

    @Override
    public int hashCode() {
        return phoneNumber != null ? phoneNumber.hashCode() : 0;
    }
}
