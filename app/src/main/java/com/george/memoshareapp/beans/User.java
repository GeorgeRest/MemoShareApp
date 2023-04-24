package com.george.memoshareapp.beans;

import org.litepal.crud.LitePalSupport;

/**
 * @projectName: MemoShareApp
 * @package: com.george.memoshareApp.beans
 * @className: User
 * @author: George
 * @description: 用户表
 * @date: 2023/4/23 12:11
 * @version: 1.0
 */
public class User extends LitePalSupport {
    private int id;
    private String phoneNumber;
    private String password;
    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
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
}
