package com.george.memoshareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;

import org.litepal.LitePal;

public class EditSignatureActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_edit_sign;
    private TextView tv_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_signature);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        et_edit_sign = (EditText) findViewById(R.id.et_edit_sign);

        iv_back.setOnClickListener(this);
        tv_complete.setOnClickListener(this);

        et_edit_sign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    tv_complete.setEnabled(false);
                    tv_complete.setTextColor(getResources().getColor(R.color.complete_normal));
                } else {
                    tv_complete.setEnabled(true);
                    tv_complete.setTextColor(getResources().getColor(R.color.complete));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_complete:
                String sign = et_edit_sign.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("result", sign);
                setResult(2, intent);
                User user = (User) getIntent().getSerializableExtra("user");
                LitePal.getDatabase();
                user.setSignature(sign);
                user.update(user.getId());
                finish();
                break;
        }
    }
}