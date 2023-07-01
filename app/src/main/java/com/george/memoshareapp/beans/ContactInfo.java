package com.george.memoshareapp.beans;

import com.george.memoshareapp.utils.ChinesetoPinyin;
import com.george.memoshareapp.utils.PinyinFirstLetter;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class ContactInfo extends LitePalSupport {
    private String name;
    private String phoneNumber;
    @Column(ignore = true)
    private int picture;
    @Column(ignore = true)
    private String pinyin;
    @Column(ignore = true)
    private String firstLetter;



    public ContactInfo(){

    }
    public ContactInfo(String name, int picture){
        this.name = name;
        this.picture = picture;
        this.pinyin = ChinesetoPinyin.getPinyin(name);
        this.firstLetter = PinyinFirstLetter.getFirstLetter(pinyin);
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
