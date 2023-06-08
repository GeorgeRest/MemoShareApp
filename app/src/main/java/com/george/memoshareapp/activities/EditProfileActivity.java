package com.george.memoshareapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.george.memoshareapp.R;
import com.george.memoshareapp.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_name;
    private RelativeLayout rl_signature;
    private RelativeLayout rl_gender;
    private RelativeLayout rl_birthday;
    private RelativeLayout rl_region;
    private CircleImageView camera;
    private String destPath;


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
        camera = (CircleImageView) findViewById(R.id.camera);
        rl_name.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        rl_region.setOnClickListener(this);
        camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.camera:
                PictureSelector.create(this)
                        .openGallery(SelectMimeType.ofImage())
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .setMaxSelectNum(1)
                        .isEmptyResultReturn(true)
                        .isMaxSelectEnabledMask(true)

                        .setCropEngine(new CropFileEngine() {
                            @Override
                            public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
                                UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
                                destPath = destinationUri+"";
                                System.out.println("-------------" + srcUri+"-----"+destinationUri+"-----"+dataSource+"-----"+requestCode);
                                uCrop.setImageEngine(new UCropImageEngine() {
                                    @Override
                                    public void loadImage(Context context, String url, ImageView imageView) {
                                    }

                                    @Override
                                    public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                                    }
                                });

                                UCrop.Options options = new UCrop.Options();
                                options.setCircleDimmedLayer(true);
                                options.setShowCropFrame(false);
                                options.setShowCropGrid(false);
                                uCrop.withOptions(options);
                                uCrop.start(fragment.getActivity(), fragment, requestCode);
                            }

                        })
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {
                                System.out.println("-------------" + result.get(0).getRealPath());

                                Glide.with(EditProfileActivity.this).load(destPath).into(camera);

                            }

                            @Override
                            public void onCancel() {
                            }
                        });

                break;
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