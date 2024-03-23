package com.george.memoshareapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.InformActivity;
import com.george.memoshareapp.adapters.ChatRoomListAdapter;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.events.ChatMessageEvent;
import com.george.memoshareapp.manager.ChatManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.Fragment
 * @className: MessageFragment
 * @author: George
 * @description: TODO
 * @date: 2023/5/8 21:25
 * @version: 1.0
 */
public class MessageFragment extends Fragment {

    private RecyclerView rv_chat_room_list;
    private LinearLayout ll_comment;
    private ChatManager chatManager;
    private ChatRoomListAdapter chatRoomListAdapter;
    private View view;
    private Handler handler= new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message_page, container, false);
        rv_chat_room_list = view.findViewById(R.id.rv_chat_room_list);
        ll_comment = view.findViewById(R.id.ll_comment);
        rv_chat_room_list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chatManager = new ChatManager(view.getContext());
        chatRoomListAdapter = new ChatRoomListAdapter(view.getContext());
        rv_chat_room_list.setAdapter(chatRoomListAdapter);
        EventBus.getDefault().register(this);
        ll_comment.setOnClickListener((view)->{
            Intent intent = new Intent(getActivity(), InformActivity.class);
            startActivity(intent);

        });
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            EventBus.getDefault().register(this);
            List<ChatRoom> chatRoomList = chatManager.getChatRoomList();
            chatRoomListAdapter.submitList(chatRoomList);
            chatRoomListAdapter.notifyDataSetChanged();
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        List<ChatRoom> chatRoomList = chatManager.getChatRoomList();
        if (chatRoomList != null) {
            chatRoomListAdapter.submitList(chatRoomList);
        } else {
            chatRoomListAdapter.setEmptyViewLayout(view.getContext(), R.layout.empty_view);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatMessageEvent(ChatMessageEvent event) {
        List<ChatRoom> chatRoomList = chatManager.getChatRoomList();
        chatRoomListAdapter.submitList(chatRoomList);
        chatRoomListAdapter.notifyDataSetChanged();
    }

}
