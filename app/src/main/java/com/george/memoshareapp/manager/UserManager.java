package com.george.memoshareapp.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.beans.User;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

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
        return user.save();
    }

    public User isPhoneNumberRegistered(String phone) {
        LitePal.getDatabase();
        return LitePal.where("phonenumber=?", phone).findFirst(User.class);
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


    // 关注一个用户
    public void followUser(User user, User target) {
        // 1. 更新User对象的following列表
        user.getFollowing().add(target);
        // 2. 更新被关注用户的followers列表
        target.getFollowers().add(user);
        // 3. 创建Relationship记录
        Relationship relationship = new Relationship();
        relationship.setUserId(user.getId());
        relationship.setFollowingId(target.getId());
        // 4. 将结果保存到数据库
        user.save();
        target.save();
        relationship.save();
        // 5. 检查是否需要添加到朋友列表
        if (target.getFollowing().contains(user)) {
            user.getFriends().add(target);
            target.getFriends().add(user);
            user.save();
            target.save();
        }
    }

    public void unfollowUser(User user, User target) {
        // 1. 更新User对象的following列表
        user.getFollowing().remove(target);

        // 2. 更新被关注用户的followers列表
        target.getFollowers().remove(user);

        // 3. 删除Relationship记录
        LitePal.deleteAll(Relationship.class, "userId = ? and followingId = ?", String.valueOf(user.getId()), String.valueOf(target.getId()));

        // 4. 将结果保存到数据库
        user.save();
        target.save();

        // 5. 检查是否需要从朋友列表移除
        if (!target.getFollowing().contains(user)) {
            user.getFriends().remove(target);
            target.getFriends().remove(user);
            user.save();
            target.save();
        }
    }


}
