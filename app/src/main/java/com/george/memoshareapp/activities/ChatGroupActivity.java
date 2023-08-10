package com.george.memoshareapp.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.george.memoshareapp.interfaces.ChatMessageListener;
import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.interfaces.SendListener;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.properties.MessageType;
import com.george.memoshareapp.service.ChatService;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.utils.MediaUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
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

public class ChatGroupActivity extends AppCompatActivity implements SendListener , ChatMessageListener {
    private ChatService mService;
    private boolean mBound = false;
    private FragmentManager manager;
    private Fragment myChatBar;
    private final int CHOOSE_PIC_REQUEST_CODE = 3;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private List<String> mImageList = new ArrayList<>();
    private List<MultiItemEntity> multiItemEntityList = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private List<User> contactList;
    private String photoChatName;
    private ImageView more;
    private TextView tv_title;
    private ImageView back;
    private List<User> friendList;
    private List<User> newAddList=new ArrayList<>();
    private int chatRoomID;
    private String phoneNumber;
    private String chatRoomName;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);
        Intent intent1 = new Intent(ChatGroupActivity.this, ChatService.class);
        startService(intent1);
        bindService(intent1, connection, Context.BIND_AUTO_CREATE);

        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        phoneNumber = sp.getString("phoneNumber", "");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }

        manager = getSupportFragmentManager();

        if (myChatBar == null) {
            myChatBar = new MyChatBarFragment(chatRoomID);
        }
        Intent intent = getIntent();
        if (intent.getBooleanExtra("comeFromChatGroupMoreActivity",false)){
            contactList = (List<User>) intent.getSerializableExtra("addedContactList");
            friendList = (List<User>) intent.getSerializableExtra("FriendList");
            chatRoomID = intent.getIntExtra("ChatRoomID", -1);
            photoChatName = intent.getStringExtra("photoChatTitleName");

        }else {//来自ContactListActivity
            contactList = (List<User>) intent.getSerializableExtra("contact_list");
            friendList = (List<User>) intent.getSerializableExtra("FriendList");
            photoChatName = intent.getStringExtra("photo_chat_name");
            chatRoomID = intent.getIntExtra("ChatRoomID", -1);
            chatRoomName = intent.getStringExtra("ChatRoomName");
        }


        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        ImageButton ibtn_group_chat_back = (ImageButton) findViewById(R.id.ibtn_group_chat_back);
        ImageButton ibtn_group_chat_menu = (ImageButton) findViewById(R.id.ibtn_group_chat_menu);
        TextView tv_group_chat_name = (TextView) findViewById(R.id.tv_group_chat_name);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_group_chat_detail_bar, myChatBar);
        transaction.commit();
        Date date = new Date(System.currentTimeMillis());
        multiItemEntityList.add(new TextMessageItem("巴拉巴拉巴拉", date, MultiItemEntity.OTHER, "鲨鱼辣椒"));
        multiItemEntityList.add(new TextMessageItem("巴拉巴拉巴拉巴拉巴拉巴拉", date, MultiItemEntity.OTHER, "鲨鱼辣椒"));
        multiItemEntityList.add(new TextMessageItem("巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉", date, MultiItemEntity.OTHER, "鲨鱼辣椒"));

        RecyclerView rcl_group_chat_detail = (RecyclerView) findViewById(R.id.rcl_group_chat_detail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        chatAdapter = new ChatAdapter(this, multiItemEntityList);
        rcl_group_chat_detail.setLayoutManager(layoutManager);
        rcl_group_chat_detail.setAdapter(chatAdapter);
        back = (ImageView) findViewById(R.id.chat_group_iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(photoChatName);
        more = (ImageView) findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatGroupActivity.this, ChatGroupMoreActivity.class);

                intent.putExtra("contact_list",(Serializable) contactList);

//                if (!intent.getBooleanExtra("comeFromChatGroupMoreActivity",false)){
                    intent.putExtra("FriendList",(Serializable) friendList);
//                }

                intent.putExtra("photo_chat_name",photoChatName);
                intent.putExtra("comeFromChatGroupActivity",true);
                intent.putExtra("ChatRoomID",chatRoomID);

                startActivityForResult(intent,1);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    unbindService(connection);
                    mBound = false;
                }
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 处理从B页面返回的数据
            List<User> contactListFromChatGroupMoreActivity = (List<User>) data.getSerializableExtra("addedContactList");
            String photoChatTitleName = data.getStringExtra("photoChatTitleName");
            photoChatName=photoChatTitleName;
            System.out.println("-=-==-="+contactListFromChatGroupMoreActivity);
            if (contactListFromChatGroupMoreActivity != null && !contactListFromChatGroupMoreActivity.isEmpty()) {
                contactList=contactListFromChatGroupMoreActivity;
                // 在这里可以刷新RecyclerView等视图，使新的列表数据生效
            }
        }
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

            ImageMessageItem imageMessageItem = new ImageMessageItem(path, date, MultiItemEntity.SELF, "user");
            imageMessageItem.setFileName(media.getFileName());
            Logger.d(imageMessageItem.getFileName());
            multiItemEntityList.add(imageMessageItem);
            uploadFile(path, imageMessageItem);

        }
        chatAdapter.notifyDataSetChanged();
    }
    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    /**
     * 发送消息给service->服务器并更新UI
     *
     * @param multiItem
     */
    @Override
    public void sendContent(MultiItemEntity multiItem) {

        multiItemEntityList.add(multiItem);
        chatAdapter.notifyDataSetChanged();
        switch (multiItem.getItemShowType()) {
            case MessageType.TEXT:
                Date currentDate = new Date();
                EventBus.getDefault().post(new SendMessageEvent(new ChatMessage(currentDate,chatRoomName,chatRoomID,phoneNumber , multiItem.getItemContent(), "文本")));
                ChatMessage message = new ChatMessage(currentDate,chatRoomName, chatRoomID, phoneNumber, multiItem.getItemContent(), "文本");
                message.save();
                break;
            case MessageType.VOICE:
                Logger.d("发送语音");
                uploadFile(multiItem.getItemContent(), multiItem);
                break;
            default:
                break;
        }

    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
                    ChatMessage message = new ChatMessage(chatRoomID, phoneNumber, "", type);
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
    /**
     * 接收服务器消息
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onChatMessageEvent(ChatMessageEvent event) {
//        ChatMessage chatMessage = event.chatMessage;
//        String filePath = AppProperties.SERVER_MEDIA_URL + chatMessage.getAttachment().getFilePath();
//        MultiItemEntity multiItem = null;
//        Logger.d("onChatMessageEvent==========调用啦哈哈哈哈");
//
//        switch (chatMessage.getMessageType()) {
//            case "文本":
//                multiItem = new TextMessageItem(chatMessage.getContent(), new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, "6666");
//                break;
//            case "图片":
//                Logger.d(filePath);
//                multiItem = new ImageMessageItem(filePath, new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, "6666");
//                break;
//            case "语音":
//                multiItem = new VoiceMessageItem(filePath, new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, "6666");
//                Logger.d(filePath);
//                break;
//            case "视频":
//
//                break;
//            default:
//                break;
//        }
//        if (multiItem != null) {
//            multiItemEntityList.add(multiItem);
//            chatAdapter.notifyDataSetChanged();
//        }
//    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatMessageEvent(ChatMessageEvent event) {
        ChatMessage chatMessage = event.chatMessage;
        // 处理接收到的消息
        ChatMessageListener(chatMessage);
    }


    @Override
    public void ChatMessageListener(ChatMessage chatMessage) {
        chatMessage.save();
        String filePath="";
        if (chatMessage != null && chatMessage.getAttachment() != null) {
             filePath = AppProperties.SERVER_MEDIA_URL + chatMessage.getAttachment().getFilePath();

        }
        MultiItemEntity multiItem = null;
        Logger.d("onChatMessageEvent=接口=========调用啦哈哈哈哈");

        switch (chatMessage.getMessageType()) {
            case "文本":
                multiItem = new TextMessageItem(chatMessage.getContent(), new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, chatMessage.getSenderId());
                break;
            case "图片":
                Logger.d(filePath);
                multiItem = new ImageMessageItem(filePath, new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, chatMessage.getSenderId());
                break;
            case "语音":
                multiItem = new VoiceMessageItem(filePath, new Date(System.currentTimeMillis()), MultiItemEntity.OTHER, chatMessage.getSenderId());
                Logger.d(filePath);
                break;
            case "视频":

                break;
            default:
                break;
        }
        if (multiItem != null) {
            multiItemEntityList.add(multiItem);
            chatAdapter.notifyDataSetChanged();
        }
    }
}