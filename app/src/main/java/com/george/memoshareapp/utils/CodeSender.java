package com.george.memoshareapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;


public class CodeSender {
    private static final int REQUEST_SMS_PERMISSION = 1;

    private Context context;

    int length = 6;

    public CodeSender(Context context) {
        this.context = context;
    }

    public String sendCode(String codePhone) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
            return null;
        } else {
            return generateCode(codePhone);
        }

    }

    public String generateCode(String codePhone) {
        StringBuilder builder = new StringBuilder();
        String code = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code = builder.append(random.nextInt(10)).toString();
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(codePhone, null, "您的验证码是：" + code, null, null);

        Toast.makeText(context, "验证码已发送到您的手机，请注意查收", Toast.LENGTH_SHORT).show();
        return code;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults,String codePhone) {
        if (requestCode == REQUEST_SMS_PERMISSION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateCode(codePhone);
                } else {
                    Toast.makeText(context, "您需要授权发送短信权限才能发送验证码", Toast.LENGTH_SHORT).show();
                    System.out.println("------------");
                }

        }
    }
}
