package com.george.memoshareapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;

import java.util.ArrayList;

public class CoverPhotoSelectionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private TextView ok;
    private ArrayList<Parcelable> uriList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_photo_selection);
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList<Parcelable> uriList = getIntent().getParcelableArrayListExtra("uriList");


    }
    public int getListCount(ArrayList<User> uriList){
        int count =uriList.size();
        return count;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Handle the selected image
            Uri selectedImageUri = data.getData();

            // Return the selected image Uri back to the calling activity
            Intent resultIntent = new Intent();
            resultIntent.setData(selectedImageUri);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}