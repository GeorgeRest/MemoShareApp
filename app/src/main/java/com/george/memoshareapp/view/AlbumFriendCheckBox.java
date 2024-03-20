package com.george.memoshareapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.george.memoshareapp.R;

public class AlbumFriendCheckBox extends AppCompatCheckBox implements View.OnClickListener {
    private boolean isClicked = false;
    private int defaultBackgroundResource = R.drawable.checkbox_album_unchecked;
    private int clickedBackgroundResource = R.drawable.checkbox_album_checked;

    private OnCheckedChangeListener listener;

    public AlbumFriendCheckBox(Context context) {
        this(context, null);
    }

    public AlbumFriendCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumFriendCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(defaultBackgroundResource);
        setOnClickListener(this);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        isClicked = !isClicked;
        setBackgroundResource(isClicked ? clickedBackgroundResource : defaultBackgroundResource);
        if (listener != null) {
            listener.onCheckedChanged(this, isClicked);
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(AlbumFriendCheckBox checkBox, boolean isChecked);
    }
}
