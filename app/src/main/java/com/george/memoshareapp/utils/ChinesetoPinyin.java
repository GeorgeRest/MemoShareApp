package com.george.memoshareapp.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

public class ChinesetoPinyin {

    //将 Contact 类中的 name 字段转换为拼音，并存储在 pinyin 字段中。
    // 在 getPinyin() 方法中，使用 PinyinHelper 类的 toHanyuPinyinStringArray() 方法将中文字符转换为拼音数组，并将第一个拼音首字母添加到 StringBuilder 中。
    public static String getPinyin(String name) {
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
}
