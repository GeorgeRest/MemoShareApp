package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ContactListAdapter;
import com.george.memoshareapp.beans.ContactInfo;
import com.george.memoshareapp.view.LetterIndexView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ContactListActivity extends AppCompatActivity {

    private List<ContactInfo> contacts = new ArrayList<>();
    private ListView lv_contact_list;
    private ContactListAdapter contactListAdapter;
    private SearchView sv_search;
    private LetterIndexView letterIndexView;
    private TextView tv_show_letter_toast;


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
        sv_search = (SearchView) findViewById(R.id.sv_search);
        letterIndexView = (LetterIndexView) findViewById(R.id.letter_index_view);
        tv_show_letter_toast = (TextView) findViewById(R.id.tv_show_letter_toast);
    }


    private void sortContacts(List<ContactInfo> contacts) {
        Collections.sort(contacts, new Comparator<ContactInfo>() {
            @Override
            public int compare(ContactInfo c1, ContactInfo c2) {
                return c1.getPinyin().compareToIgnoreCase(c2.getPinyin());
            }
        });
    }

    private void setupSearchView() {    //搜索业务
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(! TextUtils.isEmpty(newText)){
                    letterIndexView.setVisibility(View.GONE);
                }else{
                    letterIndexView.setVisibility(View.VISIBLE);
                }
                filterContacts(newText);
                return true;
            }
        });
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