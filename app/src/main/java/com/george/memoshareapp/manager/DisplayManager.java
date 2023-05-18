package com.george.memoshareapp.manager;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.DetailPhotoRecycleViewAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.utils.CustomItemDecoration;

import org.litepal.LitePal;

import java.util.List;

public class DisplayManager {
    private int offset = 0;
    private final int limit = 10;
    Context Context;
    private DetailPhotoRecycleViewAdapter detailPhotoRecycleViewAdapter;

    public DisplayManager(Context context) {
        this.Context = context;
    }
    public DisplayManager(){
    }


    public void showPhoto(RecyclerView recyclerView,List<String> photoPath, Context context) {


        switch (photoPath.size()) {
            case 1:
                recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
                break;
            case 2:
            case 4:
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                break;
            default:
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                break;
        }
        recyclerView.setHasFixedSize(true);
        detailPhotoRecycleViewAdapter = new DetailPhotoRecycleViewAdapter(context,photoPath);
        recyclerView.setAdapter(detailPhotoRecycleViewAdapter);
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size);
        recyclerView.addItemDecoration(new CustomItemDecoration(spacingInPixels));

    }





        public List<Post> getPostList() {
            List<Post> postList = LitePal.where("ispublic = ?", "1")
                    .limit(limit)
                    .offset(offset)
                    .find(Post.class, true);
            offset += limit;
            return postList;
        }

        public void resetOffset () {
            this.offset = 0;

        }


    }
