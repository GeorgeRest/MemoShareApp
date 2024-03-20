package com.george.memoshareapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.InformAdapter;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.http.api.CommentServiceApi;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.manager.RetrofitManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InformActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String phoneNumber;
    private List<CommentBean> informList;
    private ListView lv_inform_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        init();
        getInformData(phoneNumber);
    }
    private void init() {
        lv_inform_list = (ListView) findViewById(R.id.lv_inform_list);
    }
    private List<CommentBean> getInformData(String phoneNumber) {
        informList = new ArrayList<>();
        CommentServiceApi serviceApi = RetrofitManager.getInstance().create(CommentServiceApi.class);
        serviceApi.getInform(phoneNumber).enqueue(new Callback<HttpListData<CommentBean>>() {
            @Override
            public void onResponse(Call<HttpListData<CommentBean>> call, Response<HttpListData<CommentBean>> response) {
                if (response.isSuccessful()) {
                    HttpListData<CommentBean> body = response.body();
                    List<CommentBean> items = body.getItems();
                    informList.addAll(items);
                    InformAdapter informAdapter = new InformAdapter(getApplicationContext(), informList);
                    lv_inform_list.setAdapter(informAdapter);
                }else {
                    Toast.makeText(InformActivity.this, "response is not Successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpListData<CommentBean>> call, Throwable t) {
                Toast.makeText(InformActivity.this, "onFailure", Toast.LENGTH_SHORT).show();

            }
        });
        return null;
    }

}