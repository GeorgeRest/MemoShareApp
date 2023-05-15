package com.george.memoshareapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.george.memoshareapp.Fragment.CodeLoginFragment;
import com.george.memoshareapp.Fragment.PWLoginFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.PermissionUtils;
import com.george.memoshareapp.view.MyCheckBox;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        fragmentManager = getSupportFragmentManager();
        setDefaultSelection(0);
        PermissionUtils.permissionsGranted(this);
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

    }


    private void setDefaultSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                tv_pw_login.setTextColor(Color.BLACK);
                if (PwFragment == null) {
                    PwFragment = new PWLoginFragment();
                    transaction.add(R.id.frameLayout, PwFragment);
                } else {
                    transaction.show(PwFragment);
                }
                break;
            case 1:
            default:
                tv_code_login.setTextColor(Color.BLACK);
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
                Intent intent = new Intent(this,RetrievePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_yx_lg_regist:
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.ib_yx_lg_login:
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
            String phoneNumber = PwFragment.getPhoneNumber();
            String pwNumber = PwFragment.getPwNumber();

            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(pwNumber)) {
                Toasty.error(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                return;
            }

            if (agreement.isChecked()) {
                UserManager userManager = new UserManager(this);
                if(phoneNumber.matches(phoneRegex)){
                    if (userManager.queryUserInfo(phoneNumber, pwNumber)) {
                    Intent intent = new Intent(this, HomePageActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                      Toasty.success(this,"登录成功",Toast.LENGTH_SHORT).show();
                       finish();
                    }
                }else {
                    Toasty.warning(this,"请输入正确格式的手机号",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toasty.info(this, "请勾选同意协议", Toast.LENGTH_SHORT).show();
            }


        } else if (CodeFragment != null && CodeFragment.isVisible()) {
            String phoneNumber = CodeFragment.getPhoneNumber();
            String et_code = CodeFragment.getEtCode();

            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(et_code)) {
                Toasty.info(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                return;
            }
            if (agreement.isChecked()){
                UserManager userManager = new UserManager(this);
                if(phoneNumber.matches(phoneRegex)){
                    if (userManager.queryUser(phoneNumber) ) {
                        if( et_code.equals(CodeFragment.codeReal)){
                                Intent intent = new Intent(this, HomePageActivity.class);
                                intent.putExtra("phoneNumber",phoneNumber);
                                 startActivity(intent);
                            Toasty.success(this, "登录成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toasty.error(this, "验证码错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else {
                    Toasty.info(this,"请输入正确格式的手机号",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toasty.info(this, "请同意协议", Toast.LENGTH_SHORT).show();
            }

        }
    }
}





















