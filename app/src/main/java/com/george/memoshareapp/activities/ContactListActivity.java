package com.george.memoshareapp.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.statelayout.StateLayout;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ContactListAdapter;
import com.george.memoshareapp.adapters.HorizontalAdapter;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.ChatRoomMember;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.ChatRoomApi;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.ChinesetoPinyin;
import com.george.memoshareapp.view.LetterIndexView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactListActivity extends AppCompatActivity implements ContactListAdapter.OnContactsSelectedListener{

    private ListView lv_contact_list;
    private ContactListAdapter contactListAdapter;
    private LetterIndexView letterIndexView;
    private TextView tv_show_letter_toast;
    private List<User> userList = new ArrayList<>();
    private StateLayout state;

    private FrameLayout searchLayout;
    private ImageView ivCsGlass;
    private TextView tvCsSearch;
    private TextInputEditText etSearch;
    private RelativeLayout rl_text_before_layout;
    private View rootLayout;
    private HorizontalAdapter horizontalAdapter;
    private RecyclerView horizontal_recycler_view;
    private ImageView photo_chat_name_dialog_iv;
    private Button btn_submit;
    private Button btn_add_contacts_complete;
    private List<User> enableAddChatMemberList;
    private List<User> alreadyExitContactsList;
    private boolean comeFromChatGroupMoreActivity;
    private String chatTitleName;
    private List<User> friendList;
    private Intent intent;
    private ImageView back;
    private String phoneNumber;
    private int chatRoomId;
    private int chatRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initView();
        intent = getIntent();
        comeFromChatGroupMoreActivity = intent.getBooleanExtra("comeFromChatGroupMoreActivity", false);
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        phoneNumber = sp.getString("phoneNumber", "");

        if (intent.getBooleanExtra("ComeFromCalendarTripFragment",false)){
            getFriendUserList(phoneNumber);

            state.setEmptyLayout(R.layout.layout_empty);
            state.setErrorLayout(R.layout.layout_error);
            state.setLoadingLayout(R.layout.layout_loading);
            state.showLoading(null, false, false);
        }


        if (comeFromChatGroupMoreActivity){
            chatRoomID = intent.getIntExtra("ChatRoomID", -1);
            chatTitleName = intent.getStringExtra("chatTitleName");
            alreadyExitContactsList = (List<User>) intent.getSerializableExtra("alreadyExitContacts");
            friendList = (List<User>) intent.getSerializableExtra("FriendList");
            System.out.println("===comeFromChatGroupMoreActivity到conatctlist"+ friendList);
            List<User> usersNotInAlreadyExitContactsList = new ArrayList<>();


            for (User u: friendList) {
                boolean userExists = false;
                for (User existingUser : alreadyExitContactsList) {
                    if (u.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
                        userExists = true;
                        break;
                    }
                }
                if (!userExists) {
                    usersNotInAlreadyExitContactsList.add(u);
                }

            }




            horizontalAdapter = new HorizontalAdapter(userList,this);
            contactListAdapter = new ContactListAdapter(this, usersNotInAlreadyExitContactsList, horizontalAdapter, horizontal_recycler_view);

            contactListAdapter.setOnContactsSelectedListener(this);
            lv_contact_list.setAdapter(contactListAdapter);
            horizontal_recycler_view.setAdapter(horizontalAdapter);

        }else {
            horizontalAdapter = new HorizontalAdapter(userList,this);
            contactListAdapter = new ContactListAdapter(this, userList, horizontalAdapter, horizontal_recycler_view);
            contactListAdapter.setOnContactsSelectedListener(this);
            lv_contact_list.setAdapter(contactListAdapter);
            horizontal_recycler_view.setAdapter(horizontalAdapter);
        }







        lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        letterIndexView.setTextViewDialog(tv_show_letter_toast);
        letterIndexView.setUpdateListView(new LetterIndexView.UpdateListView() {
            @Override
            public void updateListView(String currentChar) {
                int positionForSection =contactListAdapter.getPositionForSection(currentChar.charAt(0));
                lv_contact_list.setSelection(positionForSection);
            }
        });

        lv_contact_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < contactListAdapter.getCount()) {
                    int sectionForPosition = contactListAdapter.getSectionForPosition(firstVisibleItem);
                    letterIndexView.updateLetterIndexView(sectionForPosition);
                }
            }
        });
        setupSearchView();
    }

    private void initView() {
        photo_chat_name_dialog_iv = (ImageView) findViewById(R.id.photo_chat_name_dialog_iv);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_add_contacts_complete = (Button) findViewById(R.id.btn_add_contacts_complete);
        back = (ImageView) findViewById(R.id.iv_back);
        lv_contact_list = (ListView) findViewById(R.id.lv_contact_list);
        letterIndexView = (LetterIndexView) findViewById(R.id.letter_index_view);
        tv_show_letter_toast = (TextView) findViewById(R.id.tv_show_letter_toast);
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        searchLayout = findViewById(R.id.contact_search_layout);
        View customSearchView = LayoutInflater.from(this).inflate(R.layout.custom_search_view, searchLayout, false);
        ivCsGlass = customSearchView.findViewById(R.id.iv_cs_glass);
        tvCsSearch = customSearchView.findViewById(R.id.tv_cs_search);
        etSearch = customSearchView.findViewById(R.id.et_search);
        rl_text_before_layout = customSearchView.findViewById(R.id.rl_text_before_layout);

        searchLayout.addView(customSearchView);

        // 先初始化搜索框的可见性
        ivCsGlass.setVisibility(View.VISIBLE);
        tvCsSearch.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comeFromChatGroupMoreActivity){
                    Intent intent1 = new Intent(ContactListActivity.this, ChatGroupMoreActivity.class);
                    intent1.putExtra("addedContactList",(Serializable) alreadyExitContactsList);
                    intent1.putExtra("chatTitleName",chatTitleName);
                    intent1.putExtra("ChatRoomID",chatRoomID);
                    intent1.putExtra("comeFromContactListActivity",true);
                    intent1.putExtra("FriendList",(Serializable) friendList);
                    startActivity(intent1);
                    finish();

                }else {
                    finish();
                }
            }
        });
        rootLayout = findViewById(android.R.id.content);
        state = (StateLayout)findViewById(R.id.state);

        btn_add_contacts_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comeFromChatGroupMoreActivity){

                    List<User> addedContactList = horizontalAdapter.getContacts();//点击添加好友选的

                    addedContactList.addAll(alreadyExitContactsList);
                    //应该把新增加的用户传回去，传给ChatGroupMoreActivity
                    Intent intent = new Intent(ContactListActivity.this, ChatGroupMoreActivity.class);
                    intent.putExtra("addedContactList",(Serializable) addedContactList);
                    intent.putExtra("chatTitleName",chatTitleName);
                    intent.putExtra("FriendList",(Serializable) friendList);
                    intent.putExtra("ChatRoomID",chatRoomID);
                    intent.putExtra("comeFromContactListActivity",true);
//加人
                    addContactListToIDE(addedContactList);


                    startActivity(intent);
                    finish();
                }else {
                    //弹出一个输入相册名字的框
                    onCompletionClicked(v);

                }


            }
        });
    }

    private void addContactListToIDE(List<User> addedContactList) {
        ChatRoomApi chatRoomApi = RetrofitManager.getInstance().create(ChatRoomApi.class);
        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        for (User u: addedContactList) {
            ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoomID, u.getPhoneNumber(),0);
            chatRoomMemberList.add(chatRoomMember);

        }
        Call<HttpData<ChatRoom>> call = chatRoomApi.AddChatRoomMember(chatRoomMemberList);
        call.enqueue(new Callback<HttpData<ChatRoom>>() {
            @Override
            public void onResponse(Call<HttpData<ChatRoom>> call, Response<HttpData<ChatRoom>> response) {
                // 请求成功的处理逻辑
                if (response.isSuccessful()) {


                } else {
                    // 请求失败的处理逻辑
                    // ... 处理错误 ...
                }
            }

            @Override
            public void onFailure(Call<HttpData<ChatRoom>> call, Throwable t) {
                // 请求失败的处理逻辑
                // ... 处理错误 ...
            }
        });
    }

    public void onCompletionClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);

        EditText etCustomInput = dialogView.findViewById(R.id.et_custom_input);
        Button btnSubmit = dialogView.findViewById(R.id.btn_submit);

        AlertDialog dialog = builder.create();

        etCustomInput.addTextChangedListener(new TextWatcher() {

            private ImageView photo_chat_name_dialog_iv;

            @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 photo_chat_name_dialog_iv = dialogView.findViewById(R.id.photo_chat_name_dialog_iv);
                 if (etCustomInput.getText().toString().length()>0){
                     photo_chat_name_dialog_iv.setImageResource(R.mipmap.photo_chat_name_ok);
                     btnSubmit.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                             String input = etCustomInput.getText().toString();
                             List<User> alreadyCheckedUserList = new ArrayList<User>();
                             alreadyCheckedUserList = horizontalAdapter.getContacts();

                             createChatRoom(alreadyCheckedUserList,input);

                             Intent intent = new Intent(getBaseContext(), ChatGroupActivity.class);
                             intent.putExtra("contact_list", (Serializable) alreadyCheckedUserList);
                             intent.putExtra("FriendList", (Serializable) userList);
                             intent.putExtra("photo_chat_name", input);
                             intent.putExtra("ChatRoomID", chatRoomId);



                             startActivity(intent);

                             finish();

                             dialog.dismiss();  // Close the dialog
                         }
                     });

                 }
                 if (etCustomInput.getText().toString().length()==0){
                     photo_chat_name_dialog_iv.setImageResource(R.mipmap.photo_chat_name);
                 }
                 if (etCustomInput.getText().toString().length()>12){
                     Toasty.warning(ContactListActivity.this,"名称不能超过11个字");
                 }
             }
             @Override
             public void afterTextChanged(Editable s) {}
        });


        dialog.show();
    }


    private void createChatRoom(List<User> alreadyCheckedUserList, String input) {
        // 创建一个聊天室对象，填充需要的数据
        ChatRoom chatRoom = new ChatRoom();
        if (alreadyCheckedUserList.size()>0){
            chatRoom.setType("多人");
        }else {
            chatRoom.setType("单人");
        }
        chatRoom.setName(input);
//        chatRoom.setAvatar();//设置拼接头像

        // 获取ChatRoomController实例

        ChatRoomApi chatRoomApi = RetrofitManager.getInstance().create(ChatRoomApi.class);

        // 发送创建聊天室的请求
        Call<HttpData<ChatRoom>> call = chatRoomApi.createChatRoom(chatRoom);
        call.enqueue(new Callback<HttpData<ChatRoom>>() {
            @Override
            public void onResponse(Call<HttpData<ChatRoom>> call, Response<HttpData<ChatRoom>> response) {
                // 请求成功的处理逻辑
                if (response.isSuccessful()) {
                    HttpData<ChatRoom> httpData = response.body();
                    if (httpData != null && httpData.getData() != null) {
                        ChatRoom createdChatRoom = httpData.getData();
                        // 在这里处理创建成功后的逻辑，获取聊天室的ID等信息
                        chatRoomId = createdChatRoom.getId();
                        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
                        for (User u:alreadyCheckedUserList) {
                            ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoomId,u.getPhoneNumber(),0);
                            chatRoomMemberList.add(chatRoomMember);
                        }
                        User user = new UserManager(ContactListActivity.this).findUserByPhoneNumber(phoneNumber);
                        ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoomId,user.getPhoneNumber(),1);

                        chatRoomMemberList.add(chatRoomMember);
                        chatRoomApi.AddChatRoomMember(chatRoomMemberList);


                    }
//                    Intent intent = new Intent(getBaseContext(), ChatGroupActivity.class);
//                    intent.putExtra("contact_list", (Serializable) alreadyCheckedUserList);
//                    intent.putExtra("FriendList", (Serializable) userList);
//                    intent.putExtra("photo_chat_name", input);
//                    intent.putExtra("ChatRoomID", chatRoomId);
//                    startActivity(intent);
//                    finish();
//                    dialog.dismiss();
                } else {
                    // 请求失败的处理逻辑
                    // ... 处理错误 ...
                }
            }

            @Override
            public void onFailure(Call<HttpData<ChatRoom>> call, Throwable t) {
                // 请求失败的处理逻辑
                // ... 处理错误 ...
            }
        });
    }




    private void sortContacts(List<User> users){
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                String u1_name = ChinesetoPinyin.getPinyin(u1.getName());
                String u2_name = ChinesetoPinyin.getPinyin(u2.getName());

                return u1_name.compareToIgnoreCase(u2_name);
            }
        });
    }

    private void setupSearchView() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if(!TextUtils.isEmpty(newText)){
                    letterIndexView.setVisibility(View.GONE);
                } else{
                    letterIndexView.setVisibility(View.VISIBLE);
                }
                filterContacts(newText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }
        });

        rl_text_before_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCsGlass.setVisibility(View.GONE);
                tvCsSearch.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });


    }
    private void filterContacts(String query) {
        List<User> filteredList = new ArrayList<>();
        for (User contact : userList) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {//contains()方法是判断字符串中是否包含指定的字符
                filteredList.add(contact);
            }
        }
        if(!filteredList.isEmpty()) {
            contactListAdapter.setData(filteredList);
            contactListAdapter.notifyDataSetChanged();

            lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("name",filteredList.get(position).getName());
                    setResult(ReleaseActivity.RESULT_CODE_CONTACT,intent);
                    finish();
                }
            });
        }else{
            Toasty.warning(this, "没有找到"+"'"+query+"'"+"相关结果", Toast.LENGTH_SHORT,true).show();
        }
    }

    private void getFriendUserList(String phoneNumber) {
        UserServiceApi serviceApi = RetrofitManager.getInstance().create(UserServiceApi.class);
        Call<HttpListData<User>> friendUserCall = serviceApi.getFriendUser(phoneNumber);
        friendUserCall.enqueue(new Callback<HttpListData<User>>() {
            @Override
            public void onResponse(Call<HttpListData<User>> call, Response<HttpListData<User>> response) {
                state.showContent(null);
                userList = response.body().getItems();
                System.out.println(userList.size()+"-=============");
                sortContacts(userList); // 按拼音首字母排序
                // 设置数据给 contactListAdapter 对象
                contactListAdapter.setData(userList);
                contactListAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<HttpListData<User>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onContactsSelected(boolean[] selectedItems) {

    }
}


