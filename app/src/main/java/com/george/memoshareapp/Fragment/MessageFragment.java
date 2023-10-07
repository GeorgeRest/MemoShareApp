package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ChatRoomAdapter;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.manager.ChatRoomManager;

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

    private View view;
    private ListView chatroomlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message_page, container, false);
        chatroomlist = view.findViewById(R.id.chatroomlist);
        // 查询最后20条ChatRoom记录
        List<ChatRoom> last20ChatRooms = new ChatRoomManager().getLast20ChatRoom();
        // 创建ChatRoomAdapter并设置给ListView
        ChatRoomAdapter adapter = new ChatRoomAdapter(getActivity(), last20ChatRooms);
        chatroomlist.setAdapter(adapter);
        return view;
    }
}
