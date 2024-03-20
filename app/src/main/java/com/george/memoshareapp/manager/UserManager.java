package com.george.memoshareapp.manager;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.interfaces.OnSaveUserListener;
import com.george.memoshareapp.interfaces.UpdateUserListener;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @projectName: MemoShare
 * @package: com.george.memoshareApp.manager
 * @className: UserManager
 * @author: George
 * @description: TODO
 * @date: 2023/4/23 14:11
 * @version: 1.0
 */
public class UserManager {
    private Context context;
    private UserServiceApi apiService;
    private int DATABASE_VERSION=100;

    public UserManager(Context context) {
        this.context = context;
    }

    /**
     * @param phone
     * @param pw
     * @param pwAgain
     * @param vcCode
     * @param codePhone 提交时验证是否和获取验证码的手机一致
     * @return
     */
    public boolean checkUserInfo(String phone, String pw, String pwAgain, String vcCode, String codePhone) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(vcCode) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(pwAgain)) {
            Toasty.info(context, "请输入完整信息", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (!phone.equals(codePhone) || !pw.equals(pwAgain)) {   //todo   验证码判断未加
            Toasty.error(context, "信息输入有误，请重新输入", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }


    public User isPhoneNumberRegistered(String phone) {
        LitePal.getDatabase();
        return LitePal.select("id")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
    }

    public boolean changePassword(String phone, String pw) {
        LitePal.getDatabase();
        User user = LitePal.select("id, phoneNumber, password")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
        user.setPassword(pw);
        return user.save();
    }

    public boolean queryUserInfo(String phone, String pw) {
        User user = LitePal.select("id, phoneNumber, password")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
        if (user == null) {
            Toasty.info(context, "请先注册", Toast.LENGTH_SHORT, true).show();
//            user = new User();
//            user.setPhoneNumber(phone);
//            user.setPassword(pw);
//            user.save();
            return false;
        }
        if (!user.getPassword().equals(pw)) {
            Toasty.error(context, "密码错误", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    public boolean queryUser(String phone) {
        User user = LitePal.select("id, phoneNumber, password")
                .where("phoneNumber = ?", phone)
                .findFirst(User.class);
        if (user == null) {
            Toasty.info(context, "请先注册", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    // 检查initiator是否关注了target
    public boolean isFollowing(User initiator, User target) {
        long count = LitePal.where("initiatorNumber = ? and targetNumber = ? " +
                                "and (relationshipStatus = ? or relationshipStatus = ?)",
                        String.valueOf(initiator.getPhoneNumber()),
                        String.valueOf(target.getPhoneNumber()),
                        String.valueOf(Relationship.ATTENTION_STATUS),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);

        return count > 0;
    }

    // 当前用户关注其他用户
    public void followUser(User initiator, User target) {
        Relationship relationship = new Relationship();
        relationship.setInitiatorNumber(initiator.getPhoneNumber());
        relationship.setTargetNumber(target.getPhoneNumber());
        relationship.setRelationshipStatus(Relationship.ATTENTION_STATUS);
        relationship.save();

        // 检查是否已经满足成为朋友的条件
        if (isMutualFollow(initiator, target)) {
            becomeFriends(initiator, target);
        }
    }

    // 当前用户取消关注
    public void unfollowUser(User initiator, User target) {
        LitePal.deleteAll(Relationship.class, "initiatorNumber = ? and targetNumber = ? and relationshipStatus = ?",
                String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()),
                String.valueOf(Relationship.ATTENTION_STATUS));

        // 检查是否需要解除朋友关系
        if (!isMutualFollow(initiator, target)) {
            endFriendship(initiator, target);
        }
    }




    public User findUserByPhoneNumber(String phoneNumber) {
        User users = LitePal
                .where("phonenumber = ?", phoneNumber)
                .findFirst(User.class);
        if (users != null) {
            return users;
        } else {
            return null;
        }
    }
//    public void updateUserInfo(User updateUser,String phoneNumber){
//        LitePal.getDatabase();
//        User user = new User( updateUser.getName(), updateUser.getSignature(),updateUser.getGender(), updateUser.getBirthday(), updateUser.getRegion());
//        user.updateAll("phoneNumber = ?", user.getPhoneNumber());
//    }

    // 检查是否满足互相关注的条件
    private boolean isMutualFollow(User initiator, User target) {
        long count1 = LitePal.where("initiatorNumber = ? and targetNumber = ? and relationshipStatus = ?", String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()), String.valueOf(Relationship.ATTENTION_STATUS)).count(Relationship.class);
        long count2 = LitePal.where("initiatorNumber = ? and targetNumber = ? and relationshipStatus = ?", String.valueOf(target.getPhoneNumber()), String.valueOf(initiator.getPhoneNumber()), String.valueOf(Relationship.ATTENTION_STATUS)).count(Relationship.class);

        return count1 > 0 && count2 > 0;
    }

    // 两个用户互相关注，成为朋友
    private void becomeFriends(User initiator, User target) {
        // 需要将之前的关注状态更新为朋友状态
        ContentValues values = new ContentValues();
        values.put("relationshipStatus", Relationship.FRIEND_STATUS);
        LitePal.updateAll(Relationship.class, values, "initiatorNumber = ? and targetNumber = ?", String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()));
        LitePal.updateAll(Relationship.class, values, "initiatorNumber = ? and targetNumber = ?", String.valueOf(target.getPhoneNumber()), String.valueOf(initiator.getPhoneNumber()));
    }

    // 解除朋友关系
    private void endFriendship(User initiator, User target) {
        // 只要有一方取消关注，就解除朋友关系
        ContentValues values = new ContentValues();
        values.put("relationshipStatus", Relationship.ATTENTION_STATUS);
        LitePal.updateAll(Relationship.class, values, "initiatorNumber = ? and targetNumber = ?",String.valueOf(target.getPhoneNumber()) , String.valueOf(initiator.getPhoneNumber()));
        LitePal.deleteAll(Relationship.class, "initiatorNumber = ? and targetNumber = ? and relationshipStatus = ?", String.valueOf(initiator.getPhoneNumber()), String.valueOf(target.getPhoneNumber()), String.valueOf(Relationship.FRIEND_STATUS));

    }

    public long countFollowing(User user) {
        return LitePal.where("initiatorNumber = ? and (relationshipStatus = ? or relationshipStatus = ?)",
                        String.valueOf(user.getPhoneNumber()),
                        String.valueOf(Relationship.ATTENTION_STATUS),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);
    }


    public long countFans(User user) {
        return LitePal.where("targetNumber = ? and (relationshipStatus = ? or relationshipStatus = ?)",
                        String.valueOf(user.getPhoneNumber()),
                        String.valueOf(Relationship.ATTENTION_STATUS),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);
    }


    public long countFriends(User user) {
        return LitePal.where("initiatorNumber = ? and relationshipStatus = ?",
                        String.valueOf(user.getPhoneNumber()),
                        String.valueOf(Relationship.FRIEND_STATUS))
                .count(Relationship.class);
    }


    //获取关注列表用户
    public List<User> getFollowedUser(String userPhoneNumber) {
        List<User> followedUserList = new ArrayList<>();
        LitePal.getDatabase();
        List<Relationship> list = LitePal
                .where("initiatorNumber = ? in (relationshipStatus =? or relationshipStatus =?)", userPhoneNumber, "1", "3")
                .find(Relationship.class);

        for (Relationship relationship : list) {
            String targetNumber = relationship.getTargetNumber();
            User user = LitePal
                    .where("phoneNumber = ?", targetNumber)
                    .findFirst(User.class);
            followedUserList.add(user);
        }
        return followedUserList;
    }

    //获取粉丝列表用户
    public List<User> getFansUser(String userPhoneNumber){
        List<User> fansUserList  = new ArrayList<>();
        LitePal.getDatabase();
        List<Relationship> list = LitePal
                .where("targetNumber = ? in(relationshipStatus =? or relationshipStatus =?)", userPhoneNumber, "1", "3")
                .find(Relationship.class);

        for (Relationship relationship : list) {
            String targetNumber = relationship.getInitiatorNumber();
            User user = LitePal
                    .where("phoneNumber = ?", targetNumber)
                    .findFirst(User.class);
            fansUserList.add(user);
        }
        return fansUserList;
    }

    //获取好友列表用户
    public List<User> getFriendUser(String userPhoneNumber) {
        List<User> friendUserList = new ArrayList<>();
        LitePal.getDatabase();
        List<Relationship> list = LitePal
                .where("initiatorNumber = ? and relationshipStatus =? ", userPhoneNumber, "3")
                .find(Relationship.class);
        for (Relationship relationship : list) {
            String targetNumber = relationship.getTargetNumber();
            User user = LitePal
                    .where("phoneNumber = ?", targetNumber)
                    .findFirst(User.class);
            friendUserList.add(user);
        }
        return friendUserList;
    }

    //判断两人关系
    public int getStatus(String targetPhoneNumber, String initPhoneNumber) {
        int relationship = 0;
        Relationship relationship1 = LitePal.where("initiatorNumber = ? and targetNumber = ?", initPhoneNumber, targetPhoneNumber).findFirst(Relationship.class);
        relationship = relationship1.getRelationshipStatus();

        return relationship;
    }

    public void saveUserToLocal(String phoneNumber) {
        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<HttpData<User>> call = userServiceApi.getUserByPhoneNumber(phoneNumber);
        call.enqueue(new Callback<HttpData<User>>() {
            @Override
            public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
                if (response.code() == 200) {
                    Log.d("saveUserToLocal", "onResponse: " + response.code());
                    User user = response.body().getData();
                    User existingUser = findUserByPhoneNumber(user.getPhoneNumber());
                    if (existingUser != null) {
                        // 用户存在，进行更新操作
                        existingUser.setName(user.getName());
                        existingUser.setSignature(user.getSignature());
                        existingUser.setGender(user.getGender());
                        existingUser.setBirthday(user.getBirthday());
                        existingUser.setRegion(user.getRegion());
                        existingUser.setHeadPortraitPath(user.getHeadPortraitPath());
                        existingUser.save();
                    } else {
                        user.save();
                    }
                } else {
                    Log.d("saveUserToLocal", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<HttpData<User>> call, Throwable t) {
                Log.d("saveUserToLocal", "onFailure: " + t.getMessage());
            }
        });
    }
    public void saveUser(String phoneNumber, OnSaveUserListener onSaveUserListener) {
        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<HttpData<User>> call = userServiceApi.getUserByPhoneNumber(phoneNumber);
        call.enqueue(new Callback<HttpData<User>>() {
            @Override
            public void onResponse(Call<HttpData<User>> call, Response<HttpData<User>> response) {
                if (response.code() == 200) {
                    Log.d("saveUserToLocal", "onResponse: " + response.code());
                    User user = response.body().getData();
                    onSaveUserListener.OnSaveUserListener(user);
                }
                else{
                    Log.d("saveUserToLocal", "onResponse: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<HttpData<User>> call, Throwable t) {
                Log.d("saveUserToLocal", "onFailure: " + t.getMessage());
            }
        });

    }


public void countFollowing(User user, OnSaveUserListener onSaveUserListener) {
    UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
    Call<Long> call = userServiceApi.countFollowing(user.getPhoneNumber());
    call.enqueue(new Callback<Long>() {
        @Override
        public void onResponse(Call<Long> call, Response<Long> response) {
            if (response.isSuccessful()){
                long count = response.body(); // 获取实际的关注者数量
                onSaveUserListener.OnCount(count); // 传递关注者数量
            } else {
                onSaveUserListener.OnCount(Long.valueOf(123));
            }
        }

        @Override
        public void onFailure(Call<Long> call, Throwable t) {
            onSaveUserListener.OnCount(Long.valueOf(1234));
        }
    });
}


    public void countFans(User user, OnSaveUserListener onSaveUserListener) {
        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<Long> call = userServiceApi.countFans(user.getPhoneNumber());
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()){
                    long count = response.body(); // 获取实际的关注者数量
                    onSaveUserListener.OnCount(count); // 传递关注者数量
                } else {
                    onSaveUserListener.OnCount(Long.valueOf(123));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                onSaveUserListener.OnCount(Long.valueOf(1234));
            }
        });
    }

    public void countFriends(User  user, OnSaveUserListener onSaveUserListener) {
        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<Long> call = userServiceApi.countFriends(user.getPhoneNumber());
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()){
                    long count = response.body(); // 获取实际的关注者数量
                    onSaveUserListener.OnCount(count); // 传递关注者数量
                } else {
                    onSaveUserListener.OnCount(Long.valueOf(123));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                onSaveUserListener.OnCount(Long.valueOf(1234));
            }
        });
    }
    public void updateUserInfo(User user, MultipartBody.Part headImage, UpdateUserListener updateUserListener) {
        LitePal.getDatabase();
        User userByPhoneNumber = findUserByPhoneNumber(user.getPhoneNumber());
        long userId = userByPhoneNumber.getId();  // 获取主键 id
        user.setId(userId);  // 设置主键 id
        Logger.d(userByPhoneNumber);
        userByPhoneNumber.setGender(user.getGender());
        userByPhoneNumber.setSignature(user.getSignature());
        userByPhoneNumber.setName(user.getName());
        userByPhoneNumber.setRegion(user.getRegion());
        userByPhoneNumber.setBirthday(user.getBirthday());
        userByPhoneNumber.setHeadPortraitPath(user.getHeadPortraitPath());

        ContentValues values = new ContentValues();
        values.put("gender", user.getGender());
        values.put("signature", user.getSignature());
        values.put("name", user.getName());
        values.put("region", user.getRegion());
        values.put("birthday", user.getBirthday());
        values.put("headPortraitPath", user.getHeadPortraitPath());
        values.put("id", userId);  // 设置更新条件

        int updateCount = LitePal.update(User.class, values, 1);
        Logger.d("Update count: " + updateCount);
        Logger.d(user);



        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<Boolean> call = userServiceApi.updateUser(user.getPhoneNumber(),user.getGender(),user.getSignature(),
                user.getName(),user.getRegion(),user.getBirthday(),headImage);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Logger.d("请求成功");
                if(response.isSuccessful()){
                    if(response.body()){
                        updateUserListener.onUpdateUserListener(true);
                    } else {
                        updateUserListener.onUpdateUserListener(false);
                    }
                } else {
                    try {
                        Logger.d("请求失败: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("-------"+t.getMessage());
            }
        });
    }
    public List<User> getAllUsersFromFriendUser() {
        List<User> friendList = LitePal.where("isFriend = ?", "1").find(User.class);

        return friendList;
    }
    public static String getSelfPhoneNumber(Context context){
        SharedPreferences sp =context.getSharedPreferences("User", MODE_PRIVATE);
        return sp.getString("phoneNumber", "");
    }

//    public void switchDatabaseForUser(String userId) {
//        String dbName = "database_" + userId;  // 为每个用户创建一个唯一的数据库名称
//        LitePalDB litePalDB = new LitePalDB(dbName, DATABASE_VERSION);
//        litePalDB.addClassName(...);  // 添加你的数据模型类名
//        // 可以添加更多配置，如设置编码等
//
//        LitePal.use(litePalDB);  // 切换到新的数据库配置
//    }

    public void getAllUsers(){
        UserServiceApi userServiceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        userServiceApi.getAllUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    List<User> users = response.body();
                    for (User user:users) {
                        user.save();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}
