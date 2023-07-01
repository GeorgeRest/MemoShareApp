package com.george.memoshareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.george.memoshareapp.BuildConfig;
import com.george.memoshareapp.Fragment.CodeLoginFragment;
import com.george.memoshareapp.Fragment.PWLoginFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.PermissionUtils;
import com.george.memoshareapp.view.MyCheckBox;

import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener , CodeLoginFragment.OnCodeReceivedListener {
    private EventHandler eventHandler;
    private TextView tv_pw_login;
    private TextView tv_code_login;
    private TextView forgetPW;
    private ImageButton login;
    private MyCheckBox agreement;
    private PWLoginFragment PwFragment;
    private CodeLoginFragment CodeFragment;
    private String phoneRegex = "^1[3-9]\\d{9}$";
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private TextView tv_yx_lg_regist;
    private TextView tv_forget_pw;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean isSuccessLogin;
    private LoadingDialog loadingDialog;
    private  String VcCode;
    private String phoneNumber;
    private String TAG="123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        fragmentManager = getSupportFragmentManager();
        setDefaultSelection(0);
        PermissionUtils.permissionsGranted(this);

        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {//走完第三方验证就走这个

                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.success(LoginActivity.this, "验证码发送成功", Toast.LENGTH_SHORT, true).show();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                User user = new User(phoneNumber);

                                UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
                                Call<HttpData<User>> call = userServiceApi.loginVcCode(user);
                                call.enqueue(new Callback<HttpData<User>>() {
                                    @Override
                                    public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
                                        if (loadingDialog != null) {
                                            loadingDialog.endAnim();
                                            loadingDialog.dismiss();
                                        }
                                        if (response.isSuccessful()) {
                                            HttpData<User> data = response.body();
                                            // 处理成功的情况
                                            if (data.getCode() == 200) {
                                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                                editor.putString("phoneNumber", phoneNumber);
                                                editor.putBoolean("isLogin", true);
                                                editor.apply();
                                                startActivity(intent);
                                                Toasty.success(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                Logger.d("登录成功");
                                                finish();
                                            } else if (data.getCode() == 401) {
                                                Toasty.error(LoginActivity.this, "该用户未注册", Toast.LENGTH_SHORT, true).show();
                                                isSuccessLogin = false;
                                            }
                                        } else {
                                            if (loadingDialog != null) {
                                                loadingDialog.endAnim();
                                                loadingDialog.dismiss();
                                            }
                                            Toasty.warning(LoginActivity.this, "未响应成功", Toast.LENGTH_SHORT,true).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<HttpData<User>> call, Throwable t) {
                                        // 处理网络错误等情况
                                        if (loadingDialog != null) {
                                            loadingDialog.endAnim();
                                            loadingDialog.dismiss();
                                        }
                                        Logger.d(t.getMessage());
                                    }
                                });

                            }
                        });
                    }
                } else {
                    final Throwable throwable = (Throwable) data;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                Toasty.error(LoginActivity.this, "验证码发送失败，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                Toasty.error(LoginActivity.this, "验证码输入错误，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                                loadingDialog.endAnim();
                                loadingDialog.dismiss();
                            }
                        }
                    });
                }
            }
        };


        SMSSDK.registerEventHandler(eventHandler);//注册这个短信

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            String signValidString = getSignValidString(packageInfo.signatures[0].toByteArray());
            Log.e("获取应用签名", BuildConfig.APPLICATION_ID + "__" + signValidString);
        } catch (Exception e) {
            Log.e("获取应用签名", "异常__" + e);
        }


    }

    private void initView() {
        tv_pw_login = (TextView) findViewById(R.id.tv_pw_login);
        tv_code_login = (TextView) findViewById(R.id.tv_code_login);
        tv_yx_lg_regist = (TextView) findViewById(R.id.tv_yx_lg_regist);
        tv_forget_pw = (TextView) findViewById(R.id.tv_forget_pw);
        forgetPW = (TextView) findViewById(R.id.tv_forget_pw);
        login = (ImageButton) findViewById(R.id.ib_yx_lg_login);
        agreement = (MyCheckBox) findViewById(R.id.rb_agree);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tv_pw_login.setOnClickListener(this);
        tv_code_login.setOnClickListener(this);
        login.setOnClickListener(this);
        tv_yx_lg_regist.setOnClickListener(this);
        tv_forget_pw.setOnClickListener(this);
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sp.edit();
    }


    private void setDefaultSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                tv_pw_login.setTextColor(Color.parseColor("#55377a"));
                if (PwFragment == null) {
                    PwFragment = new PWLoginFragment();
                    transaction.add(R.id.frameLayout, PwFragment);
                } else {
                    transaction.show(PwFragment);
                }
                break;
            case 1:
            default:
                tv_code_login.setTextColor(Color.parseColor("#55377a"));
                if (CodeFragment == null) {
                    CodeFragment = new CodeLoginFragment();
                    transaction.add(R.id.frameLayout, CodeFragment);
                } else {
                    transaction.show(CodeFragment);
                }
                break;


        }
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pw_login:
                setDefaultSelection(0);
                break;
            case R.id.tv_code_login:
                setDefaultSelection(1);
                break;
            case R.id.tv_forget_pw:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_yx_lg_regist:
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.ib_yx_lg_login:
                loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                loadingDialog.startAnim();
                handleLogin();


                break;
            default:
                break;
        }
    }

    private void clearSelection() {
        tv_pw_login.setTextColor(Color.WHITE);
        tv_code_login.setTextColor(Color.WHITE);

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (PwFragment != null) {
            transaction.hide(PwFragment);
        }
        if (CodeFragment != null) {
            transaction.hide(CodeFragment);
        }

    }

    private void handleLogin() {
        if (PwFragment != null && PwFragment.isVisible()) {
            phoneNumber = PwFragment.getPhoneNumber();
            String pwNumber = PwFragment.getPwNumber();
            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(pwNumber)) {
                Toasty.error(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                loadingDialog.endAnim();
                loadingDialog.dismiss();

                return;
            }
            if (agreement.isChecked()) {
                performLogin(phoneNumber, pwNumber);
            } else {
                Toasty.info(this, "请勾选同意协议", Toast.LENGTH_SHORT).show();
            }
        } else if (CodeFragment != null && CodeFragment.isVisible()) {
            phoneNumber = CodeFragment.getPhoneNumber();
            String et_code = CodeFragment.getEtCode();
            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(et_code)) {
                Toasty.info(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                return;
            }
            if (agreement.isChecked()) {
                SMSSDK.submitVerificationCode("86", phoneNumber, et_code);//第三方服务器验证，手机号和验证码

//                performLoginVcCode(phoneNumber, et_code);

            } else {
                Toasty.info(this, "请同意协议", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private Boolean performLogin(String phoneNumber, String password) {
        isSuccessLogin = false;
        User user = new User(phoneNumber, password);
        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<HttpData<User>> call = userServiceApi.loginUser(user);
        call.enqueue(new Callback<HttpData<User>>() {
            @Override
            public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    HttpData<User> data = response.body();
                    // 处理成功的情况
                    if (data.getCode() == 200) {
                        Toasty.success(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT, true).show();
                        isSuccessLogin = true;

                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                        editor.putString("phoneNumber", phoneNumber);
                        editor.putBoolean("isLogin", true);
                        editor.apply();
                        startActivity(intent);
                        Toasty.success(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Logger.d("登录成功");

                        finish();
                    } else if (data.getCode() == 401) {
                        Toasty.error(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT, true).show();
                        isSuccessLogin = false;
                        Logger.d("登录失败");
                    }

                } else {
                    loadingDialog.endAnim();
                    loadingDialog.dismiss();
                    Toasty.error(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT, true).show();
                    isSuccessLogin = false;
                    Logger.d("登录失败");
                }

            }

            @Override
            public void onFailure(Call<HttpData<User>> call, Throwable t) {
                // 处理网络错误等情况
                Logger.d(t.getMessage());
            }
        });
        return isSuccessLogin;
    }


    private void performLoginVcCode(String phoneNumber, String vcCode) {
        System.out.println("====66==========");
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {//走完第三方验证就走这个
                System.out.println(data+"====77=========="+result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.success(LoginActivity.this, "验证码发送成功", Toast.LENGTH_SHORT, true).show();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                User user = new User(phoneNumber);
                                UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
                                Call<HttpData<User>> call = userServiceApi.loginVcCode(user);
                                call.enqueue(new Callback<HttpData<User>>() {
                                    @Override
                                    public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
                                        loadingDialog.endAnim();
                                        loadingDialog.dismiss();
                                        if (response.isSuccessful()) {
                                            HttpData<User> data = response.body();
                                            // 处理成功的情况
                                            if (data.getCode() == 200) {
                                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                                editor.putString("phoneNumber", phoneNumber);
                                                editor.putBoolean("isLogin", true);
                                                editor.apply();
                                                startActivity(intent);
                                                Toasty.success(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                Logger.d("登录成功");
                                                finish();
                                            } else if (data.getCode() == 401) {
                                                Toasty.error(LoginActivity.this, "该用户未注册", Toast.LENGTH_SHORT, true).show();
                                                isSuccessLogin = false;
                                            }
                                        } else {
                                            loadingDialog.endAnim();
                                            loadingDialog.dismiss();
                                            Toasty.warning(LoginActivity.this, "未响应成功", Toast.LENGTH_SHORT,true).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<HttpData<User>> call, Throwable t) {
                                        // 处理网络错误等情况
                                        Logger.d(t.getMessage());
                                    }
                                });

                            }
                        });
                    }
                } else {
                    final Throwable throwable = (Throwable) data;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                Toasty.error(LoginActivity.this, "验证码发送失败，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                Toasty.error(LoginActivity.this, "验证码输入错误，原因：" + throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    public  String getVcCode(String vcCode){
        this.VcCode=vcCode;
        return VcCode;
    }
    @Override
    public void onCodeReceived(String code) {
        getVcCode(code);
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

}





