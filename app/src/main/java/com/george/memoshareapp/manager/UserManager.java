package com.george.memoshareapp.manager;

import android.content.Context;

import com.george.memoshareapp.beans.User;

import org.litepal.LitePal;

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

    public boolean  saveUserInfo(String phone, String pw){
        LitePal.getDatabase();
        User user = new User(phone, pw);
        return user.save();
    }

}
