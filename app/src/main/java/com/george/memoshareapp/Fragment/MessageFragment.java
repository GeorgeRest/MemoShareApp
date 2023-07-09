package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message_page, container, false);
        return view;
    }
}
