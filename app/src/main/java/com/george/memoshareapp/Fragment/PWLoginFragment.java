package com.george.memoshareapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.LoginActivity;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.view.MyCheckBox;

public class PWLoginFragment extends Fragment{


    private EditText phone;
    private EditText pw;
    private MyCheckBox agreement;
    private View view;
    private static String TAG = "PWLoginFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pw_login, container, false);
        initView();
        return view;
    }

    private void initView() {
        phone = (EditText) view.findViewById(R.id.et_phone);
        pw = (EditText) view.findViewById(R.id.et_pw);
        agreement = (MyCheckBox) getActivity().findViewById(R.id.rb_yx_lg_agreement);
    }

    public String getPhoneNumber() {
        return phone != null ? phone.getText().toString().trim() : "";
    }

    public String getPwNumber() {
        return pw != null ? pw.getText().toString().trim() : "";
    }

}































