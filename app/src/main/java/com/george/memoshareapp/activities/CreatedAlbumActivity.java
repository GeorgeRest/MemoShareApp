package com.george.memoshareapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.dialog.UriListDialog;
import com.george.memoshareapp.manager.AlbumManager;
import com.george.memoshareapp.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.util.ArrayList;
import java.util.List;

public class CreatedAlbumActivity extends AppCompatActivity {
    private List<Uri> uriPathList=new ArrayList<>();
    private RecyclerView recyclerView;
    private PictureAdapter pictureAdapter;
    private EditText description;
    private int maxLength = 100; // 你想要限制的最大字数
    private EditText album_name;
    private TextView tv_photo_complete;
    private ImageView releaseBack;
    private TextView tv_complete;
    private static final int REQUEST_CODE_SELECT_COVER_PHOTO = 102;
    private UriListDialog uriListDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_album);
        initView();
        jumpToPictureSelector();
    }

    private void initView() {
        recyclerView = findViewById(R.id.picture_recyclerView);
        description = findViewById(R.id.album_description);
        album_name = findViewById(R.id.album_name);
        tv_photo_complete = findViewById(R.id.tv_photo_complete);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        pictureAdapter = new PictureAdapter(uriPathList);
        recyclerView.setAdapter(pictureAdapter);
        releaseBack = findViewById(R.id.release_back);
        tv_complete = findViewById(R.id.tv_photo_complete);
        releaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 添加文本变化监听器
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable editable) {
               int currentLength = editable.length();
                if (currentLength > maxLength) {
                    String limitedText = editable.toString().substring(0, maxLength);
                    description.setText(limitedText);
                    description.setSelection(maxLength);
                }
            }
        });
        album_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(album_name.getText().toString())) {
                    tv_photo_complete.setTextColor(ContextCompat.getColor(CreatedAlbumActivity.this, R.color.green)); // 替换为你想要的颜色
                } else {
                    tv_photo_complete.setTextColor(ContextCompat.getColor(CreatedAlbumActivity.this, R.color.gray));
                }
            }
        });
        tv_photo_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(album_name.getText().toString())){
                    Toast.makeText(CreatedAlbumActivity.this, "请选择一张作为相册封面", Toast.LENGTH_LONG).show();
                    setAlbumFirstPhoto();//设置封皮
                }else {
                    Toast.makeText(CreatedAlbumActivity.this, "请填入相册名称", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void setAlbumFirstPhoto() {
        uriListDialog = new UriListDialog(CreatedAlbumActivity.this, uriPathList,
                new UriListDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(Uri uri) {
                        // 处理选中的Uri，firstAlbumPhoto=1
//                        Toast.makeText(CreatedAlbumActivity.this, uri+"", Toast.LENGTH_SHORT).show();
                        getData2AlbumManager(uri);
                        uriListDialog.dismiss();
                    }
                });
        uriListDialog.show();
    }

    private void getData2AlbumManager(Uri uri) {
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "");
        String albumName = album_name.getText().toString();
        String albumDescription = description.getText().toString();

        AlbumManager albumManager = new AlbumManager(this);
        albumManager.saveAlbumAndPhoto2DB(phoneNumber,albumName,albumDescription,uriPathList,uri);

    }

    private void jumpToPictureSelector() {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofAll())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(20)
                .isEmptyResultReturn(true)
                .isMaxSelectEnabledMask(true)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> arrayList) {

                        for (int i = 0; i < arrayList.size(); i++) {
                            Uri uri = Uri.parse(arrayList.get(i).getPath());
                            uriPathList.add(uri);
                            System.out.println("7777777777777--"+ uriPathList);
                        }
                        pictureAdapter.notifyDataSetChanged(); // 通知适配器数据发生了变化

                    }

                    @Override
                    public void onCancel() {

                    }
                });

    }


    private class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
        private List<Uri> picturePaths;
        public PictureAdapter(List<Uri> picturePaths) {
            if (picturePaths != null) {
                this.picturePaths = picturePaths;
            } else {
                this.picturePaths = new ArrayList<>();
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Uri uri = picturePaths.get(position);
            Glide.with(holder.imageView)
                    .load(uri)
                    .thumbnail(Glide.with(CreatedAlbumActivity.this).load(R.drawable.photo_loading))
                    .error(R.drawable.ic_close)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ArrayList<Uri> list = new ArrayList<>();
                        Intent intent = new Intent(CreatedAlbumActivity.this, FullScreenImageActivity.class);
                        for (Uri  uri:uriPathList) {
                            list.add(uri);
                        }
                        intent.putParcelableArrayListExtra("photoPath", list);
                        intent.putExtra("position", position);
                        CreatedAlbumActivity.this.startActivity(intent);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return picturePaths.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}





