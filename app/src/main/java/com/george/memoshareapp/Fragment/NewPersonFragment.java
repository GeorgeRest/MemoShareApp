package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;

public class NewPersonFragment extends Fragment {
    //内部 选 点赞和发布

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public NewPersonFragment() {
    }
    public static NewPersonFragment newInstance(String param1) {
        NewPersonFragment fragment = new NewPersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        switch (mParam1) {
            case "发布":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_release, container, false);
                break;
            case "点赞":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_dainzan, container, false);
                break;
            default:
                break;
        }
        return rootView;
    }

}
