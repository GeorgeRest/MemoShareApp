package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.george.memoshareapp.R;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.CodeSender;
import com.george.memoshareapp.utils.PermissionUtils;
import com.george.memoshareapp.utils.VerificationCountDownTimer;
import com.george.memoshareapp.view.MyCheckBox;

import es.dmoral.toasty.Toasty;

public class RetrievePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_pw;
    private EditText et_pwAgain;
    private ImageButton bt_ok;
    private TextView tv_getCode;
    private TextView code;
    private String codePhone;
    private final long COUNTDOWN_TIME=60000;
    private boolean isClicked = false;  //
    private String codeReal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        initView();
        PermissionUtils.permissionsGranted(this);
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_pwAgain = (EditText) findViewById(R.id.et_pwAgain);
        bt_ok = (ImageButton) findViewById(R.id.bt_ok);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        code = (TextView) findViewById(R.id.et_verificationCode);
        bt_ok.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString().trim();
        String pw = et_pw.getText().toString().trim();
        String pwAgain = et_pwAgain.getText().toString().trim();
        String vcCode = code.getText().toString().trim();
        UserManager userManager = new UserManager(this);
        switch (v.getId()) {
            case R.id.tv_getCode:
                if (!TextUtils.isEmpty(phone) && isPhoneNumberValid(phone)) {
                    // 手机号找不到，即没被注册过，弹提示，请先注册
                    if(userManager.isPhoneNumberRegistered(phone) == null){
                        Toasty.warning(this, "该手机号未被注册过，请先注册", Toast.LENGTH_SHORT,true).show();
                        return;
                    }
                    //能找到手机号，做业务
                    codePhone = phone;
                    VerificationCountDownTimer timer = new VerificationCountDownTimer(tv_getCode, COUNTDOWN_TIME, 1000);
                    timer.start();
                    // 调用验证码方法传入手机号获取验证码
                    codeReal=new CodeSender(this).sendCode(phone);
                    if(codeReal != null){
                        Toasty.success(this, "验证码已发送", Toast.LENGTH_SHORT,true).show();
                    }
                } else {
                    Toasty.error(this, "请输入正确格式的手机号", Toast.LENGTH_SHORT,true).show();
                }
                break;

            case R.id.bt_ok:
                if(userManager.checkUserInfo(phone,pw,pwAgain,vcCode,codePhone)){
                    // 判断验证码是否正确
                    if (!vcCode.equals(codeReal)){
                        Toasty.error(this, "验证码输入错误", Toast.LENGTH_SHORT,true).show();
                        return;
                    }
                    // 修改密码
                    if (userManager.changePassword(phone,pw)){
                        Toasty.success(this, "修改密码成功，请登录", Toast.LENGTH_SHORT).show();
                    }else{
                        Toasty.error(this, "修改失败，请重试", Toast.LENGTH_SHORT,true).show();
                    }
                }

                break;

            case R.id.iv_back:
                finish();
                break;

        }
    }
        private boolean isPhoneNumberValid(String phoneNumber) {
            // 手机号正则表达式
            String phoneRegex = "^1[3-9]\\d{9}$";
            return phoneNumber.matches(phoneRegex);
        }
}