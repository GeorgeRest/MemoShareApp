package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ContactListAdapter;
import com.george.memoshareapp.beans.ContactInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private List<ContactInfo> contacts = new ArrayList<>();
    private ListView lv_contact_list;
    private ContactListAdapter contactListAdapter;
    private SearchView sv_search;
    private ListView lv_abc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        lv_contact_list = (ListView) findViewById(R.id.lv_contact_list);
        sv_search = (SearchView) findViewById(R.id.sv_search);
        lv_abc = (ListView) findViewById(R.id.lv_ABC);

        contactListAdapter = new ContactListAdapter(this,contacts);
        lv_contact_list.setAdapter(contactListAdapter);
        lv_contact_list.setFastScrollEnabled(true); // 启用快速滚动

        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: 实现搜索逻辑
                return true;
            }
        });
        setupLettersNavigation();
        initDate();
        sortContacts(contacts); // 按拼音首字母排序
        contactListAdapter.setContacts(contacts); // 将排序后的数据设置到适配器中
    }

    private void sortContacts(List<ContactInfo> contacts) {
        Collections.sort(contacts, new Comparator<ContactInfo>() {
            @Override
            public int compare(ContactInfo c1, ContactInfo c2) {
                return c1.getPinyin().compareToIgnoreCase(c2.getPinyin());
            }
        });
    }
    private void initDate(){
        contacts.add(new ContactInfo("张三",R.mipmap.app_icon));
        contacts.add(new ContactInfo("李潇",R.mipmap.app_icon));
        contacts.add(new ContactInfo("唐莉",R.mipmap.app_icon));
        contacts.add(new ContactInfo("程思迪",R.mipmap.app_icon));
        contacts.add(new ContactInfo("Audss",R.mipmap.app_icon));
        contacts.add(new ContactInfo("王五",R.mipmap.app_icon));
        contacts.add(new ContactInfo("CC",R.mipmap.app_icon));
        contacts.add(new ContactInfo("张明敏",R.mipmap.app_icon));
        contacts.add(new ContactInfo("lilies",R.mipmap.app_icon));
        contacts.add(new ContactInfo("大师",R.mipmap.app_icon));
        contacts.add(new ContactInfo("历史老师",R.mipmap.app_icon));
        contacts.add(new ContactInfo("Kato",R.mipmap.app_icon));
        contacts.add(new ContactInfo("seven",R.mipmap.app_icon));
        contacts.add(new ContactInfo("吴仪",R.mipmap.app_icon));
        contacts.add(new ContactInfo("李宏",R.mipmap.app_icon));
        contacts.add(new ContactInfo("高倩倩",R.mipmap.app_icon));
        contacts.add(new ContactInfo("福福",R.mipmap.app_icon));
        contacts.add(new ContactInfo("小庞",R.mipmap.app_icon));
        contacts.add(new ContactInfo("***",R.mipmap.app_icon));


    }
    private void setupLettersNavigation() {
        LinearLayout lettersLayout = findViewById(R.id.letters_layout);
        for (int i = 0; i < lettersLayout.getChildCount(); i++) {
            TextView letterTextView = (TextView) lettersLayout.getChildAt(i);
            letterTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    char letter = ((TextView) v).getText().charAt(0);
                    int position = getPositionForLetter(letter);
                    if (position >= 0) {
                        lv_abc.setSelection(position);
                    }
                }
            });
        }
    }

//    private int getPositionForLetter(char letter) {
//        for (int i = 0; i < contacts.size(); i++) {
//            String contact = String.valueOf(contacts.get(i));
//            if (contact != null && !contact.isEmpty() && Character.toUpperCase(contact.charAt(0)) == letter) {
//                return i;
//            }
//        }
//        return -1; // 如果找不到对应的字母，返回-1
//    }

    private int getPositionForLetter(char letter) {
        for (int i = 0; i < contacts.size(); i++) {
            ContactInfo contact = contacts.get(i);
            if (contact != null && Character.toUpperCase(contact.getName().charAt(0)) == letter) {
                return i;
            }
        }
        return -1; // 如果找不到对应的字母，返回-1
    }


    public void onClick(View view) {
        finish();
    }

}