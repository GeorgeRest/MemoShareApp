package com.george.memoshareapp.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreatedAlbumActivity extends BaseActivity {
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
    private String realFilePathFromUri;

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
                    Toast.makeText(CreatedAlbumActivity.this, "请选择一张作为相册封面", Toast.LENGTH_SHORT).show();
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
//                        uriListDialog.dismiss();
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
        List<String> AlbumRealPathName = new ArrayList<>();
        List<String> AlbumRealPath = new ArrayList<>();
        for (int i = 0; i < uriPathList.size(); i++) {
            realFilePathFromUri = getRealFilePathFromUri(this, uriPathList.get(i));
            AlbumRealPath.add(realFilePathFromUri);
            String filePath = realFilePathFromUri;
            File file = new File(filePath);
            String fileName = file.getName();
            AlbumRealPathName.add(fileName);
        }
        String realFilePathFirstPhoto = getRealFilePathFromUri(this, uri);
        String firstPhotoFilePath = realFilePathFirstPhoto;
        File firstPhotoFile = new File(firstPhotoFilePath);
        String firstPhotoFileName = firstPhotoFile.getName();
        albumManager.saveAlbumAndPhoto2DB(AlbumRealPath,phoneNumber,albumName,albumDescription,AlbumRealPathName,firstPhotoFileName);
        //System.out.println("000000000000000"+"电话号："+phoneNumber+"选择的照片"+AlbumRealPath+"相册名称："+albumName+"相册描述："+albumDescription+"封面照片："+firstPhotoFileName);
        // 000000000000000电话号：15242089476选择的照片[Screenshot_20240308_175558_com.ss.android.ugc.aweme.jpg, IMG_20240308_210653.jpg]相册名称：哈哈哈相册描述：okok封面照片：IMG_20240308_210653.jpg
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void jumpToPictureSelector() {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.TYPE_IMAGE)
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
                        }
                       // pictureAdapter = new PictureAdapter(uriPathList);
                        pictureAdapter.notifyDataSetChanged(); // 通知适配器数据发生了变化
                    }
                    @Override
                    public void onCancel() {
                        finish();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uri_list, parent, false);
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





