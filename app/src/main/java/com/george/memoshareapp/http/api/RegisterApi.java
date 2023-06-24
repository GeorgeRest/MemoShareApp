package com.george.memoshareapp.http.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.http.api
 * @className: UserApi
 * @author: George
 * @description: TODO
 * @date: 2023/6/21 22:06
 * @version: 1.0
 */
public class RegisterApi implements IRequestApi {
    private String phoneNumber;
    private String password;

    @NonNull
    @Override
    public String getApi() {
        return "user/register";
    }

    public RegisterApi setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public RegisterApi setPassword(String password) {
        this.password = password;
        return this;
    }
}
