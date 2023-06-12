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
import android.widget.Toast;

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
    private long countFollowings;
    private long countFans;
    private String phoneNumber;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public  NewPersonPageFragment newInstance(User user, Post newPost) {
        NewPersonPageFragment newPersonPageFragment = new NewPersonPageFragment();

        Bundle args = new Bundle();

        args.putSerializable("user", user);
        args.putSerializable("newpost", newPost);
        newPersonPageFragment.setArguments(args);
//         this.user= user;
//        this.newpost=newPost;
//        newPersonPageFragment.setArguments(args);

        return newPersonPageFragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences1 = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        userPhoneNumber = sharedPreferences1.getString("phoneNumber", "");//我
        userManager = new UserManager(getContext());
        Bundle args = getArguments();
        System.out.println("===========args:" + args);
        if (args != null) {
           user = (User) args.getSerializable("user");
            newpost = (Post) args.getSerializable("newpost");
            if (user != null) {
                //从首页点进来的用户
                phoneNumber = newpost.getPhoneNumber();
                // 当前设备正在登录的账号

                if (phoneNumber.equals(userPhoneNumber)) {
                    rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);

                } else {
                    rootView = inflater.inflate(R.layout.fragment_personal_page_other, container, false);

                    editablesource = rootView.findViewById(R.id.person_fragment_iv_attention);
                    isfollowingOrFriend1 = userManager.isFollowing(this.user, findUserByPhoneNumber(newpost.getPhoneNumber()));
                    if (isfollowingOrFriend1) {
                        editablesource.setImageResource(R.drawable.already_attention);
                    } else {
                        editablesource.setImageResource(R.drawable.attention);
                    }



                }
            }

            User postUser = null;  // 创建一个新的变量来保存从 newpost.getPhoneNumber() 获取的用户
            if (newpost != null) {
                postUser = findUserByPhoneNumber(newpost.getPhoneNumber());
            } else {
                Toast.makeText(getContext(), "newpost为空", Toast.LENGTH_SHORT).show();
            }
            if (postUser != null) {
                // 如果从 newpost.getPhoneNumber() 获取的用户不为空，使用这个用户
                this.user = postUser;
            } else if (this.user == null) {
                Toast.makeText(getContext(), "没有这个用户", Toast.LENGTH_SHORT).show();
            }


            isFromIntent = true;
        } else {
            user = findUserByPhoneNumber(userPhoneNumber);
            countFans = userManager.countFans(user);
            countFollowings = userManager.countFollowing(user);
            countFriends = userManager.countFriends(user);

            rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
            isFromIntent = false;
        }

        initData();
        initView(rootView);

        return rootView;

    }
    public User findUserByPhoneNumber(String phoneNumber) {
        List<User> users = LitePal.select("phoneNumber = ?", phoneNumber).find(User.class);
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
        if (userPhoneNumber.equals(phoneNumber)){
            friend = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend);
            friendNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend_number);
            friendNumber.setText(String.valueOf(countFriends));
        }
        attentionNumber.setText(String.valueOf(countFollowings));
        fensiNumber.setText(String.valueOf(countFans));



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
                if (phoneNumber.equals(userPhoneNumber)){
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivityForResult(intent,EDITABLEACTIVITY_BACK);
                }else {
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
//                String editedLocation = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_REGION);
//                String editedSignature = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_SIGNATURE);
//                String editedName = data.getStringExtra(EditProfileActivity.EXTRA_EDITED_NAME);
                if (editedGender!=null){
                    gender.setText(editedGender);
                }else {
                    gender.setText("未知");
                }
//                if (editedName!=null){
//                    name.setText(editedName);
//                }else {
//                    name.setText("YIXIANG");
//                }
//
//                if (editedSignature!=null) {
//                    signature.setText(editedSignature);
//                }else {
//                    signature.setText("这个人很懒，什么都没有留下");
//                }

                if (editedBirthday!=null){
                    birthday.setText(editedBirthday);
                }else {
                    birthday.setText("未知");
                }
//
//                if (editedLocation!=null) {
//                    region.setText(editedLocation);
//                }else {
//                    region.setText("未知");
//                }

                user.setGender(editedGender);
                user.setBirthday(editedBirthday);
//                user.setRegion(editedLocation);
//                user.setSignature(editedSignature);
//                user.setName(editedName);

                user.update(user.getId());

            }
        }
    }
}
