package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ContactListAdapter;
import com.george.memoshareapp.beans.ContactInfo;
import com.george.memoshareapp.view.LetterIndexView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ContactListActivity extends AppCompatActivity {

    private List<ContactInfo> contacts = new ArrayList<>();
    private ListView lv_contact_list;
    private ContactListAdapter contactListAdapter;
//    private TextInputEditText sv_search;
    private LetterIndexView letterIndexView;
    private TextView tv_show_letter_toast;

    private FrameLayout searchLayout;
    private ImageView ivCsGlass;
    private TextView tvCsSearch;
    private TextInputEditText etSearch;
    private RelativeLayout rl_text_before_layout;
    private View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initView();
        initDate();
        sortContacts(contacts); // 按拼音首字母排序
        contactListAdapter = new ContactListAdapter(this,contacts);
        lv_contact_list.setAdapter(contactListAdapter);

        lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("name",contacts.get(position).getName());
                setResult(ReleaseActivity.RESULT_CODE_CONTACT,intent);
                finish();   //关闭页面,回传结果
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
                int sectionForPosition = contactListAdapter.getSectionForPosition(firstVisibleItem);
                letterIndexView.updateLetterIndexView(sectionForPosition);
            }
        });
        setupSearchView();
    }

    private void initView() {
        lv_contact_list = (ListView) findViewById(R.id.lv_contact_list);
        letterIndexView = (LetterIndexView) findViewById(R.id.letter_index_view);
        tv_show_letter_toast = (TextView) findViewById(R.id.tv_show_letter_toast);

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
//        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
//                int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//
//                // 输入法的高度大于 100，认为输入法打开了
//                if(heightDiff - contentViewTop > 100) {
//                    // 输入法打开状态
//                    rl_text_before_layout.setVisibility(View.GONE);
//                    etSearch.setVisibility(View.VISIBLE);
//                } else {
//                    // 输入法关闭状态
//                    rl_text_before_layout.setVisibility(View.VISIBLE);
//                    etSearch.setVisibility(View.GONE);
//                }
//            }
//        });

    }




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
//                if(!TextUtils.isEmpty(newText)){
//                    letterIndexView.setVisibility(View.GONE);
//                } else{
//                    letterIndexView.setVisibility(View.VISIBLE);
//                }
//                filterContacts(newText);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                rl_text_before_layout.setVisibility(View.VISIBLE);
//            }
//        });
//
//        searchLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rl_text_before_layout.setVisibility(View.GONE);
//                etSearch.setVisibility(View.VISIBLE);
//                etSearch.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
//
////        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
////            @Override
////            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
////                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////                    // 在这里处理搜索逻辑
////                    String newText = v.toString();
////                    filterContacts(newText);
////
////                    // 搜索完成后，隐藏 EditText，显示自定义搜索框
////                    etSearch.setVisibility(View.GONE);
////                    searchLayout.setVisibility(View.VISIBLE);
////                    etSearch.clearFocus();
////                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
////                    return true;
////                }
////                return false;
////            }
////        });
//    }

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

//        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    rl_text_before_layout.setVisibility(View.VISIBLE);
//                    etSearch.setVisibility(View.GONE);
//                    etSearch.setText("");
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
//                    return true;
//                }
//                return false;
//            }
//        });


    }
    private void filterContacts(String query) {
        List<ContactInfo> filteredList = new ArrayList<>();
        for (ContactInfo contact : contacts) {
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardLayoutListener);
//    }

    private void sortContacts(List<ContactInfo> contacts) {
        Collections.sort(contacts, new Comparator<ContactInfo>() {
            @Override
            public int compare(ContactInfo c1, ContactInfo c2) {
                return c1.getPinyin().compareToIgnoreCase(c2.getPinyin());
            }
        });
    }
    private void initDate(){
        contacts.add(new ContactInfo("张三",R.mipmap.photo_1));
        contacts.add(new ContactInfo("李潇",R.mipmap.photo_2));
        contacts.add(new ContactInfo("唐莉",R.mipmap.photo_3));
        contacts.add(new ContactInfo("程思迪",R.mipmap.photo_4));
        contacts.add(new ContactInfo("Audss",R.mipmap.photo_5));
        contacts.add(new ContactInfo("王五",R.mipmap.photo_6));
        contacts.add(new ContactInfo("CC",R.mipmap.photo_7));
        contacts.add(new ContactInfo("张明敏",R.mipmap.photo_8));
        contacts.add(new ContactInfo("lilies",R.mipmap.photo_9));
        contacts.add(new ContactInfo("大师",R.mipmap.photo_10));
        contacts.add(new ContactInfo("历史老师",R.mipmap.photo_2));
        contacts.add(new ContactInfo("Kato",R.mipmap.photo_7));
        contacts.add(new ContactInfo("seven",R.mipmap.photo_5));
        contacts.add(new ContactInfo("吴仪",R.mipmap.photo_1));
        contacts.add(new ContactInfo("李宏",R.mipmap.photo_3));
        contacts.add(new ContactInfo("高倩倩",R.mipmap.photo_10));
        contacts.add(new ContactInfo("福福",R.mipmap.photo_4));
        contacts.add(new ContactInfo("小庞",R.mipmap.photo_9));
        contacts.add(new ContactInfo("***",R.mipmap.photo_6));
    }

    public void onClick(View view) {
        finish();
    }


}