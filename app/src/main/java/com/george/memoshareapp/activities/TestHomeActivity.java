//package com.george.memoshareapp.activities;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.george.memoshareapp.R;
//import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestHomeActivity extends AppCompatActivity {
//    private RecyclerView outerRecyclerView;
//    private HomeWholeRecyclerViewAdapter outerAdapter;
//    private static final int PICK_IMAGE = 1;
//    private List<String> mSelectedImages = new ArrayList<>();
//    private Button btnTest;
//    @Override
//    protected void onCreate (Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_activity_home);
//
//        outerRecyclerView = findViewById(R.id.whole_recycler);
//
//        // assuming you have the data ready
//        List<List<String>> data = new ArrayList<>();
//
////        outerAdapter = new HomeWholeRecyclerViewAdapter(this, data);
//        outerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        outerRecyclerView.setAdapter(outerAdapter);
//
//        btnTest = findViewById(R.id.btn_test);
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//            }
//        });
//    }
//
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
////            Uri uri = data.getData();
////            String uriString = uri.toString();
////            mSelectedImages.add(uriString);
////
////            // Update the last item in the RecyclerView with the new image
////            List<String> lastItem = outerAdapter.getLastItem();
////            if (lastItem != null) {
////                lastItem.add(uriString);
////                outerAdapter.notifyItemChanged(outerAdapter.getItemCount() - 1);
////            } else {
////                List<String> newItem = new ArrayList<>();
////                newItem.add(uriString);
////                outerAdapter.addItem(newItem);
////                outerAdapter.notifyItemInserted(outerAdapter.getItemCount() - 1);
////            }
////        }
////    }
//
//}
