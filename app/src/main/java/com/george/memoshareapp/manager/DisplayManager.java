package com.george.memoshareapp.manager;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ImageAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.utils.CustomItemDecoration;

import org.litepal.LitePal;

import java.util.List;

public class DisplayManager {
    private ImageAdapter imageAdapter;
    private int offset = 0;
    private final int limit = 10;
    Context Context;
    public DisplayManager(Context context) {
        this.Context = context;
    }
    public DisplayManager(){
    }


    public void showPhoto(RecyclerView recyclerView,List<Uri> photoPath, Context context) {

        if (photoPath.size() == 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        }
        if (photoPath.size() > 1) {
            if (photoPath.size() < 4) {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            }
            if (photoPath.size() == 4) {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            }
            if (photoPath.size() > 4 && photoPath.size() < 9) {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            }
            recyclerView.setHasFixedSize(true);
            photoPath.add(null);
            imageAdapter = new ImageAdapter(context, photoPath);
            recyclerView.setAdapter(imageAdapter);
            int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size);
            recyclerView.addItemDecoration(new CustomItemDecoration(spacingInPixels));
        }
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
