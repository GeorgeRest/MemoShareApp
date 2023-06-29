package com.george.memoshareapp.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.BuildConfig;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.http.api.UserApiService;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.utils.CodeSender;
import com.george.memoshareapp.utils.VerificationCountDownTimer;
import com.george.memoshareapp.view.MyCheckBox;
import com.orhanobut.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_pw;
    private EditText et_pwAgain;
    private ImageButton bt_register;
    private MyCheckBox rb_agree;
    private TextView tv_getCode;
    private TextView code;
    private String codePhone;
    private final long COUNTDOWN_TIME = 60000;
    private boolean isClicked = false;
    private CodeSender codeSender;
    private String codeReal;
    private ImageView back;
    private EventHandler eventHandler;
    private String phone;
    private String pw;
    private String pwAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            String signValidString = getSignValidString(packageInfo.signatures[0].toByteArray());
            Log.e("获取应用签名", BuildConfig.APPLICATION_ID + "__" + signValidString);
        } catch (Exception e) {
            Log.e("获取应用签名", "异常__" + e);
        }
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {//走完第三方验证就走这个
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.success(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT, true).show();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);
                                loadingDialog.show();

                                UserApiService apiService = RetrofitManager.getInstance().create(UserApiService.class);
                                User user = new User(phone, pw);
                                Call<HttpData<User>> call = apiService.uploadUser(user);
                                call.enqueue(new Callback<HttpData<User>>() {
                                    @Override
                                    public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
                                        loadingDialog.endAnim(); // 请求成功，结束加载框的动画
                                        loadingDialog.dismiss(); // 隐藏加载框
                                        if (response.isSuccessful()) {
                                            HttpData<User> apiResponse = response.body();
                                            Logger.d(apiResponse.getData());
                                            if (apiResponse != null && apiResponse.getCode() == 200) {
                                                Toasty.success(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT, true).show();
                                                finish();
                                            } else if (apiResponse != null && apiResponse.getCode() == 201) {
                                                Toasty.info(RegisterActivity.this, "该用户已注册，请登录", Toast.LENGTH_SHORT, true).show();
                                            }
                                            }
                                        }

                                    @Override
                                    public void onFailure(Call<HttpData<User>> call, Throwable t) {
                                        loadingDialog.endAnim(); // 请求成功，结束加载框的动画
                                        loadingDialog.dismiss(); // 隐藏加载框
                                        Logger.d(t.getMessage());
                                    }
                                });

                                Toasty.success(RegisterActivity.this, "验证码输入正确", Toast.LENGTH_SHORT, true).show();

                            }
                        });
                    }

                } else {

                    final Throwable throwable = (Throwable) data;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                                Toasty.error(RegisterActivity.this, "验证码发送失败，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                Toasty.error(RegisterActivity.this, "验证码输入错误，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                                return;
                            }
                        }
                    });
                }
            }
        };

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            String signValidString = getSignValidString(packageInfo.signatures[0].toByteArray());
            Log.e("获取应用签名", BuildConfig.APPLICATION_ID + "__" + signValidString);
        } catch (Exception e) {
            Log.e("获取应用签名", "异常__" + e);
        }



        SMSSDK.registerEventHandler(eventHandler);//注册这个短信

    }


    private String getSignValidString( byte[] paramArrayOfByte) throws NoSuchAlgorithmException {
        MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
        localMessageDigest.update(paramArrayOfByte);
        return toHexString(localMessageDigest.digest());
    }
    public String toHexString(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfByte.length) {
                return localStringBuilder.toString();
            }
            String str = Integer.toString(0xFF & paramArrayOfByte[i], 16);
            if (str.length() == 1) {
                str = "0" + str;
            }
            localStringBuilder.append(str);
        }
    }


    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_pwAgain = (EditText) findViewById(R.id.et_pwAgain);
        bt_register = (ImageButton) findViewById(R.id.bt_register);
        rb_agree = (MyCheckBox) findViewById(R.id.rb_agree);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        code = (TextView) findViewById(R.id.fragment_et_code);
        back = (ImageView) findViewById(R.id.iv_back_rg);
        back.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        phone = et_phone.getText().toString().trim();
        pw = et_pw.getText().toString().trim();
        pwAgain = et_pwAgain.getText().toString().trim();
        String vcCode = code.getText().toString().trim();
        switch (v.getId()) {
            case R.id.tv_getCode:
                if (!TextUtils.isEmpty(phone) && isPhoneNumberValid(phone)) {

                    VerificationCountDownTimer timer = new VerificationCountDownTimer(tv_getCode, COUNTDOWN_TIME, 1000);
                    timer.start();

                    SMSSDK.getVerificationCode("86", phone);//给手机发验证码

                } else {
                    Toasty.warning(this, "请输入正确格式的手机号", Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.bt_register:
                if (!pw.equals(pwAgain)) {
                    Toasty.warning(this, "两次密码输入不一致", Toast.LENGTH_SHORT, true).show();
                    return;
                }
//                if (!rb_agree.isChecked()) {
//                    Toasty.warning(this, "请阅读并同意《用户协议》", Toast.LENGTH_SHORT, true).show();
//                    return;
//                }

                SMSSDK.submitVerificationCode("86", phone, vcCode);//第三方服务器验证，手机号和验证码


                break;
            case R.id.iv_back_rg:
                finish();
                break;

        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phoneNumber.matches(phoneRegex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

}