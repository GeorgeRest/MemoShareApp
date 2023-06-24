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

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.EditProfileActivity;
import com.george.memoshareapp.activities.FriendActivity;
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
    private TextView person_fragment_tv_name;
    private TextView mingyan;
    private TextView attentionNumber;
    private TextView fensiNumber;
    private TextView friendNumber;
    private User userFromHomePageFragment;
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
    private boolean isfollowingOrFriend1 = false;
    private long countFollowings;
    private long countFans;
    private String phoneNumber;
    private User userMe;
    private TextView person_fragment_tv_guanzhu;
    private TextView person_fragment_tv_fensi;
    private TextView person_fragment_tv_friend;
    private Boolean ismyslef = false;
    private ImageView iv_sex;
    private User otheruser;
    private Bundle args;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public NewPersonPageFragment newInstance(User user, Post newPost) {
        NewPersonPageFragment newPersonPageFragment = new NewPersonPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        args.putSerializable("newpost", newPost);
        newPersonPageFragment.setArguments(args);
        return newPersonPageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences1 = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        userPhoneNumber = sharedPreferences1.getString("phoneNumber", "");//我
        userManager = new UserManager(getContext());
        userMe = findUserByPhoneNumber(userPhoneNumber);//根据登录的时候的手机号找到的用户
        args = getArguments();
        if (args != null) {
            newpost = (Post) args.getSerializable("newpost");
            phoneNumber = newpost.getPhoneNumber();
            otheruser = userManager.findUserByPhoneNumber(phoneNumber);
            if (otheruser != null) {
                //从首页点进来的用户
//                String headPortraitPath = userFromHomePageFragment.getHeadPortraitPath();
//                Glide.with(this).load(headPortraitPath).into(head);

                // 当前设备正在登录的账号
                if (phoneNumber.equals(userPhoneNumber)) {
                    rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
                    ismyslef = true;
                } else {
                    rootView = inflater.inflate(R.layout.fragment_personal_page_other, container, false);
                    ismyslef = false;
                    editablesource = rootView.findViewById(R.id.person_fragment_iv_attention);
                    isfollowingOrFriend1 = userManager.isFollowing(userMe, otheruser);
                    if (isfollowingOrFriend1) {
                        editablesource.setImageResource(R.drawable.already_attention);
                    } else {
                        editablesource.setImageResource(R.drawable.attention);
                    }

                }
            }
            getParams2View(otheruser);
            countFans = userManager.countFans(otheruser);
            countFollowings = userManager.countFollowing(otheruser);
            countFriends = userManager.countFriends(otheruser);
            isFromIntent = true;
            headIcon(otheruser);
        } else {
            rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
            getParams2View(userMe);
            countFans = userManager.countFans(userMe);
            countFollowings = userManager.countFollowing(userMe);
            countFriends = userManager.countFriends(userMe);
            ismyslef = true;
            isFromIntent = false;
            headIcon(userMe);
        }
        initData();
        initView(rootView);

        return rootView;
    }

    private void headIcon(User user) {
        head = (NiceImageView) rootView.findViewById(R.id.person_fragment_iv_head);
        String headPortraitPath = user.getHeadPortraitPath();
        if (headPortraitPath != null ){
            Glide.with(this).load(headPortraitPath).into(head);
        }else{
            head.setImageResource(R.mipmap.app_icon);
        }
    }


    private void getParams2View(User u1) {
        person_fragment_tv_name = (TextView) rootView.findViewById(R.id.person_fragment_tv_name);
        iv_sex = (ImageView) rootView.findViewById(R.id.sex);
        tv_location = (TextView) rootView.findViewById(R.id.person_fragment_tv_location);
        mingyan = (TextView) rootView.findViewById(R.id.person_fragment_tv_mingyan);
        String name = u1.getName();
        String gender = u1.getGender();
        String signature = u1.getSignature();
        String region = u1.getRegion();
        if (name == null) {
            String s = u1.generateDefaultName(u1.getId());
            person_fragment_tv_name.setText(s);
        } else {
            person_fragment_tv_name.setText(name);
        }

        if (gender == null) {
            iv_sex.setImageResource(R.mipmap.sex_open);
        } else {
            iv_sex.setVisibility(View.VISIBLE);
            if (gender.equals("男")) {
                iv_sex.setImageResource(R.mipmap.sex_man);
            } else if (gender.equals("女")) {
                iv_sex.setImageResource(R.drawable.sex_woman);
            } else {
                iv_sex.setVisibility(View.GONE);
            }
        }


        if (signature == null) {
            mingyan.setText("这个人很懒，什么都没有留下");
        } else {
            mingyan.setText(signature);
        }
        if (region == null) {
            tv_location.setText("地区未知");
        } else {
            tv_location.setText(region);
        }
    }

    public User findUserByPhoneNumber(String phoneNumber) {
        User users = LitePal
                .where("phoneNumber = ?", phoneNumber)
                .findFirst(User.class);
        if (users != null) {
            return users;
        } else {
            return null;
        }
    }


    private void initView(View rootView) {
        head = (NiceImageView) rootView.findViewById(R.id.person_fragment_iv_head);
        person_fragment_tv_guanzhu = (TextView) rootView.findViewById(R.id.person_fragment_tv_guanzhu);
        person_fragment_tv_fensi = (TextView) rootView.findViewById(R.id.person_fragment_tv_fensi);
        iv_sex = (ImageView) rootView.findViewById(R.id.sex);
        editablesource = (ImageView) rootView.findViewById(R.id.person_fragment_iv_attention);
        person_fragment_tv_name = (TextView) rootView.findViewById(R.id.person_fragment_tv_name);
        tv_location = (TextView) rootView.findViewById(R.id.person_fragment_tv_location);
        mingyan = (TextView) rootView.findViewById(R.id.person_fragment_tv_mingyan);
        attentionNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_guanzhu_number);
        fensiNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_fensi_number);
        if (phoneNumber == null) {
            countFans = userManager.countFans(userMe);
            countFollowings = userManager.countFollowing(userMe);
            countFriends = userManager.countFriends(userMe);
            friend = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend);
            friendNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend_number);
            friendNumber.setText(String.valueOf(countFriends));
        } else {
            if (userPhoneNumber.equals(phoneNumber)) {
                friend = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend);
                friendNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend_number);
                friendNumber.setText(String.valueOf(countFriends));
            }
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
                if(phoneNumber!=null){
                    if (phoneNumber.equals(userPhoneNumber)) {//newpost和sp的电话号
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        intent.putExtra("user", userMe);
                        startActivityForResult(intent, EDITABLEACTIVITY_BACK);
                    } else {
                        if (isfollowingOrFriend1) {
                            isfollowingOrFriend1 = false;
                            editablesource.setImageResource(R.drawable.attention);
                            userManager.unfollowUser(userMe, otheruser);
                        } else {
                            isfollowingOrFriend1 = true;
                            editablesource.setImageResource(R.drawable.already_attention);
                            userManager.followUser(userMe, otheruser);
                        }
                        if (userPhoneNumber.equals(phoneNumber)) {
                            attentionNumber.setText(userManager.countFollowing(otheruser) + "");
                            fensiNumber.setText(userManager.countFans(otheruser) + "");
                            friendNumber.setText(userManager.countFriends(otheruser) + "");
                        } else {
                            attentionNumber.setText(userManager.countFollowing(otheruser) + "");
                            fensiNumber.setText(userManager.countFans(otheruser) + "");
                        }
                    }

                }else {
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    intent.putExtra("user", userMe);
                    startActivityForResult(intent, EDITABLEACTIVITY_BACK);
                }




            }
        });
        if (ismyslef) {
            person_fragment_tv_friend = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend);
            person_fragment_tv_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FriendActivity.class);
                    if (args != null) {
                        phoneNumber = newpost.getPhoneNumber();
                        intent.putExtra("postPhoneNumber", phoneNumber);
                    }
                    intent.putExtra("isFriend", 2);
                    intent.putExtra("ismyself", ismyslef);
                    startActivity(intent);
                }
            });
        }

        person_fragment_tv_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendActivity.class);

                intent.putExtra("isFriend", 0);
                intent.putExtra("ismyself", ismyslef);
                if (args != null) {
                    phoneNumber = newpost.getPhoneNumber();
                    intent.putExtra("postPhoneNumber", phoneNumber);
                }
                startActivity(intent);
            }
        });
        person_fragment_tv_fensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendActivity.class);

                intent.putExtra("isFriend", 1);
                intent.putExtra("ismyself", ismyslef);
                if (args != null) {
                    phoneNumber = newpost.getPhoneNumber();
                    intent.putExtra("postPhoneNumber", phoneNumber);
                }
                startActivity(intent);
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
                if (editedGender != null) {
                    if (editedGender.equals("男")) {
                        iv_sex.setImageResource(R.mipmap.sex_man);
                    } else if (editedGender.equals("女")) {
                        iv_sex.setImageResource(R.drawable.sex_woman);
                    }
                }

                if (otheruser != null) {
                    otheruser.setGender(editedGender);
                    otheruser.setBirthday(editedBirthday);
                    otheruser.update(otheruser.getId());
                }
                if (userMe != null) {
                    userMe.setGender(editedGender);
                    userMe.setBirthday(editedBirthday);
                    userMe.update(userMe.getId());
                }

            }
        }
    }
}
