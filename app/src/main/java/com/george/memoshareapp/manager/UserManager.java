package com.george.memoshareapp.manager;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.beans.User;

import org.litepal.LitePal;

import es.dmoral.toasty.Toasty;

/**
 * @projectName: MemoShare
 * @package: com.george.memoshareApp.manager
 * @className: UserManager
 * @author: George
 * @description: TODO
 * @date: 2023/4/23 14:11
 * @version: 1.0
 */
public class UserManager {
    private Context context;

    public UserManager(Context context) {
        this.context = context;
    }

    /**
     * @param phone
     * @param pw
     * @param pwAgain
     * @param vcCode
     * @param codePhone 提交时验证是否和获取验证码的手机一致
     * @return
     */
    public boolean checkUserInfo(String phone, String pw, String pwAgain, String vcCode, String codePhone) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(vcCode) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(pwAgain)) {
            Toasty.info(context, "请输入完整信息", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (!phone.equals(codePhone) || !pw.equals(pwAgain)) {   //todo   验证码判断未加
            Toasty.error(context, "信息输入有误，请重新输入", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    public static boolean saveUserInfo(String phone, String pw) {
        LitePal.getDatabase();
        User user = new User(phone, pw);
        user.setRegion("中国");
        user.setGender("男");
        user.setSignature("暂时还没有简介");
        if (user.save()) {
            long userId = user.getId();
            user.generateDefaultName((int) userId);
            user.update(user.getId());
            return true;
        }
        return false;
    }

    public User isPhoneNumberRegistered(String phone) {
        LitePal.getDatabase();
        return LitePal.select("id")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
    }

    public boolean changePassword(String phone, String pw) {
        LitePal.getDatabase();
        User user = LitePal.select("id, phoneNumber, password")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
        user.setPassword(pw);
        return user.save();
    }

    public boolean queryUserInfo(String phone, String pw) {
        User user = LitePal.select("id, phoneNumber, password")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
        if (user == null) {
            Toasty.info(context, "请先注册", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (!user.getPassword().equals(pw)) {
            Toasty.error(context, "密码错误", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    public boolean queryUser(String phone) {
        User user = LitePal.select("id, phoneNumber, password")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
        if (user == null) {
            Toasty.info(context, "请先注册", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }
    // 检查initiator是否关注了target
    public boolean isFollowing(User initiator, User target) {
        long count = LitePal.where("initiatorNumber = ? and targetNumber = ? and (relationshipStatus = ? or relationshipStatus = ?)",
                        String.valueOf(initiator.getPhoneNumber()),
                        String.valueOf(target.getPhoneNumber()),
                        String.valueOf(Relationship.ATTENTION_STATUS),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);

        return count > 0;
    }
    // 当前用户关注其他用户
    public void followUser(User initiator, User target) {
        Relationship relationship = new Relationship();
        relationship.setInitiatorNumber(initiator.getPhoneNumber());
        relationship.setTargetNumber(target.getPhoneNumber());
        relationship.setRelationshipStatus(Relationship.ATTENTION_STATUS);
        relationship.save();

        // 检查是否已经满足成为朋友的条件
        if (isMutualFollow(initiator, target)) {
            becomeFriends(initiator, target);
        }
    }

    // 当前用户取消关注
    public void unfollowUser(User initiator, User target) {
        LitePal.deleteAll(Relationship.class, "initiatorNumber = ? and targetNumber = ? and relationshipStatus = ?", String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()), String.valueOf(Relationship.ATTENTION_STATUS));

        // 检查是否需要解除朋友关系
        if (!isMutualFollow(initiator, target)) {
            endFriendship(initiator, target);
        }
    }
    public User findUserByPhoneNumber(String phoneNumber) {
        User users = LitePal.select(" phoneNumber")
                .where("phoneNumber = ?", phoneNumber)
                .findFirst(User.class);
        if (users != null) {
            return users;
        } else {
            return null;
        }
    }

    // 检查是否满足互相关注的条件
    private boolean isMutualFollow(User initiator, User target) {
        long count1 = LitePal.where("initiatorNumber = ? and targetNumber = ? and relationshipStatus = ?", String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()), String.valueOf(Relationship.ATTENTION_STATUS)).count(Relationship.class);
        long count2 = LitePal.where("initiatorNumber = ? and targetNumber = ? and relationshipStatus = ?", String.valueOf(target.getPhoneNumber()), String.valueOf(initiator.getPhoneNumber()), String.valueOf(Relationship.ATTENTION_STATUS)).count(Relationship.class);

        return count1 > 0 && count2 > 0;
    }

    // 两个用户互相关注，成为朋友
    private void becomeFriends(User initiator, User target) {
        // 需要将之前的关注状态更新为朋友状态
        ContentValues values = new ContentValues();
        values.put("relationshipStatus", Relationship.FRIEND_STATUS);
        LitePal.updateAll(Relationship.class, values, "initiatorNumber = ? and targetNumber = ?", String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()));
        LitePal.updateAll(Relationship.class, values, "initiatorNumber = ? and targetNumber = ?", String.valueOf(target.getPhoneNumber()), String.valueOf(initiator.getPhoneNumber()));
    }

    // 解除朋友关系
    private void endFriendship(User initiator, User target) {
        // 只要有一方取消关注，就解除朋友关系
        ContentValues values = new ContentValues();
        values.put("relationshipStatus", Relationship.ATTENTION_STATUS);
        LitePal.updateAll(Relationship.class, values, "initiatorNumber = ? and targetNumber = ?", String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()));
        LitePal.updateAll(Relationship.class, values, "initiatorNumber = ? and targetNumber = ?", String.valueOf(target.getPhoneNumber()), String.valueOf(initiator.getPhoneNumber()));
    }

    public long countFollowing(User user) {
        return LitePal.where("initiatorNumber = ? and (relationshipStatus = ? or relationshipStatus = ?)",
                        String.valueOf(user.getPhoneNumber()),
                        String.valueOf(Relationship.ATTENTION_STATUS),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);
    }


    public long countFans (User user) {
        return LitePal.where("targetNumber = ? and (relationshipStatus = ? or relationshipStatus = ?)",
                        String.valueOf(user.getPhoneNumber()),
                        String.valueOf(Relationship.ATTENTION_STATUS),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);
    }


    public long countFriends(User user) {
        return LitePal.where("initiatorNumber = ? and relationshipStatus = ?",
                        String.valueOf(user.getPhoneNumber()),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);
    }
}
