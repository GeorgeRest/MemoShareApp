package com.george.memoshareapp.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

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
     *
     * @param phone
     * @param pw
     * @param pwAgain
     * @param vcCode
     * @param codePhone 提交时验证是否和获取验证码的手机一致
     * @return
     */
    public boolean checkUserInfo(String phone, String pw, String pwAgain, String vcCode, String codePhone) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(vcCode) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(pwAgain)) {
            Toasty.info(context, "请输入完整信息", Toast.LENGTH_SHORT,true).show();
            return false;
        }
        if (!phone.equals(codePhone) || !pw.equals(pwAgain)) {   //todo   验证码判断未加
            Toasty.error(context, "信息输入有误，请重新输入", Toast.LENGTH_SHORT,true).show();
            return false;
        }
        return true;
    }

    public static boolean  saveUserInfo(String phone, String pw){
        LitePal.getDatabase();
        User user = new User(phone, pw);
        return user.save();
    }

    public User isPhoneNumberRegistered(String phone){
        LitePal.getDatabase();
        return  LitePal.where("phonenumber=?", phone).findFirst(User.class);
    }

    public boolean changePassword(String phone,String pw) {
        LitePal.getDatabase();
        User user = LitePal.where("phonenumber=?", phone).findFirst(User.class);
        user.setPassword(pw);
        return user.save();
    }

    public  boolean queryUserInfo(String phone ,String pw){
        User user = LitePal.where("phonenumber=?", phone).findFirst(User.class);
        if(user==null){
            Toasty.info(context, "请先注册", Toast.LENGTH_SHORT,true).show();
            return false;
        }
        if (!user.getPassword().equals(pw)){
            Toasty.error(context, "密码错误", Toast.LENGTH_SHORT,true).show();
            return false;
        }
        return true;
    }
    public  boolean queryUser(String phone ){
        User user = LitePal.where("phonenumber=?", phone).findFirst(User.class);
        if(user==null){
            Toasty.info(context, "请先注册", Toast.LENGTH_SHORT,true).show();
            return false;
        }
        return true;
    }


}
