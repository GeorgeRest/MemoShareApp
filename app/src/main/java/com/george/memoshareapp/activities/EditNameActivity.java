package com.george.memoshareapp.activities;

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

public class EditNameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_edit_name;
    private TextView tv_complete;
    private TextView tv_number;
    private String newStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        et_edit_name = (EditText) findViewById(R.id.et_edit_name);
        tv_number = (TextView) findViewById(R.id.tv_number);

        iv_back.setOnClickListener(this);
        tv_complete.setOnClickListener(this);


        // 添加文本监听
        et_edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听，此处不需要做任何操作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文本改变的时候的监听
                if (s.length() > 20) {  // 判断字符数量是否超过20
                    // 如果超过20，则截取前20个字符，替换原输入
                    newStr = s.subSequence(0, 20).toString();
                    et_edit_name.setText(newStr);
                    // 重置光标位置到文本末尾
                    et_edit_name.setSelection(newStr.length());
                    tv_number.setText(newStr.length() + "/20");
                }else {
                    tv_number.setText(s.length() + "/20");
                }
                if(s.toString().trim().length()==0){
                    tv_complete.setEnabled(false);
                    tv_complete.setTextColor(getResources().getColor(R.color.complete_normal));
                } else {
                    tv_complete.setEnabled(true);
                    tv_complete.setTextColor(getResources().getColor(R.color.complete));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                String name = et_edit_name.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("result", name);
                setResult(1, intent);
                User user = (User) getIntent().getSerializableExtra("user");
                LitePal.getDatabase();
                user.setName(name);
                user.update(user.getId());
                finish();
                break;
        }
    }
}