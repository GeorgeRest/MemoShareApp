package com.george.memoshareapp.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.utils
 * @className: KeyBoardUtil
 * @author: George
 * @description: TODO
 * @date: 2023/11/27 14:45
 * @version: 1.0
 */
public class KeyBoardUtil {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // 找到当前获取焦点的视图，以确保可以正确地隐藏键盘
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
