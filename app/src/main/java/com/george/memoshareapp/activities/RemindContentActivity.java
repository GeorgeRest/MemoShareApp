package com.george.memoshareapp.activities;

import static com.george.memoshareapp.activities.RemindActivity.RESULT_CODE_REMIND_CONTENT;

import androidx.appcompat.app.AppCompatActivity;

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

public class RemindContentActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;
    private EditText et_content;
    private TextView tv_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_content);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        et_content = (EditText) findViewById(R.id.et_content);

        iv_back.setOnClickListener(this);
        tv_complete.setOnClickListener(this);

        et_content.addTextChangedListener(new TextWatcher() {
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
                String sign = et_content.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("result", sign);
                setResult(RESULT_CODE_REMIND_CONTENT, intent);
                finish();
                break;
        }
    }
}