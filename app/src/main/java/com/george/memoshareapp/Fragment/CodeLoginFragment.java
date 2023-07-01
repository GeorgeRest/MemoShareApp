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
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.utils.VerificationCountDownTimer;
import com.george.memoshareapp.view.MyCheckBox;
import com.orhanobut.logger.Logger;

import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeLoginFragment extends Fragment implements View.OnClickListener{
    private LoadingDialog loadingDialog;
    private final long COUNTDOWN_TIME = 60000;
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
    private TextView tv_getCode;
    private boolean isSuccessLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_code_login, container, false);
        initView();

        return view;

    }
//    public interface OnCodeReceivedListener {
//        void onCodeReceived(String code);
//    }

    private void initView() {
        phone = (EditText) view.findViewById(R.id.et_phone);
        fragment_et_code = (EditText) view.findViewById(R.id.fragment_et_code);
        getCode = (TextView) view.findViewById(R.id.tv_getCode);
        agreement = (MyCheckBox) getActivity().findViewById(R.id.rb_agree);
        register = (TextView) getActivity().findViewById(R.id.tv_yx_lg_regist);
        tv_getCode = (TextView) view.findViewById(R.id.tv_getCode);
        getCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        phoneNumber = this.phone.getText().toString().trim();
        switch (v.getId()){
            case R.id.tv_getCode:
                if (!TextUtils.isEmpty(phoneNumber) && isPhoneNumberValid(phoneNumber)) {

                    VerificationCountDownTimer timer = new VerificationCountDownTimer(tv_getCode, COUNTDOWN_TIME, 1000);
                    timer.start();
                    SMSSDK.getVerificationCode("86", phoneNumber);//给手机发验证码

                } else {
                    Toasty.warning(getContext(), "请输入正确格式的手机号", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }
    public String getPhoneNumber() {
        return phone != null ? phone.getText().toString().trim() : "";
    }
    private Boolean userIsExit(String phoneNumber) {
        isSuccessLogin = false;
        User user = new User(phoneNumber);
        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<HttpData<User>> call = userServiceApi.loginVcCode(user);
        call.enqueue(new Callback<HttpData<User>>() {
            @Override
            public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
                loadingDialog = new LoadingDialog(getContext());
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    HttpData<User> data = response.body();
                    // 处理成功的情况
                    if (data.getCode() == 200) {
                        isSuccessLogin = true;
                        SMSSDK.getVerificationCode("86", phoneNumber);//给手机发验证码
                        VerificationCountDownTimer timer = new VerificationCountDownTimer(tv_getCode, COUNTDOWN_TIME, 1000);
                        timer.start();

                    } else if (data.getCode() == 401) {
                        Toasty.error(getContext(), "该用户未注册", Toast.LENGTH_SHORT, true).show();
                        isSuccessLogin = false;
                    }
                } else {
                    loadingDialog.endAnim();
                    loadingDialog.dismiss();
                    isSuccessLogin = false;
                    Toasty.warning(getContext(), "该手机号未注册", Toast.LENGTH_SHORT,true).show();
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

    public String getEtCode() {
        return fragment_et_code != null ? fragment_et_code.getText().toString().trim() : "";
    }
    private boolean isPhoneNumberValid(String phoneNumber) {
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phoneNumber.matches(phoneRegex);
    }

}
