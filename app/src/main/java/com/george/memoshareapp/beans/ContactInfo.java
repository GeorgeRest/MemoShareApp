package com.george.memoshareapp.beans;

import com.george.memoshareapp.utils.ChinesetoPinyin;
import com.george.memoshareapp.utils.PinyinFirstLetter;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class ContactInfo extends LitePalSupport implements Serializable {
    private String name;
    private String phoneNumber;
    private boolean isSelected; // 新添加的字段

    // ...其他字段和方法



    @Column(ignore = true)
    private int picture;
    @Column(ignore = true)
    private String pinyin;
    @Column(ignore = true)
    private String firstLetter;



    public ContactInfo(){

    }
    public ContactInfo(String name,String phoneNumber,int picture){
        this.name = name;
this.picture=picture;
        this.phoneNumber=phoneNumber;
    }
    public ContactInfo(String name, int picture){
        this.name = name;
        this.picture = picture;
        this.pinyin = ChinesetoPinyin.getPinyin(name);
        this.firstLetter = PinyinFirstLetter.getFirstLetter(pinyin);
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
