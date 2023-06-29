package com.george.memoshareapp.http.api;

public class LoginApi {

    private String phoneNumber;
    private String password;
    private String vcCode;



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
