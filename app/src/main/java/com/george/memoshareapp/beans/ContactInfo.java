package com.george.memoshareapp.beans;

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
        this.pinyin = getPinyin(name);
        this.firstLetter = getFirstLetter(pinyin);
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
    //将 Contact 类中的 name 字段转换为拼音，并存储在 pinyin 字段中。
    // 在 getPinyin() 方法中，使用 PinyinHelper 类的 toHanyuPinyinStringArray() 方法将中文字符转换为拼音数组，并将第一个拼音首字母添加到 StringBuilder 中。
    private String getPinyin(String name) {
        StringBuilder sb = new StringBuilder();

        for (char c : name.toCharArray()) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);

            if (pinyinArray != null && pinyinArray.length > 0) {
                sb.append(pinyinArray[0].charAt(0));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    private String getFirstLetter(String pinyin) {
        char firstChar = pinyin.charAt(0);
        String s = String.valueOf(firstChar).toUpperCase();
        if (s.matches("[A-Z]")) {
            return s;
        }else{
            return "#";
        }
    }

}
