package com.george.memoshareapp.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.george.memoshareapp.Fragment.MyChatBarFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ChatAdapter;
import com.george.memoshareapp.beans.ImageMessageItem;
import com.george.memoshareapp.beans.TextMessageItem;
import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.interfaces.SendListener;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.utils.MediaUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements SendListener {

    private FragmentManager manager;
    private Fragment myChatBar;
    private final int CHOOSE_PIC_REQUEST_CODE = 3;


    private List<String> mImageList = new ArrayList<>();
    private List<MultiItemEntity> multiItemEntityList = new ArrayList<>();
    private ChatAdapter chatAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},3);
        }

        manager = getSupportFragmentManager();

        if(myChatBar==null){
            myChatBar = new MyChatBarFragment();
        }

        initView();

    }

    private void initView() {

        ImageButton ibtn_group_chat_back = (ImageButton) findViewById(R.id.ibtn_group_chat_back);
        ImageButton ibtn_group_chat_menu = (ImageButton) findViewById(R.id.ibtn_group_chat_menu);
        TextView tv_group_chat_name = (TextView) findViewById(R.id.tv_group_chat_name);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_group_chat_detail_bar,myChatBar);
        transaction.commit();
        Date date = new Date(System.currentTimeMillis());
        multiItemEntityList.add(new TextMessageItem("巴拉巴拉巴拉",date,MultiItemEntity.OTHER,"鲨鱼辣椒"));
        multiItemEntityList.add(new TextMessageItem("巴拉巴拉巴拉巴拉巴拉巴拉",date,MultiItemEntity.OTHER,"鲨鱼辣椒"));
        multiItemEntityList.add(new TextMessageItem("巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉",date,MultiItemEntity.OTHER,"鲨鱼辣椒"));

        RecyclerView rcl_group_chat_detail = (RecyclerView) findViewById(R.id.rcl_group_chat_detail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        chatAdapter = new ChatAdapter(this, multiItemEntityList);
        rcl_group_chat_detail.setLayoutManager(layoutManager);
        rcl_group_chat_detail.setAdapter(chatAdapter);



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CHOOSE_PIC_REQUEST_CODE:
                Toast.makeText(this, "退出选择图片", Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "成功返回", Toast.LENGTH_SHORT).show();
                    ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                    analyticalSelectResults(result);
                } else if (resultCode == RESULT_CANCELED) {
                    Log.i("TAG", "onActivityResult PictureSelector Cancel");
                }
                break;
            default:
                break;

        }
    }
    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        mImageList = new ArrayList<>();
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(this, media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(this, media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
//            Log.i(TAG, "文件名: " + media.getFileName());
//            Log.i(TAG, "是否压缩:" + media.isCompressed());
//            Log.i(TAG, "压缩:" + media.getCompressPath());
//            Log.i(TAG, "初始路径:" + media.getPath());
//            Log.i(TAG, "绝对路径:" + media.getRealPath());
//            Log.i(TAG, "是否裁剪:" + media.isCut());
//            Log.i(TAG, "裁剪:" + media.getCutPath());
//            Log.i(TAG, "是否开启原图:" + media.isOriginal());
//            Log.i(TAG, "原图路径:" + media.getOriginalPath());
//            Log.i(TAG, "沙盒路径:" + media.getSandboxPath());
//            Log.i(TAG, "水印路径:" + media.getWatermarkPath());
//            Log.i(TAG, "视频缩略图:" + media.getVideoThumbnailPath());
//            Log.i(TAG, "原始宽高: " + media.getWidth() + "x" + media.getHeight());
//            Log.i(TAG, "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
//            Log.i(TAG, "文件大小: " + media.getSize());

            String path = new File(media.getRealPath()).getPath();
            Date date = new Date(System.currentTimeMillis());

            mImageList.add(path); // 接收已选图片地址，用于接口上传图片
            multiItemEntityList.add(new ImageMessageItem(path,date, MultiItemEntity.SELF,"user"));
        }
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendContent(MultiItemEntity multiItem) {
        multiItemEntityList.add(multiItem);
        chatAdapter.notifyDataSetChanged();

    }
}