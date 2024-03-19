package com.george.memoshareapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.Fragment.MyChatBarFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ChatAdapter;
import com.george.memoshareapp.beans.ChatAttachment;
import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.beans.ImageMessageItem;
import com.george.memoshareapp.beans.TextMessageItem;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.beans.VoiceMessageItem;
import com.george.memoshareapp.events.ChatMessageEvent;
import com.george.memoshareapp.events.SendMessageEvent;
import com.george.memoshareapp.http.ProgressRequestBody;
import com.george.memoshareapp.http.api.ChatServiceApi;
import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.interfaces.SendListener;
import com.george.memoshareapp.manager.ChatManager;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.properties.MessageType;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.utils.MediaUtils;
import com.lxj.xpopup.util.KeyboardUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatActivity extends BaseActivity implements SendListener, View.OnClickListener {

    private static final String TAG = "GroupChatActivity";
    private FragmentManager manager;
    private MyChatBarFragment myChatBar;
    private final int CHOOSE_PIC_REQUEST_CODE = 3;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private List<String> mImageList = new ArrayList<>();
    private List<MultiItemEntity> multiItemEntityList;
    private ChatAdapter chatAdapter;
    private UserManager userManager;
    public String chatRoomId;
    private User selfUser;
    private ChatManager chatManager;
    private RecyclerView rv_chat;
    private String chatRoomName;
    public static boolean isInGroupChatActivity;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }

        manager = getSupportFragmentManager();

        if (myChatBar == null) {
            myChatBar = new MyChatBarFragment();
        }

        initView();
    }

    private void initView() {

        ImageButton ibtn_group_chat_back = (ImageButton) findViewById(R.id.ibtn_group_chat_back);
        ImageButton ibtn_group_chat_menu = (ImageButton) findViewById(R.id.ibtn_group_chat_menu);
        TextView ibtn_group_chat = (TextView) findViewById(R.id.ibtn_group_chat);
        TextView tv_group_chat_name = (TextView) findViewById(R.id.tv_group_chat_name);
        ibtn_group_chat_back.setOnClickListener(this);
        ibtn_group_chat_menu.setOnClickListener(this);
        ibtn_group_chat.setOnClickListener(this);
        userManager = new UserManager(this);
        selfUser = userManager.findUserByPhoneNumber(UserManager.getSelfPhoneNumber(this));
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_group_chat_detail_bar, myChatBar);
        transaction.commit();
        chatManager = new ChatManager(this);
        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chatRoomId");
        chatRoomName = intent.getStringExtra("chatRoomName");
        tv_group_chat_name.setText(chatRoomName);
        multiItemEntityList = chatManager.getMessageFromDB(chatRoomId, 0);
        rv_chat = (RecyclerView) findViewById(R.id.rcl_group_chat_detail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        chatAdapter = new ChatAdapter(this, multiItemEntityList,rv_chat,chatRoomId);
        rv_chat.setLayoutManager(layoutManager);
        rv_chat.setAdapter(chatAdapter);
        rv_chat.scrollToPosition(multiItemEntityList.size() - 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
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
            //需要进行上传图片或视频

            ImageMessageItem imageMessageItem = new ImageMessageItem(path, date, MultiItemEntity.SELF, selfUser);
            imageMessageItem.setFileName(media.getFileName());
            Logger.d(imageMessageItem.getFileName());
//            multiItemEntityList.add(imageMessageItem);
            chatAdapter.addData(imageMessageItem);
            uploadFile(path, imageMessageItem);

        }
        chatAdapter.notifyDataSetChanged();
    }

    /**
     * 发送消息给service->服务器并更新UI
     *
     * @param multiItem
     */
    @Override
    public void sendContent(MultiItemEntity multiItem) {
//        multiItemEntityList.add(multiItem);
        chatAdapter.addData(multiItem);
        chatAdapter.notifyDataSetChanged();

        switch (multiItem.getItemShowType()) {
            case MessageType.TEXT:
                EventBus.getDefault().post(new SendMessageEvent(new ChatMessage(Integer.parseInt(chatRoomId), selfUser.getPhoneNumber(), multiItem.getItemContent(), "文本")));
                break;
            case MessageType.VOICE:
                Logger.d("发送语音");
                uploadFile(multiItem.getItemContent(), multiItem);
                break;
            default:
                break;
        }
        rv_chat.smoothScrollToPosition(chatAdapter.getData().size() - 1);
        KeyboardUtils.hideSoftInput(getWindow());
    }

    private void uploadFile(String filePath, MultiItemEntity multiItem) {
        ChatServiceApi service = RetrofitManager.getInstance().create(ChatServiceApi.class);
        File file = new File(filePath);
        int itemPosition = chatAdapter.getItemPosition(multiItem);
        Logger.d("itemPosition" + itemPosition);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        multiItem.setProgress(percentage);
                        chatAdapter.notifyItemChanged(itemPosition);
                    }
                });
            }

            @Override
            public void onError() {
                // do something on error
            }

            @Override
            public void onFinish() {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        multiItem.setProgress(100);
                        chatAdapter.notifyItemChanged(itemPosition);
                    }
                });

            }
        });

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        String descriptionString = "This is a description";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String type = "";
                    switch (multiItem.getItemShowType()) {
                        case MessageType.VOICE:
                            type = "语音";
                            break;
                        case MessageType.IMAGE:
                            type = "图片";
                            break;
                        default:
                            return;
                    }
                    ChatMessage message = new ChatMessage(Integer.parseInt(chatRoomId), UserManager.getSelfPhoneNumber(GroupChatActivity.this), "", type);
                    ChatAttachment attachment = new ChatAttachment(multiItem.getFileName(), type);
                    message.setAttachment(attachment);
                    EventBus.getDefault().post(new SendMessageEvent(message));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        chatManager.setLastViewTime(-1);
        super.onDestroy();
    }

    /**
     * 接收服务器消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatMessageEvent(ChatMessageEvent event) {
        ChatMessage chatMessage = event.chatMessage;
        if (chatMessage.getChatRoomId() != Integer.parseInt(chatRoomId)) {
            return;
        }
        MultiItemEntity multiItem = null;

        switch (chatMessage.getMessageType()) {
            case "文本":
                multiItem = new TextMessageItem(chatMessage.getContent(), new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, chatMessage.getUser());
                break;
            case "图片":
                String pictureFilePath = AppProperties.SERVER_MEDIA_URL + chatMessage.getAttachment().getFilePath();
                Logger.d(pictureFilePath);
                multiItem = new ImageMessageItem(pictureFilePath, new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, chatMessage.getUser());
                break;
            case "语音":
                String RecordFilePath = AppProperties.SERVER_MEDIA_URL + chatMessage.getAttachment().getFilePath();
                multiItem = new VoiceMessageItem(RecordFilePath, new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, chatMessage.getUser());
                Logger.d(RecordFilePath);
                break;
            case "视频":

                break;
            default:
                break;
        }
        if (multiItem != null) {
//            multiItemEntityList.add(multiItem);
            chatAdapter.addData(multiItem);
            chatAdapter.notifyDataSetChanged();
            rv_chat.scrollToPosition(chatAdapter.getData().size() - 1);
        }
    }

    public static void openGroupChatActivity(Context context, int chatRoomId, String chatRoomName) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        intent.putExtra("chatRoomId", chatRoomId + "");
        intent.putExtra("chatRoomName", chatRoomName);
        context.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isInGroupChatActivity = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtils.hideSoftInput(getWindow());
        myChatBar.getEditText().clearFocus();
        EventBus.getDefault().register(this);
        long lastViewTime = chatManager.getLastViewTime();
        if (lastViewTime == -1) {
            return;
        }
        List<MultiItemEntity> multiItemEntities = chatManager.getMessageFromDB(chatRoomId, lastViewTime);
        if (multiItemEntities != null && !multiItemEntities.isEmpty()) {
            chatAdapter.addAll(multiItemEntities);
            chatAdapter.notifyDataSetChanged();
            rv_chat.scrollToPosition(multiItemEntityList.size() - 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInGroupChatActivity = false;
        EventBus.getDefault().unregister(this);
        chatManager.setLastViewTime(System.currentTimeMillis());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_group_chat_back:
                finish();
                break;
            case R.id.ibtn_group_chat:
            case R.id.ibtn_group_chat_menu:
                Intent intent = new Intent(this, GroupMoreActivity.class);
                intent.putExtra("chatRoomId", chatRoomId);
                intent.putExtra("ChatRoomName", chatRoomName);
                startActivity(intent);
                break;

            default:
                break;
        }
    }


}