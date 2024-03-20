package com.george.memoshareapp.activities;

import android.os.Bundle;

import com.george.memoshareapp.R;

import java.util.concurrent.ExecutorService;

public class MainActivity extends BaseActivity {

    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


}