package com.george.memoshareapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.george.memoshareapp.R;

public class ReleasePhotoImageDetailActivity extends BaseActivity {

    private ImageView release_detail_back;
    private ImageView release_detail_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_photo_detail);

        // 获取传递过来的图片的 URI
        String imageUriString = getIntent().getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);

        // 将图片显示在 ImageView 中
        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageURI(imageUri);

        Glide.with(this)
                .load(imageUri)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imageView);

        initView();
        initEvent();
    }

    private void initView(){
        release_detail_back = (ImageView) findViewById(R.id.release_detail_back);
        release_detail_button = (ImageView) findViewById(R.id.release_detail_button);
    }

    private void initEvent() {
        release_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /* 未完成按钮，逻辑未知**/
        release_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReleasePhotoImageDetailActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
