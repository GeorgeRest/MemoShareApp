package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.memoshareapp.Fragment.CodeLoginFragment;
import com.george.memoshareapp.Fragment.PWLoginFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.utils.PermissionUtils;
import com.george.memoshareapp.view.MyCheckBox;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_pw_login;
    private TextView tv_code_login;
    private TextView forgetPW;
    private ImageView login;
    private MyCheckBox agreement;
    private PWLoginFragment PwFragment;
    private CodeLoginFragment CodeFragment;

    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;


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
        forgetPW = (TextView) findViewById(R.id.tv_forget_pw);
        login = (ImageView) findViewById(R.id.ib_yx_lg_login);
        agreement = (MyCheckBox) findViewById(R.id.rb_yx_lg_agreement);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tv_pw_login.setOnClickListener(this);
        tv_code_login.setOnClickListener(this);
    }


    private void setDefaultSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case 0:
                tv_pw_login.setTextColor(Color.BLACK);
                if(PwFragment == null){
                    PwFragment=new PWLoginFragment();
                    transaction.add(R.id.frameLayout,PwFragment);
                }else {
                    transaction.show(PwFragment);
                }
                break;
            case 1:
                default:
                tv_code_login.setTextColor(Color.BLACK);
                if (CodeFragment==null){
                    CodeFragment=new CodeLoginFragment();
                    transaction.add(R.id.frameLayout,CodeFragment);
                }else{
                    transaction.show(CodeFragment);
                }
                break;

        }
        transaction.commit();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_pw_login:
                setDefaultSelection(0);
                break;
            case R.id.tv_code_login:
                setDefaultSelection(1);
            default:
                break;
        }
    }

    private void clearSelection() {
        tv_pw_login.setTextColor(Color.WHITE);
        tv_code_login.setTextColor(Color.WHITE);

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (PwFragment!=null){
            transaction.hide(PwFragment);
        }
        if (CodeFragment!=null){
            transaction.hide(CodeFragment);
        }

    }

}





















