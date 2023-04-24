package com.george.memoshareapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
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
@SuppressLint("AppCompatCustomView")
public class MyCheckBox extends CheckBox {
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
    }

    @Override
    public boolean performClick() {
        isClicked = !isClicked;
        setBackgroundResource(isClicked ? clickedBackgroundResource : defaultBackgroundResource);
        return super.performClick();
    }

}

