package com.george.memoshareapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import com.george.memoshareapp.R;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.view
 * @className: MyCheckBox
 * @author: George
 * @description: TODO
 * @date: 2023/4/24 22:28
 * @version: 1.0
 */


public class MyCheckBox extends androidx.appcompat.widget.AppCompatCheckBox implements View.OnClickListener {
    private boolean isClicked = false;
    private int defaultBackgroundResource = R.drawable.my_checkbox;
    private int clickedBackgroundResource = R.drawable.my_checkbox_click;

    public MyCheckBox(Context context) {
        this(context, null);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(defaultBackgroundResource);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        isClicked = !isClicked;
        setBackgroundResource(isClicked ? clickedBackgroundResource : defaultBackgroundResource);
    }
}

