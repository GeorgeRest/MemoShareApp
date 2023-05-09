package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ContactListAdapter;
import com.george.memoshareapp.beans.ContactInfo;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private List<ContactInfo> contacts = new ArrayList<>();
    private ListView lv_contact_list;
    private ContactListAdapter contactListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        lv_contact_list = (ListView) findViewById(R.id.lv_contact_list);
        contactListAdapter = new ContactListAdapter(this,contacts);
        lv_contact_list.setAdapter(contactListAdapter);
        initDate();
    }

    private void initDate(){
        contacts.add(new ContactInfo("a",R.mipmap.app_icon));
        contacts.add(new ContactInfo("aa",R.mipmap.app_icon));
        contacts.add(new ContactInfo("aaaa",R.mipmap.app_icon));
        contacts.add(new ContactInfo("b",R.mipmap.app_icon));
        contacts.add(new ContactInfo("bbb",R.mipmap.app_icon));
        contacts.add(new ContactInfo("bbbb",R.mipmap.app_icon));
        contacts.add(new ContactInfo("c",R.mipmap.app_icon));
        contacts.add(new ContactInfo("cc",R.mipmap.app_icon));
        contacts.add(new ContactInfo("cccc",R.mipmap.app_icon));
        contacts.add(new ContactInfo("d",R.mipmap.app_icon));
        contacts.add(new ContactInfo("dd",R.mipmap.app_icon));
        contacts.add(new ContactInfo("ee",R.mipmap.app_icon));
        contacts.add(new ContactInfo("eeee",R.mipmap.app_icon));
        contacts.add(new ContactInfo("eeee",R.mipmap.app_icon));
        contacts.add(new ContactInfo("ff",R.mipmap.app_icon));
        contacts.add(new ContactInfo("fffff",R.mipmap.app_icon));
        contacts.add(new ContactInfo("ffff",R.mipmap.app_icon));
        contacts.add(new ContactInfo("g",R.mipmap.app_icon));
        contacts.add(new ContactInfo("ggg",R.mipmap.app_icon));


    }
    public void onClick(View view) {
        finish();
    }


}