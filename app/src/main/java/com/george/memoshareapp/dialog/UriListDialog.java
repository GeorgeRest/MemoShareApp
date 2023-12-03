package com.george.memoshareapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.UriListAdapter;

import java.util.List;

public class UriListDialog extends Dialog {
    private List<Uri> uriList;
    private OnItemClickListener itemClickListener;

    public UriListDialog(@NonNull Context context, List<Uri> uriList, OnItemClickListener itemClickListener) {
        super(context);
        this.uriList = uriList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_uri_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        UriListAdapter adapter = new UriListAdapter(uriList, itemClickListener);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
    }

    public interface OnItemClickListener {
        void onItemClick(Uri uri);
    }
}
