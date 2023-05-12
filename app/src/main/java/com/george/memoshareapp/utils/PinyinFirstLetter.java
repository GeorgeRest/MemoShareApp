package com.george.memoshareapp.utils;

public class PinyinFirstLetter {
    public static String getFirstLetter(String pinyin) {
        char firstChar = pinyin.charAt(0);
        String s = String.valueOf(firstChar).toUpperCase();
        if (s.matches("[A-Z]")) {
            return s;
        }else{
            return "#";
        }
    }
}
