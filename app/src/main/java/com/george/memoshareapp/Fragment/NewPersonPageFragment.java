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

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.EditProfileActivity;
import com.george.memoshareapp.activities.FriendActivity;
import com.george.memoshareapp.activities.LoginActivity;
import com.george.memoshareapp.adapters.PersonPageAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.RelationshipServiceApi;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.interfaces.OnSaveUserListener;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.view.NiceImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPersonPageFragment extends Fragment  {//外部
    private static final int EDITABLEACTIVITY_BACK = 1;
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
    private RelationshipServiceApi relationshipServiceApi;
    private UserServiceApi userServiceApi;
    private User userfromIDE;
    private Relationship relationship;
    private UserManager userManager;
    private boolean alreadyAttention=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public NewPersonPageFragment newInstance(User user, String newPostNumber) {
        NewPersonPageFragment newPersonPageFragment = new NewPersonPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        args.putSerializable("newpost", newPostNumber);
        newPersonPageFragment.setArguments(args);
        return newPersonPageFragment;
    }
    public NewPersonPageFragment newInstance(User user) {
        NewPersonPageFragment newPersonPageFragment = new NewPersonPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        newPersonPageFragment.setArguments(args);
        return newPersonPageFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences1 = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        userPhoneNumber = sharedPreferences1.getString("phoneNumber", "");//我
        userManager = new UserManager(getContext());
        userMe=userManager.findUserByPhoneNumber(userPhoneNumber);
        initData();
        args = getArguments();
        if (args != null) {
            otheruser = (User) args.getSerializable("user");
            phoneNumber= otheruser.getPhoneNumber();
            if (otheruser != null) {
                // 当前设备正在登录的账号
                if (phoneNumber.equals(userPhoneNumber)) {
                    rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
                    ismyslef = true;
                    initView(rootView);
                } else {
                    rootView = inflater.inflate(R.layout.fragment_personal_page_other, container, false);
                    ismyslef = false;
                    initView(rootView);
                    editablesource = rootView.findViewById(R.id.person_fragment_iv_attention);
                    Relationship relationship = new Relationship(userMe.getPhoneNumber(), otheruser.getPhoneNumber());
                    relationshipServiceApi = RetrofitManager.getInstance().create(RelationshipServiceApi.class);
                    Call<HttpData<Relationship>> call = relationshipServiceApi.isFollowing(relationship);
                    call.enqueue(new Callback<HttpData<Relationship>>() {
                        @Override
                        public void onResponse(Call<HttpData<Relationship>> call, Response<HttpData<Relationship>> response) {
                            if (response.isSuccessful()){
                                HttpData<Relationship> data = response.body();
                                if (data.getCode()==200){
                                    editablesource.setImageResource(R.drawable.already_attention);
                                    alreadyAttention=true;
                                }else if(data.getCode()==401){
                                    editablesource.setImageResource(R.drawable.attention);
                                    alreadyAttention=false;
                                }
                            }else {
                                Toasty.error(getContext(), "response isfail", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<HttpData<Relationship>> call, Throwable t) {
                            Toasty.error(getContext(), "response onFailure", Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }
            }
            getParams2View(otheruser);
            userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
            userManager.countFollowing(otheruser, new OnSaveUserListener() {
                @Override
                public void OnSaveUserListener(User user) {
                }
                @Override
                public void OnCount(Long count) {
                    attentionNumber.setText(count + "");
                }
            });
            userManager.countFans(otheruser, new OnSaveUserListener() {
                @Override
                public void OnSaveUserListener(User user) {
                }
                @Override
                public void OnCount(Long count) {
                    fensiNumber.setText(count + "");
                }
            });
            isFromIntent = true;
            headIcon(otheruser);
        } else {
            rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
            initView(rootView);
            UserServiceApi userServiceApi1 = RetrofitManager.getInstance().create(UserServiceApi.class);
            userManager.saveUser(phoneNumber, new OnSaveUserListener() {
                @Override
                public void OnSaveUserListener(User user) {
                    userfromIDE = new User();
                    userfromIDE=user;
                }
                @Override
                public void OnCount(Long count) {
                }
            });
            getParams2View(userMe);
            headIcon(userMe);
            userManager.countFollowing(userMe, new OnSaveUserListener() {
                @Override
                public void OnSaveUserListener(User user) {
                }
                @Override
                public void OnCount(Long count) {
                    attentionNumber.setText(count + "");
                }
            });
            userManager.countFans(userMe, new OnSaveUserListener() {
                @Override
                public void OnSaveUserListener(User user) {
                }
                @Override
                public void OnCount(Long count) {
                    fensiNumber.setText(count + "");
                }
            });
            userManager.countFriends(userMe, new OnSaveUserListener() {
                @Override
                public void OnSaveUserListener(User user) {
                }
                @Override
                public void OnCount(Long count) {
                    friendNumber.setText(count+"");
                }
            });
            ismyslef = true;
            isFromIntent = false;
        }
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
    private void initView(View rootView) {
        userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
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
            friend = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend);
            friendNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend_number);
            friendNumber.setText(String.valueOf(countFriends));
        } else {
            if (userPhoneNumber.equals(phoneNumber)) {
                friend = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend);
                friendNumber = (TextView) rootView.findViewById(R.id.person_fragment_tv_friend_number);
                userManager.countFriends(otheruser, new OnSaveUserListener() {
                    @Override
                    public void OnSaveUserListener(User user) {
                    }
                    @Override
                    public void OnCount(Long count) {
                        friendNumber.setText(count + "");
                    }
                });
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
            private Relationship relationship;
            @Override
            public void onClick(View v) {
                if(phoneNumber!=null){
                    System.out.println(userMe+""+otheruser);
                    if (phoneNumber.equals(userPhoneNumber)) {//newpost和sp的电话号
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        intent.putExtra("user", userMe);
                        startActivityForResult(intent, EDITABLEACTIVITY_BACK);

                    } else {
                        relationship = new Relationship(userMe.getPhoneNumber(), otheruser.getPhoneNumber());
                        if(alreadyAttention){
                            alreadyAttention = false;
                            editablesource.setImageResource(R.drawable.attention);
                            relationship = new Relationship(userMe.getPhoneNumber(), otheruser.getPhoneNumber());
// 取消关注操作
                            relationshipServiceApi.unfollowUser(relationship).enqueue(new Callback<HttpData<Relationship>>() {
                                @Override
                                public void onResponse(Call<HttpData<Relationship>> call, Response<HttpData<Relationship>> response) {
                                    if (response.isSuccessful()) {
                                        // 取消关注成功，更新关注者和粉丝的数量
                                        userManager.countFans(otheruser, new OnSaveUserListener() {
                                            @Override
                                            public void OnSaveUserListener(User user) {

                                            }

                                            @Override
                                            public void OnCount(Long count) {
                                                fensiNumber.setText(count+"");
                                            }
                                        });
                                    } else {
                                        // 处理错误情况
                                    }
                                }

                                @Override
                                public void onFailure(Call<HttpData<Relationship>> call, Throwable t) {
                                    // 处理网络错误
                                }
                            });


                        }else {
                            alreadyAttention = true;
                            editablesource.setImageResource(R.drawable.already_attention);

                            // 关注操作
                            relationshipServiceApi.followUser(relationship).enqueue(new Callback<HttpData<Relationship>>() {
                                @Override
                                public void onResponse(Call<HttpData<Relationship>> call, Response<HttpData<Relationship>> response) {
                                    if (response.isSuccessful()) {
                                        // 关注成功，更新关注者和粉丝的数量

                                        userManager.countFans(otheruser, new OnSaveUserListener() {
                                            @Override
                                            public void OnSaveUserListener(User user) {

                                            }

                                            @Override
                                            public void OnCount(Long count) {
                                                fensiNumber.setText(count+"");
                                            }
                                        });
                                    } else {
                                        // 处理错误情况
                                    }
                                }

                                @Override
                                public void onFailure(Call<HttpData<Relationship>> call, Throwable t) {
                                    // 处理网络错误
                                }
                            });

                        }
                        userManager.countFans(otheruser, new OnSaveUserListener() {
                            @Override
                            public void OnSaveUserListener(User user) {
                            }
                            @Override
                            public void OnCount(Long count) {
                                fensiNumber.setText(count + "");
                            }
                        });
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
                        intent.putExtra("postPhoneNumber", phoneNumber);
                    }else {
                        intent.putExtra("postPhoneNumber",userPhoneNumber);
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
                    intent.putExtra("postPhoneNumber", phoneNumber);
                }else {
                    intent.putExtra("postPhoneNumber",userPhoneNumber);
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
                    intent.putExtra("postPhoneNumber", phoneNumber);
                }else {
                    intent.putExtra("postPhoneNumber",userPhoneNumber);
                }
                startActivity(intent);
            }
        });
    }
    private void initData() {
        mData.add("发布");
        mData.add("点赞");
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (userPhoneNumber.equals(phoneNumber)) {
//            userManager.countFollowing(userMe, new OnSaveUserListener() {
//                @Override
//                public void OnSaveUserListener(User user) {
//                }
//                @Override
//                public void OnCount(Long count) {
//                    attentionNumber.setText(count + "");
//                }
//            });
//            userManager.countFans(userMe, new OnSaveUserListener() {
//                @Override
//                public void OnSaveUserListener(User user) {
//                }
//                @Override
//                public void OnCount(Long count) {
//                    fensiNumber.setText(count + "");
//                }
//            });
//            userManager.countFriends(userMe, new OnSaveUserListener() {
//                @Override
//                public void OnSaveUserListener(User user) {
//                }
//                @Override
//                public void OnCount(Long count) {
//                    friendNumber.setText(count+"");
//                }
//            });
//        }else {
//            userManager.countFollowing(otheruser, new OnSaveUserListener() {
//                @Override
//                public void OnSaveUserListener(User user) {
//                }
//                @Override
//                public void OnCount(Long count) {
//                    attentionNumber.setText(count + "");
//                }
//            });
//            userManager.countFans(otheruser, new OnSaveUserListener() {
//                @Override
//                public void OnSaveUserListener(User user) {
//                }
//                @Override
//                public void OnCount(Long count) {
//                    fensiNumber.setText(count + "");
//                }
//            });
//        }
//    }
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
