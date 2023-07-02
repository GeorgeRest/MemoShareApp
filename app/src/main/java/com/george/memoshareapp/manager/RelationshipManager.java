package com.george.memoshareapp.manager;

import android.content.Context;
import android.util.Log;

import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.http.api.RelationshipServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.interfaces.OnSaveUserListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelationshipManager {
    private Context context;
    private RelationshipServiceApi relationshipServiceApi;

    public RelationshipManager(Context context) {
        this.context = context;
    }


    public void isFollowings(Relationship relationship, OnSaveUserListener onSaveUserListener) {
        RelationshipServiceApi relationshipServiceApi = RetrofitManager.getInstance().create(RelationshipServiceApi.class);
        Call<HttpData<Relationship>> call = relationshipServiceApi.isFollowing(relationship);
        call.enqueue(new Callback<HttpData<Relationship>>() {
            @Override
            public void onResponse(Call<HttpData<Relationship>> call, Response<HttpData<Relationship>> response) {
                if (response.isSuccessful()){
                    HttpData<Relationship> data = response.body();
                    if (data.getCode()==200){
                        onSaveUserListener.OnCount(Long.valueOf(data.getCode()));
                        Log.d("isFollowing", "code: " + data.getCode());

                    }else if(data.getCode()==401){
                        onSaveUserListener.OnCount(Long.valueOf(data.getCode()));
                        Log.d("isFollowing", "code: " + data.getCode());
                    }
                }else {
                    Log.d("isFollowing", "onResponse: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<HttpData<Relationship>> call, Throwable t) {
                Log.d("isFollowing", "onResponse: " + t.getMessage());
            }
        });

    }


}
