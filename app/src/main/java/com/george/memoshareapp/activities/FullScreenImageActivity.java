package com.george.memoshareapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.UriViewPager2Adapter;
import com.george.memoshareapp.adapters.ViewPager2Adapter;

import java.util.ArrayList;

public class FullScreenImageActivity extends AppCompatActivity {

    private TextView tv_setFirstPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        if (getIntent().getBooleanExtra("DetailPhotoRecyclerViewAdapter",false)){
            ArrayList<String> photoPath = getIntent().getStringArrayListExtra("photoPath");
            int position = getIntent().getIntExtra("position", 0);
            ViewPager2Adapter adapter = new ViewPager2Adapter(photoPath);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position, false);
        }else {
            ArrayList<Uri> photoPath = getIntent().getParcelableArrayListExtra("photoPath");
            int position = getIntent().getIntExtra("position", 0);
            UriViewPager2Adapter adapter = new UriViewPager2Adapter(photoPath);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position, false);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


