package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.beans.TextMessageItem;
import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.interfaces.SendListener;
import com.george.memoshareapp.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEdittextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEdittextFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    private int sendBtnState = 1;
    private final int SEND_PIC_BTN_STATE = 1;
    private final int SEND_TEXT_BTN_STATE = 2;
    private final int CHOOSE_PIC_REQUEST_CODE = 3;
    private static final int REQUEST_SELECT_MEDIA = 100;
    private List<String> mImageList = new ArrayList<>();
    public MyEdittextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyEdittextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyEdittextFragment newInstance(String param1, String param2) {
        MyEdittextFragment fragment = new MyEdittextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_edittext, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        initView();

    }

    private void initView() {
        EditText et_group_chat = (EditText) view.findViewById(R.id.et_group_chat);
        ImageView iv_group_chat_send = (ImageView) view.findViewById(R.id.iv_group_chat_text_send);
        RelativeLayout rl_group_chat_text_send = (RelativeLayout) view.findViewById(R.id.rl_group_chat_text_send);
        TextView tv_group_chat_send = (TextView) view.findViewById(R.id.tv_group_chat_send);

        et_group_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    iv_group_chat_send.setImageResource(R.drawable.group_chat_send_pic);
                    tv_group_chat_send.setVisibility(View.INVISIBLE);
                    sendBtnState = SEND_PIC_BTN_STATE;
                }else {
                    iv_group_chat_send.setImageResource(R.drawable.group_chat_send_text);
                    tv_group_chat_send.setVisibility(View.VISIBLE);
                    sendBtnState = SEND_TEXT_BTN_STATE;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rl_group_chat_text_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sendBtnState){
                    case SEND_PIC_BTN_STATE:
                        PictureSelector.create(getActivity())
                                .openGallery(SelectMimeType.ofAll())
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isWithSelectVideoImage(false)//图片视频分别处理
//                                .setSelectionMode(Picture)//设置单选多选
                                .setMaxSelectNum(9)//最多可选多少图片
                                .setMaxVideoSelectNum(9)//最多可选多少视频
                                .isEmptyResultReturn(true)
                                .isMaxSelectEnabledMask(true)//设置是否启用选中数量超过限制时的蒙层效果，超出数量，显示蒙层提示用户无法再继续选择
                                .setImageSpanCount(4) //相册列表每行显示个数
                                .isDisplayCamera(false)//是否显示相机入口
                                .isPreviewVideo(true)//是否可以预览视频
                                .forResult(CHOOSE_PIC_REQUEST_CODE);



                        break;
                    case SEND_TEXT_BTN_STATE:
//                        Toast.makeText(getContext(), "现在是发文字", Toast.LENGTH_SHORT).show();

                        String text = et_group_chat.getText().toString();
                        SendListener listener = (SendListener) getActivity();
                        Date date = new Date(System.currentTimeMillis());
                        listener.sendContent(new TextMessageItem(text,date, MultiItemEntity.SELF,"user"));
                        Toast.makeText(getContext(), "发送文字成功", Toast.LENGTH_SHORT).show();

                        et_group_chat.setText("");

                        break;
                    default:
                        break;
                }
            }
        });


    }


}