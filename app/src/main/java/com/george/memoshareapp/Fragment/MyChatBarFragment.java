package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.george.memoshareapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyChatBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyChatBarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentManager manager;
    private final int EDITTEXT_STATE = 1;
    private final int VOICE_RECORD_STATE = 2;
    private int chatState = 1;
    private View view;
    private ImageView ibtn_group_chat_input_type;

    MyEdittextFragment myEtFragment = null;
    Fragment myRecFragment = null;


    public MyChatBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyChatBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyChatBarFragment newInstance(String param1, String param2) {
        MyChatBarFragment fragment = new MyChatBarFragment();
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
        return inflater.inflate(R.layout.fragment_my_chat_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        manager = getFragmentManager();

        initView();
        initFrag();

    }

    private void initView() {
        ibtn_group_chat_input_type = (ImageView) view.findViewById(R.id.iv_group_chat_input_type);

        ibtn_group_chat_input_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (chatState){
                    case EDITTEXT_STATE:
                        set2Rec();
                        break;
                    case VOICE_RECORD_STATE:
                        set2Et();
                        break;
                    default:
                        break;

                }
            }
        });



    }

    private void initFrag() {
        set2Et();
    }



    private void set2Et() {
        if (myEtFragment ==null){
            myEtFragment = new MyEdittextFragment();
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_group_chat_bar, myEtFragment);
        transaction.commit();
        ibtn_group_chat_input_type.setImageResource(R.drawable.group_chat_voice);
        chatState = EDITTEXT_STATE;
    }

    private void set2Rec() {
        if (myRecFragment ==null){
            myRecFragment = new MyVoiceRecordFragment();
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_group_chat_bar, myRecFragment);
        transaction.commit();
        ibtn_group_chat_input_type.setImageResource(R.drawable.group_chat_letters);
        chatState = VOICE_RECORD_STATE;
    }

    public EditText getEditText() {
        return myEtFragment.getEditText();
    }
}