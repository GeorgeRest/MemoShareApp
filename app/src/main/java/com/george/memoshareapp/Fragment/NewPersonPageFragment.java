package com.george.memoshareapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.EditProfileActivity;
import com.george.memoshareapp.activities.LoginActivity;
import com.george.memoshareapp.adapters.PersonPageAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.view.NiceImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class NewPersonPageFragment extends Fragment {
    private static final int EDITABLEACTIVITY_BACK = 1;
    //外部
    private TabLayout mTabLayout;
    public ViewPager2 mViewPager2;
    private List<String> mData = new ArrayList<>();
    private PersonPageAdapter personpageadapter;
    private AppBarLayout appBarLayout;
    private TextView toolbarTitle;
    private Button quit;
    private SharedPreferences sharedPreferences1;
    private View rootView;

    private NiceImageView head;
    private TextView name;
    private TextView mingyan;
    private TextView attentionNumber;
    private TextView fensiNumber;
    private TextView friendNumber;
    private User user;
    private Intent intent;
    private boolean isFromIntent = false;
    private TextView friend;
    private ImageView editablesource;

    private UserManager userManager;
    private TextView tv_location;
    private TextView signature;
    private TextView gender;
    private TextView birthday;
    private TextView region;
    private String userPhoneNumber;
    private Post newpost;
    private long countFriends;
    private boolean isfollowingOrFriend1=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static NewPersonPageFragment newInstance(User user) {
        NewPersonPageFragment newFragment = new NewPersonPageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 从Intent中获取数据
        intent = getActivity().getIntent();
        if(intent != null) {
            user = (User) intent.getSerializableExtra("user");
            newpost = (Post) intent.getSerializableExtra("newpost");

            if (user != null) {
                String phoneNumber = user.getPhoneNumber();//从首页点进来的用户
                sharedPreferences1 = getActivity().getSharedPreferences("User", MODE_PRIVATE);
                userPhoneNumber = sharedPreferences1.getString("phoneNumber", "");//我
                // 当前设备正在登录的账号

                if(phoneNumber.equals(userPhoneNumber)){
                    rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
                    isFromIntent = true;
                } else {
                    rootView = inflater.inflate(R.layout.fragment_personal_page_other, container, false);
                    isFromIntent = false;
                }
                userManager = new UserManager(getContext());
                isfollowingOrFriend1 = userManager.isFollowing( user,findUserByPhoneNumber(newpost.getPhoneNumber()));

                if (isfollowingOrFriend1) {
                    editablesource.setImageResource(R.drawable.already_attention);
                } else {
                    editablesource.setImageResource(R.drawable.attention);
                }
            } else {
                // 处理用户为空的情况
                rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
                isFromIntent = false;
            }


        } else {
            // 处理Intent为空的情况
            rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
            isFromIntent = false;
        }

        initData();
        initView(rootView);


        long countFollowings = userManager.countFollowing(user);
        long countFans = userManager.countFans(user);
        countFriends = userManager.countFriends(user);
        attentionNumber.setText(String.valueOf(countFollowings));
        fensiNumber.setText(String.valueOf(countFans));



        return rootView;

    }
    public User findUserByPhoneNumber(String phoneNumber) {
        List<User> users = LitePal.where("phoneNumber = ?", phoneNumber).find(User.class);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }


    private void initView(View rootView) {
        head = (NiceImageView) rootView.findViewById(R.id.person_fragment_iv_head);
        editablesource = (ImageView) rootView.findViewById(R.id.person_fragment_iv_attention);
        name = (TextView) rootView.findViewById(R.id.person_fragment_tv_name);
        signature = (TextView) rootView.findViewById(R.id.tv_edit_signature);
        gender = (TextView) rootView.findViewById(R.id.tv_edit_gender);
        birthday = (TextView) rootView.findViewById(R.id.tv_edit_birthday);
        region = (TextView) rootView.findViewById(R.id.tv_edit_region);
        tv_location = (TextView) rootView.findViewById(R.id.person_fragment_tv_location);
        mingyan = (TextView) rootView.findViewById(R.id.person_fragment_tv_mingyan);
        attentionNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_guanzhu_number);
        fensiNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_fensi_number);
        if (!isFromIntent){
            friend = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend);
            friendNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend_number);
            friendNumber.setText(String.valueOf(countFriends));
        }


        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPager2 = rootView.findViewById(R.id.person_pager);
        personpageadapter = new PersonPageAdapter(getActivity(), mData);
        mViewPager2.setAdapter(personpageadapter);
        new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mData.get(position));

            }
        }).attach();
        mTabLayout.setTabTextColors(Color.WHITE, Color.parseColor("#685C97"));

        appBarLayout = rootView.findViewById(R.id.appBar);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(i);
            if (tab != null) {
                // 设置为null移除背景和点击效果
                tab.setBackground(null);
            }
        }

        quit = (Button) rootView.findViewById(R.id.quit_login);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences1.edit().putBoolean("isLogin", false).commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        editablesource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivityForResult(intent,EDITABLEACTIVITY_BACK);
                if (isFromIntent){
                    if (isfollowingOrFriend1){
                        isfollowingOrFriend1 = false;
                        editablesource.setImageResource(R.drawable.attention);
                        userManager.unfollowUser( user,findUserByPhoneNumber(newpost.getPhoneNumber()));
                    } else {
                        isfollowingOrFriend1 = true;
                        editablesource.setImageResource(R.drawable.already_attention);
                        userManager.followUser( user,findUserByPhoneNumber(newpost.getPhoneNumber()));
                    }
                }


            }
        });
    }

    private void initData() {
        mData.add("发布");
        mData.add("点赞");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITABLEACTIVITY_BACK && resultCode == EditProfileActivity.RESULT_OK) {
            if (data != null) {
                String editedGender = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_GENDER);
                String editedBirthday = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_BIRTHDAY);
                String editedLocation = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_REGION);
                String editedSignature = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_SIGNATURE);
                String editedName = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_NAME);
                name.setText(editedName);
                signature.setText(editedSignature);
                gender.setText(editedGender);
                birthday.setText(editedBirthday);
                region.setText(editedLocation);

                user.setGender(editedGender);
                user.setBirthday(editedBirthday);
                user.setRegion(editedLocation);
                user.setSignature(editedSignature);
                user.setName(editedName);

                user.update(user.getId());

            }
        }
    }
}
