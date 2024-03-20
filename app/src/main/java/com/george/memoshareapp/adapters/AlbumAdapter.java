package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.FullScreenImageActivity;
import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.PhotoInAlbum;
import com.george.memoshareapp.properties.AppProperties;

import java.util.ArrayList;
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
        holder.bind(album);
        //album.setAlbumName("zxp");
        //photoInAlbumList.get(1).setPhoto_path("https://www.bing.com/images/search?view=detailV2&ccid=CyWwrhal&id=9A2DDEB11531D9E97D27536D6FAC47EF8F243750&thid=OIP.CyWwrhallMLQ754v0CBXNgHaF8&mediaurl=https%3a%2f%2fkfwimg.kafan.cn%2fuploadx%2f7a899e510fb30f2443ee0e7dc795d143ad4b0353.jpg&cdnurl=https%3a%2f%2fth.bing.com%2fth%2fid%2fR.0b25b0ae16a594c2d0ef9e2fd0205736%3frik%3dUDckj%252b9HrG9tUw%26pid%3dImgRaw%26r%3d0%26sres%3d1%26sresct%3d1%26srh%3d799%26srw%3d997&exph=401&expw=500&q=ImageView%e5%8f%af%e4%bb%a5%e6%b7%bb%e5%8a%a0%e6%96%87%e5%ad%97%e5%90%97&simid=607997104423330084&FORM=IRPRST&ck=5AA200BB5955FDC0E17652A777FB9C6A&selectedIndex=0&itb=0&ajaxhist=0&ajaxserp=0");
        //Glide.with(context).load("https://www.bing.com/images/search?view=detailV2&ccid=CyWwrhal&id=9A2DDEB11531D9E97D27536D6FAC47EF8F243750&thid=OIP.CyWwrhallMLQ754v0CBXNgHaF8&mediaurl=https%3a%2f%2fkfwimg.kafan.cn%2fuploadx%2f7a899e510fb30f2443ee0e7dc795d143ad4b0353.jpg&cdnurl=https%3a%2f%2fth.bing.com%2fth%2fid%2fR.0b25b0ae16a594c2d0ef9e2fd0205736%3frik%3dUDckj%252b9HrG9tUw%26pid%3dImgRaw%26r%3d0%26sres%3d1%26sresct%3d1%26srh%3d799%26srw%3d997&exph=401&expw=500&q=ImageView%e5%8f%af%e4%bb%a5%e6%b7%bb%e5%8a%a0%e6%96%87%e5%ad%97%e5%90%97&simid=607997104423330084&FORM=IRPRST&ck=5AA200BB5955FDC0E17652A777FB9C6A&selectedIndex=0&itb=0&ajaxhist=0&ajaxserp=0").into(holder.album_cover_image);
        for (PhotoInAlbum p:photoInAlbumList) {
            if (album.getId()==p.getAlbum_id()){
                if(p.getFirstPhoto()==1){

                    Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+p.getPhoto_path()).into(holder.album_cover_image);
                }
            }
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullScreenImageActivity.class);
            //把Albumid对应的PhotoInAlbum的photoPath，拼接好之后传递给FullScreenImageActivity
            List<String> photoInAlbumPathList = new ArrayList<>();
            for (PhotoInAlbum p : photoInAlbumList) {
                if (album.getId() == p.getAlbum_id()) {
                    photoInAlbumPathList.add(AppProperties.SERVER_MEDIA_URL + p.getPhoto_path());
                }
            }
            intent.putExtra("comeFromAlbum",true);
            intent.putExtra("position",position);
            intent.putStringArrayListExtra("imagePathFromAlbum", (ArrayList<String>) photoInAlbumPathList);
            context.startActivity(intent);
        });
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
            albumName.setText(album.getAlbumDescription());//不知到那块把相册名称和相册描述弄错了
            // 更多的绑定逻辑，比如处理相册封面图片等...
            // 获得album的时间，通过时间去找本地album表的id，然后再去找photoalbum表里的对应的封面照片
            //得用Glide吧，传过来的是图片的名字，还得拼接
            //album_cover_image.setImageResource();


        }
    }
}
