package com.george.memoshareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.george.memoshareapp.R;

import es.dmoral.toasty.Toasty;

public class TageActivity extends BaseActivity {

    private RelativeLayout rl_tag_back;
    private TextView tv_tag_finish;
    private EditText et_tag_input;
    public static final int TAG_RESULT_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tage);

        initView();

    }

    private void initView() {

        rl_tag_back = (RelativeLayout) findViewById(R.id.rl_tag_back);
        rl_tag_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TageActivity.this.finish();
            }
        });

        tv_tag_finish = (TextView) findViewById(R.id.tv_tag_finish);
        et_tag_input = (EditText) findViewById(R.id.et_tag_input);
        tv_tag_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_tag_input.getText().toString().trim();
                Intent intent = new Intent();
                if(text.length() == 0){
                    Toasty.info(TageActivity.this, "标签已清空").show();
                    intent.putExtra("tagResult","");
                    setResult(RESULT_OK,intent);
                    finish();
                } else if (text.length() >10) {
                    Toasty.info(TageActivity.this, "标签不能超过10个字").show();
                }else {
                    intent.putExtra("tagResult",text);
                    et_tag_input.setText("");
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

    }
}