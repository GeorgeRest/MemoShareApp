package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.PhotoInAlbum;
import com.george.memoshareapp.properties.AppProperties;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Album> albums;
    private List<PhotoInAlbum> photoInAlbumList;
    private Context context;


    public AlbumAdapter(List<Album> albums, List<PhotoInAlbum> photoInAlbumList, Context context) {
        this.albums = albums;
        this.context=context;
        this.photoInAlbumList=photoInAlbumList;
    }

    public AlbumAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        System.out.println("============="+album);
        System.out.println("============="+photoInAlbumList);
        holder.bind(album);

        for (PhotoInAlbum p:photoInAlbumList) {
            if (album.getId()==p.getAlbum_id()){
                if(p.getFirstPhoto()==1){
                    Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+p.getPhoto_path()).into(holder.album_cover_image);

                    System.out.println("图片地址："+AppProperties.SERVER_MEDIA_URL+p.getPhoto_path());
                    System.out.println("图片地址1："+p.getPhoto_path());
                }
            }
        }
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, FullScreenImageActivity.class);
//            //把Albumid对应的PhotoInAlbum的photoPath，拼接好之后传递给FullScreenImageActivity
//            ArrayList<String> photoInAlbumPathList = new ArrayList<>();
//            for (PhotoInAlbum p : photoInAlbumList) {
//                if (album.getId() == p.getAlbum_id()) {
//                    photoInAlbumPathList.add(AppProperties.SERVER_MEDIA_URL + p.getPhoto_path());
//                }
//            }
//            intent.putStringArrayListExtra("imagePathFromAlbum", photoInAlbumPathList);
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return albums == null ? 0 : albums.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView album_cover_image;
        TextView albumName;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.album_name);
            album_cover_image = itemView.findViewById(R.id.album_cover_image);
        }

        public void bind(Album album) {
            albumName.setText(album.getAlbumName());
            // 更多的绑定逻辑，比如处理相册封面图片等...
            // 获得album的时间，通过时间去找本地album表的id，然后再去找photoalbum表里的对应的封面照片
            //得用Glide吧，传过来的是图片的名字，还得拼接
            //album_cover_image.setImageResource();


        }
    }
}
