package com.george.memoshareapp.beans;

import com.george.memoshareapp.utils.ChinesetoPinyin;
import com.george.memoshareapp.utils.PinyinFirstLetter;

import net.sourceforge.pinyin4j.PinyinHelper;

public class ContactInfo {
    private String name;
    private int picture;
    private String pinyin;
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

}
