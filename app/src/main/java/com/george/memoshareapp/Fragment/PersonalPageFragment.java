package com.george.memoshareapp.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.LoginActivity;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.Fragment
 * @className: PersonalPageFragment
 * @author: George
 * @description: TODO
 * @date: 2023/5/8 21:27
 * @version: 1.0
 */
public class PersonalPageFragment extends Fragment {

    private View view;
    private Button quit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_page, container, false);

        initView();
        return view;
    }

    private void initView() {
        SharedPreferences sp = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
        quit = (Button) view.findViewById(R.id.quit_login);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putBoolean("isLogin", false).commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

}
