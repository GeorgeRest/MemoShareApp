package com.george.memoshareapp.beans;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: TextViewAndButton
 * @author: George
 * @description: TODO
 * @date: 2023/5/13 20:23
 * @version: 1.0
 */
public class TextViewAndButton {
    public TextView textView;
    public ImageView deleteButton;

    public TextViewAndButton(TextView textView, ImageView deleteButton) {
        this.textView = textView;
        this.deleteButton = deleteButton;
    }
}

