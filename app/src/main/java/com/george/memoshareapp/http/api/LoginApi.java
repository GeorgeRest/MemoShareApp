package com.george.memoshareapp.http.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

public class LoginApi implements IRequestApi {

    private String phoneNumber;
    private String password;
    private String vcCode;

    @NonNull
    @Override
    public String getApi() {
        return "user/login";
    }

    public LoginApi setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public LoginApi setPassword(String password) {
        this.password = password;
        return this;
    }public LoginApi setVcCode(String vcCode) {
        this.vcCode = vcCode;
        return this;
    }
}
