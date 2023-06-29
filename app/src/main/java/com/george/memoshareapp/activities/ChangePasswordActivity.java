package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.george.memoshareapp.BuildConfig;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.http.api.UserApiService;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.CodeSender;
import com.george.memoshareapp.utils.PermissionUtils;
import com.george.memoshareapp.utils.VerificationCountDownTimer;
import com.orhanobut.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_pw;
    private EditText et_pwAgain;
    private ImageButton bt_ok;
    private TextView tv_getCode;
    private TextView code;
    private String codePhone;
    private final long COUNTDOWN_TIME=60000;
    private boolean isClicked = false;
    private String codeReal;
    private EventHandler eventHandler;
    private String phone;
    private String pw;
    private String pwAgain;
    private String vcCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        init();
        PermissionUtils.permissionsGranted(this);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            String signValidString = getSignValidString(packageInfo.signatures[0].toByteArray());
            Log.e("获取应用签名", BuildConfig.APPLICATION_ID + "__" + signValidString);
        } catch (Exception e) {
            Log.e("获取应用签名", "异常__" + e);
        }

        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.success(ChangePasswordActivity.this, "验证码发送成功", Toast.LENGTH_SHORT, true).show();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                LoadingDialog loadingDialog = new LoadingDialog(ChangePasswordActivity.this);
//                                loadingDialog.show();
//
//                                UserApiService apiService = RetrofitManager.getInstance().create(UserApiService.class);
//                                User user = new User(phone, pw);
//                                Call<HttpData<User>> call = apiService.changePassword(user);
//                                call.enqueue(new Callback<HttpData<User>>() {
//                                    @Override
//                                    public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
//                                        loadingDialog.endAnim(); // 请求成功，结束加载框的动画
//                                        loadingDialog.dismiss(); // 隐藏加载框
//                                        if (response.isSuccessful()) {
//                                            Toasty.success(ChangePasswordActivity.this, "修改密码成功，请登录", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toasty.error(ChangePasswordActivity.this, "修改失败，请重试", Toast.LENGTH_SHORT,true).show();
//                                        }
//                                        if (response.isSuccessful()) {
//                                            HttpData<User> apiResponse = response.body();
//                                            Logger.d(apiResponse.getData());
//                                            if (apiResponse != null && apiResponse.getCode() == 201) {
//                                                Toasty.success(ChangePasswordActivity.this, "修改密码成功，请登录", Toast.LENGTH_SHORT).show();
//                                                finish();
//                                            } else if (apiResponse != null && apiResponse.getCode() == 200) {
//                                                Toasty.info(ChangePasswordActivity.this, "该手机号未注册，请先注册", Toast.LENGTH_SHORT, true).show();
//                                            }
//                                        }else {
//                                            Toasty.error(ChangePasswordActivity.this, "修改失败，请重试", Toast.LENGTH_SHORT,true).show();
//                                        }
//                                    }
//
////                                    @Override
////                                    public void onFailure(Call<HttpData<User>> call, Throwable t) {
////                                        Toasty.error(ChangePasswordActivity.this, "网络错误，请重试", Toast.LENGTH_SHORT,true).show();
////                                    }
//                                    @Override
//                                    public void onFailure(Call<HttpData<User>> call, Throwable t) {
//                                        loadingDialog.endAnim(); // 请求成功，结束加载框的动画
//                                        loadingDialog.dismiss(); // 隐藏加载框
//                                        Logger.d(t.getMessage());
//                                    }
//                                });
//
//                                Toasty.success(ChangePasswordActivity.this, "验证码输入正确", Toast.LENGTH_SHORT, true).show();

                            }
                        });
                    }

                } else {
                    final Throwable throwable = (Throwable) data;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                                Toasty.error(ChangePasswordActivity.this, "验证码发送失败，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                Toasty.error(ChangePasswordActivity.this, "验证码输入错误，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                                return;
                            }
                        }
                    });
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void init() {
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
        phone = et_phone.getText().toString().trim();
        pw = et_pw.getText().toString().trim();
        pwAgain = et_pwAgain.getText().toString().trim();
        vcCode = code.getText().toString().trim();
        switch (v.getId()) {
            case R.id.tv_getCode:
                if (!TextUtils.isEmpty(phone) && isPhoneNumberValid(phone)) {

                    VerificationCountDownTimer timer = new VerificationCountDownTimer(tv_getCode, COUNTDOWN_TIME, 1000);
                    timer.start();

                    SMSSDK.getVerificationCode("86", phone);

                } else {
                    Toasty.warning(this, "请输入正确格式的手机号", Toast.LENGTH_SHORT, true).show();
                }
                break;

            case R.id.bt_ok:
//                if(userManager.checkUserInfo(phone,pw,pwAgain,vcCode,codePhone)){
                    // 判断验证码是否正确
//                    if (!vcCode.equals(codeReal)){
//                        Toasty.error(this, "验证码输入错误", Toast.LENGTH_SHORT,true).show();
//                        return;
//                    }
//                    // 修改密码
//                    if (userManager.changePassword(phone,pw)){
//                        Toasty.success(this, "修改密码成功，请登录", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toasty.error(this, "修改失败，请重试", Toast.LENGTH_SHORT,true).show();
//                    }
//                }
//                if(userManager.checkUserInfo(phone,pw,pwAgain,vcCode,codePhone)){
                    // 判断验证码是否正确
//                    if (!vcCode.equals(codeReal)){
//                        Toasty.error(this, "验证码输入错误", Toast.LENGTH_SHORT,true).show();
//                        return;
//                    }
                    // 修改密码
//                UserApiService apiService = RetrofitManager.getInstance().create(UserApiService.class);
//                User user = new User(phone, pw);
//                Call<HttpData<User>> call = apiService.changePassword(user);
//                call.enqueue(new Callback<HttpData<User>>() {
//                        @Override
//                        public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
//                            if (response.isSuccessful()) {
//                                Toasty.success(ChangePasswordActivity.this, "修改密码成功，请登录", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toasty.error(ChangePasswordActivity.this, "修改失败，请重试", Toast.LENGTH_SHORT,true).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<HttpData<User>> call, Throwable t) {
//                            Toasty.error(ChangePasswordActivity.this, "网络错误，请重试", Toast.LENGTH_SHORT,true).show();
//                        }
//                    });


                if (!pw.equals(pwAgain)) {
                    Toasty.warning(this, "两次密码输入不一致", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", phone, vcCode);
                break;

            case R.id.iv_back_rg:
                finish();
                break;

        }
    }
        private boolean isPhoneNumberValid(String phoneNumber) {
            // 手机号正则表达式
            String phoneRegex = "^1[3-9]\\d{9}$";
            return phoneNumber.matches(phoneRegex);
        }

    private String getSignValidString(byte[] paramArrayOfByte) throws NoSuchAlgorithmException {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}