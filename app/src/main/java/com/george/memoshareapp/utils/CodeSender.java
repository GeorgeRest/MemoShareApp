package com.george.memoshareapp.utils;

import android.content.Context;

import android.telephony.SmsManager;
import android.widget.Toast;


import java.util.Random;

import es.dmoral.toasty.Toasty;


public class CodeSender {

    private Context context;

    int length = 6;

    public CodeSender(Context context) {
        this.context = context;
    }

    public String sendCode(String codePhone) {
        StringBuilder builder = new StringBuilder();
        String code = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code = builder.append(random.nextInt(10)).toString();
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(codePhone, null, "【忆享】您的验证码是：" + code, null, null);
        return code;
    }

}
