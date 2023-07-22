package com.george.memoshareapp.activities;

//public class ContactListActivity extends AppCompatActivity implements ContactListAdapter.OnContactsSelectedListener {
//
//    private List<ContactInfo> contacts = new ArrayList<>();
//    private List<ContactInfo> contacts1 = new ArrayList<>();
//    private ListView lv_contact_list;
//    private ContactListAdapter contactListAdapter;
//    private LetterIndexView letterIndexView;
//    private TextView tv_show_letter_toast;
//    private HorizontalAdapter horizontalAdapter;
//    private FrameLayout searchLayout;
//    private ImageView ivCsGlass;
//    private TextView tvCsSearch;
//    private TextInputEditText etSearch;
//    private RelativeLayout rl_text_before_layout;
//    private View rootLayout;
//    private RecyclerView horizontal_recycler_view;
//    private Button btn_add_contacts_complete;
//    private ImageView photo_chat_name_dialog_iv;
//    private Button btn_submit;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_list);
//
//        initView();
//        initDate();
//        sortContacts(contacts); // 按拼音首字母排序
//
//        horizontalAdapter = new HorizontalAdapter(contacts1);
//
//        contactListAdapter = new ContactListAdapter(this, contacts, horizontalAdapter, horizontal_recycler_view);
//
//
//
//        contactListAdapter.setOnContactsSelectedListener(this);
//        lv_contact_list.setAdapter(contactListAdapter);
//        horizontal_recycler_view.setAdapter(horizontalAdapter);
//
//
//
//        lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });
//
//        letterIndexView.setTextViewDialog(tv_show_letter_toast);
//        letterIndexView.setUpdateListView(new LetterIndexView.UpdateListView() {
//            @Override
//            public void updateListView(String currentChar) {
//                int positionForSection = contactListAdapter.getPositionForSection(currentChar.charAt(0));
//                lv_contact_list.setSelection(positionForSection);
//            }
//        });
//
//        lv_contact_list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int sectionForPosition = contactListAdapter.getSectionForPosition(firstVisibleItem);
//                letterIndexView.updateLetterIndexView(sectionForPosition);
//            }
//        });
//
//
//        setupSearchView();
//    }
//
//    private void initView() {
//        photo_chat_name_dialog_iv = (ImageView) findViewById(R.id.photo_chat_name_dialog_iv);
//        btn_submit = (Button) findViewById(R.id.btn_submit);
//        btn_add_contacts_complete = (Button) findViewById(R.id.btn_add_contacts_complete);
//        lv_contact_list = findViewById(R.id.lv_contact_list);
//        letterIndexView = findViewById(R.id.letter_index_view);
//        tv_show_letter_toast = findViewById(R.id.tv_show_letter_toast);
//        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
//
//        searchLayout = findViewById(R.id.contact_search_layout);
//        View customSearchView = LayoutInflater.from(this).inflate(R.layout.custom_search_view, searchLayout, false);
//        ivCsGlass = customSearchView.findViewById(R.id.iv_cs_glass);
//        tvCsSearch = customSearchView.findViewById(R.id.tv_cs_search);
//        etSearch = customSearchView.findViewById(R.id.et_search);
//        rl_text_before_layout = customSearchView.findViewById(R.id.rl_text_before_layout);
//        searchLayout.addView(customSearchView);
//
//        // 先初始化搜索框的可见性
//        ivCsGlass.setVisibility(View.VISIBLE);
//        tvCsSearch.setVisibility(View.VISIBLE);
//        etSearch.setVisibility(View.GONE);
//
//        rootLayout = findViewById(android.R.id.content);
//        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                rootLayout.getWindowVisibleDisplayFrame(r);
//                int screenHeight = rootLayout.getRootView().getHeight();
//
//                int keypadHeight = screenHeight - r.bottom;
//
//                if (keypadHeight < screenHeight * 0.15) {
//                    ivCsGlass.setVisibility(View.VISIBLE);
//                    tvCsSearch.setVisibility(View.VISIBLE);
//                    etSearch.setVisibility(View.GONE);
//                } else {
//                    ivCsGlass.setVisibility(View.GONE);
//                    tvCsSearch.setVisibility(View.GONE);
//                    etSearch.setVisibility(View.VISIBLE);
//                    etSearch.requestFocus();
//                }
//            }
//        });
//        btn_add_contacts_complete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //弹出一个输入相册名字的框
//                onCompletionClicked(v);
//
//            }
//        });
//    }
//    public void onCompletionClicked(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
//        builder.setView(dialogView);
//
//        EditText etCustomInput = dialogView.findViewById(R.id.et_custom_input);
//        Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
//
//        AlertDialog dialog = builder.create();
//
//        etCustomInput.addTextChangedListener(new TextWatcher() {
//             @Override
//             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//             @Override
//             public void onTextChanged(CharSequence s, int start, int before, int count) {
//                 photo_chat_name_dialog_iv = dialogView.findViewById(R.id.photo_chat_name_dialog_iv);
//                 if (etCustomInput.getText().toString().length()>0){
//                     photo_chat_name_dialog_iv.setImageResource(R.mipmap.photo_chat_name_ok);
//                     btnSubmit.setOnClickListener(new View.OnClickListener() {
//                         @Override
//                         public void onClick(View v) {
//
//                             String input = etCustomInput.getText().toString();
//
//
//                             List<ContactInfo> contactList = new ArrayList<ContactInfo>();
//                             contactList = horizontalAdapter.getContacts();
//                             Intent intent = new Intent(getBaseContext(), ChatGroupActivity.class);
//                             intent.putExtra("contact_list", (Serializable) contactList);
//                             intent.putExtra("photo_chat_name", input);
//
//                             startActivity(intent);
//
//                             finish();
//
//                             dialog.dismiss();  // Close the dialog
//                         }
//                     });
//
//                 }
//                 if (etCustomInput.getText().toString().length()==0){
//                     photo_chat_name_dialog_iv.setImageResource(R.mipmap.photo_chat_name);
//                 }
//                 if (etCustomInput.getText().toString().length()>12){
//                     Toasty.warning(ContactListActivity.this,"名称不能超过11个字");
//                 }
//             }
//             @Override
//             public void afterTextChanged(Editable s) {}
//        });
//
//
//        dialog.show();
//    }
//
//    private void setupSearchView() {
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Intentionally left blank
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String newText = s.toString();
//                if (!TextUtils.isEmpty(newText)) {
//                    letterIndexView.setVisibility(View.GONE);
//                } else {
//                    letterIndexView.setVisibility(View.VISIBLE);
//                }
//                filterContacts(newText);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Intentionally left blank
//            }
//        });
//
//        rl_text_before_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ivCsGlass.setVisibility(View.GONE);
//                tvCsSearch.setVisibility(View.GONE);
//                etSearch.setVisibility(View.VISIBLE);
//                etSearch.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
//    }
//
//    private void filterContacts(String query) {
//        List<ContactInfo> filteredList = new ArrayList<>();
//        for (ContactInfo contact : contacts) {
//            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
//                filteredList.add(contact);
//            }
//        }
//        if (!filteredList.isEmpty()) {
//            contactListAdapter.setData(filteredList);
//            contactListAdapter.notifyDataSetChanged();
//
//            lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent();
//                    intent.putExtra("name", filteredList.get(position).getName());
//                    setResult(ReleaseActivity.RESULT_CODE_CONTACT, intent);
//                    finish();
//                }
//            });
//        } else {
//            Toasty.warning(this, "没有找到" + "'" + query + "'" + "相关结果", Toast.LENGTH_SHORT, true).show();
//        }
//    }
//
//    private void sortContacts(List<ContactInfo> contacts) {
//        Collections.sort(contacts, new Comparator<ContactInfo>() {
//            @Override
//            public int compare(ContactInfo c1, ContactInfo c2) {
//                return c1.getPinyin().compareToIgnoreCase(c2.getPinyin());
//            }
//        });
//    }
//
//    private void initDate() {
//        contacts.add(new ContactInfo("张三", R.mipmap.photo_1));
//        contacts.add(new ContactInfo("李潇", R.mipmap.photo_2));
//        contacts.add(new ContactInfo("唐莉", R.mipmap.photo_3));
//        contacts.add(new ContactInfo("程思迪", R.mipmap.photo_4));
//        contacts.add(new ContactInfo("Audss", R.mipmap.photo_5));
//        contacts.add(new ContactInfo("王五", R.mipmap.photo_6));
//        contacts.add(new ContactInfo("CC", R.mipmap.photo_7));
//        contacts.add(new ContactInfo("张明敏", R.mipmap.photo_8));
//        contacts.add(new ContactInfo("lilies", R.mipmap.photo_9));
//        contacts.add(new ContactInfo("大师", R.mipmap.photo_10));
//        contacts.add(new ContactInfo("历史老师", R.mipmap.photo_2));
//        contacts.add(new ContactInfo("Kato", R.mipmap.photo_7));
//        contacts.add(new ContactInfo("seven", R.mipmap.photo_5));
//        contacts.add(new ContactInfo("吴仪", R.mipmap.photo_1));
//        contacts.add(new ContactInfo("李宏", R.mipmap.photo_3));
//        contacts.add(new ContactInfo("高倩倩", R.mipmap.photo_10));
//        contacts.add(new ContactInfo("福福", R.mipmap.photo_4));
//        contacts.add(new ContactInfo("小庞", R.mipmap.photo_9));
//        contacts.add(new ContactInfo("***", R.mipmap.photo_6));
//
//
////        contacts1.add(new ContactInfo("高倩倩", R.mipmap.photo_10));
////        contacts1.add(new ContactInfo("福福", R.mipmap.photo_4));
////        contacts1.add(new ContactInfo("小庞", R.mipmap.photo_9));
////        contacts1.add(new ContactInfo("***", R.mipmap.photo_6));
//    }
//
//    public void onClick(View view) {
//        finish();
//    }
//
//    @Override
//    public void onContactsSelected(boolean[] selectedItems) {
//
//    }
//}

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
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.manager.RetrofitManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initView();

        state.setEmptyLayout(R.layout.layout_empty);
        state.setErrorLayout(R.layout.layout_error);
        state.setLoadingLayout(R.layout.layout_loading);
        state.showLoading(null, false, false);

        horizontalAdapter = new HorizontalAdapter(userList);

        contactListAdapter = new ContactListAdapter(this, userList, horizontalAdapter, horizontal_recycler_view);
        contactListAdapter.setOnContactsSelectedListener(this);
        lv_contact_list.setAdapter(contactListAdapter);
        horizontal_recycler_view.setAdapter(horizontalAdapter);

        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        String phoneNumber = sp.getString("phoneNumber", "");
        getFriendUserList(phoneNumber);

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
        rootLayout = findViewById(android.R.id.content);
        state = (StateLayout)findViewById(R.id.state);

        btn_add_contacts_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出一个输入相册名字的框
                onCompletionClicked(v);

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


                             List<User> userList = new ArrayList<User>();
                             userList = horizontalAdapter.getContacts();
                             Intent intent = new Intent(getBaseContext(), ChatGroupActivity.class);
                             intent.putExtra("contact_list", (Serializable) userList);
                             intent.putExtra("photo_chat_name", input);

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

    public void onClick(View view) {
        finish();
    }

    @Override
    public void onContactsSelected(boolean[] selectedItems) {

    }
}


