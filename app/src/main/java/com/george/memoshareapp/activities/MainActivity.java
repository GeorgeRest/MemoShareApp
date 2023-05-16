package com.george.memoshareapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.R;

import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


}