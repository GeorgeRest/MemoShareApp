package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.george.memoshareapp.R;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_name;
    private RelativeLayout rl_signature;
    private RelativeLayout rl_gender;
    private RelativeLayout rl_birthday;
    private RelativeLayout rl_region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
    }

    private void initView() {
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        rl_region = (RelativeLayout) findViewById(R.id.rl_region);
        rl_name.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        rl_region.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_name:

                break;
            case R.id.rl_signature:

                break;
            case R.id.rl_gender:

                break;
            case R.id.rl_birthday:

                break;
            case R.id.rl_region:

                break;


        }

    }
}