package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.CodeSender;
import com.george.memoshareapp.utils.VerificationCountDownTimer;
import com.george.memoshareapp.view.MyCheckBox;

import es.dmoral.toasty.Toasty;

public class CodeLoginFragment extends Fragment implements View.OnClickListener{


    private EditText phone;
    private EditText fragment_et_code;
    private String phoneNumber;
    private String pwNumber;
    private MyCheckBox agreement;
    private View view;
    public  String codeReal="";
    private TextView getCode;
    private String et_code;
    private TextView register;
    private  static String TAG = "CodeLoginFragment";

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
        agreement = (MyCheckBox) getActivity().findViewById(R.id.rb_agree);
        register = (TextView) getActivity().findViewById(R.id.tv_yx_lg_regist);
        getCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        phoneNumber = this.phone.getText().toString().trim();
        switch (v.getId()){
            case R.id.tv_getCode:
                if (TextUtils.isEmpty(phoneNumber) || !isPhoneNumberValid(phoneNumber)){
                    Toasty.error(getContext(),"请输入正确的手机号",Toasty.LENGTH_SHORT,true).show();
                    return;
                }
                if(new UserManager(getContext()).isPhoneNumberRegistered(phoneNumber)==null){
                    Toasty.warning(getContext(), "该手机号未注册", Toast.LENGTH_SHORT,true).show();
                    return;
                }
                new VerificationCountDownTimer(getCode, 60000, 1000).start();
                if (!TextUtils.isEmpty(phoneNumber)){
                    codeReal = new CodeSender(getContext()).sendCode(phoneNumber);
                    if(codeReal != null){
                        Toasty.success(getContext(), "验证码已发送", Toast.LENGTH_SHORT,true).show();
                    }
                }
                break;
        }
    }
    public String getPhoneNumber() {
        return phone != null ? phone.getText().toString().trim() : "";
    }

    public String getEtCode() {
        return fragment_et_code != null ? fragment_et_code.getText().toString().trim() : "";
    }
    private boolean isPhoneNumberValid(String phoneNumber) {
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phoneNumber.matches(phoneRegex);
    }
}
