package com.george.memoshareapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ViewPager2Adapter;

import java.util.ArrayList;

public class FullScreenImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ArrayList<String> photoPath = getIntent().getStringArrayListExtra("photoPath");
        int position = getIntent().getIntExtra("position", 0);

        // Adapter for ViewPager2
        ViewPager2Adapter adapter = new ViewPager2Adapter(photoPath);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position, false);
    }
}


