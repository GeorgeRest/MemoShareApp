package com.george.memoshareapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.LoginActivity;
import com.george.memoshareapp.activities.MainActivity;
import com.george.memoshareapp.activities.RegisterActivity;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.CodeSender;
import com.george.memoshareapp.view.MyCheckBox;

import org.litepal.LitePal;

import java.util.List;

public class CodeLoginFragment extends Fragment implements View.OnClickListener{


    private EditText phone;
    private EditText fragment_et_code;
    private ImageView login;
    private String phoneNumber;
    private String pwNumber;
    private MyCheckBox agreement;
    private View view;
    private  String codeReal;
    private TextView getCode;
    private String et_code;
    private TextView register;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_code_login, container, false);
        initView();

        return view;

    }

    private void initView() {
        phone = (EditText) view.findViewById(R.id.et_phone);
        fragment_et_code = (EditText) view.findViewById(R.id.fragment_et_code);
        getCode = (TextView) view.findViewById(R.id.tv_getCode);
        agreement = (MyCheckBox) getActivity().findViewById(R.id.rb_yx_lg_agreement);
        login = (ImageView) getActivity().findViewById(R.id.ib_yx_lg_login);
         register = (TextView) getActivity().findViewById(R.id.tv_yx_lg_regist);
        login.setOnClickListener(this);
        getCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        phoneNumber = this.phone.getText().toString().trim();
        switch (v.getId()){
            case R.id.tv_yx_lg_regist:
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_getCode:
                codeReal = new CodeSender(getContext()).sendCode(phoneNumber);
                break;
            case R.id.ib_yx_lg_login:
                UserManager userManager = new UserManager(getContext());
                et_code = this.fragment_et_code.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)||TextUtils.isEmpty(et_code)){
                    Toast.makeText(getActivity(), "请将信息填写完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userManager.queryUser(phoneNumber) && et_code.equals(codeReal) ){
                    if (agreement.isChecked()) {
                        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(this,);
//                        startActivity(intent);跳首页
                    } else {
                        Toast.makeText(getActivity(), "请勾选同意协议", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }
}
