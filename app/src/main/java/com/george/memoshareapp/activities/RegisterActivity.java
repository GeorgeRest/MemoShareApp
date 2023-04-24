package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.george.memoshareapp.R;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.VerificationCountDownTimer;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_pw;
    private EditText et_pwAgain;
    private ImageButton bt_register;
    private RadioButton rb_agree;
    private TextView tv_getCode;
    private TextView code;
    private String codePhone;
    private final long COUNTDOWN_TIME=60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_pwAgain = (EditText) findViewById(R.id.et_pwAgain);
        bt_register = (ImageButton) findViewById(R.id.bt_register);
        rb_agree = (RadioButton) findViewById(R.id.rb_agree);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        code = (TextView) findViewById(R.id.et_verificationCode);
        bt_register.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString().trim();
        String pw = et_pw.getText().toString().trim();
        String pwAgain = et_pwAgain.getText().toString().trim();
        String vcCode = code.getText().toString().trim();
        boolean isCodeValid = false;
        switch (v.getId()) {
            case R.id.tv_getCode:
                if (!TextUtils.isEmpty(phone) && isPhoneNumberValid(phone)) {
                    codePhone = phone; // 记录下发送验证码的手机号
                    VerificationCountDownTimer timer = new VerificationCountDownTimer(tv_getCode, 60000, 1000);
                    timer.start();
                    // todo  调用验证码方法传入手机号获取验证码
                    Toast.makeText(this, "验证码已发送到您的手机，请注意查收", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请输入正确格式的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_register:
                UserManager userManager = new UserManager(this);
                if (userManager.checkUserInfo(phone, pw, pwAgain, vcCode, codePhone)) {
                    //todo 还需判断验证码是否正确
                    if (userManager.saveUserInfo(phone, pw)){
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }


    private boolean isPhoneNumberValid(String phoneNumber) {
        // 手机号正则表达式
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phoneNumber.matches(phoneRegex);
    }


}