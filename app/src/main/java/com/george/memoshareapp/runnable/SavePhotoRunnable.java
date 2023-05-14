package com.george.memoshareapp.runnable;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.george.memoshareapp.dialog.LoadingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SavePhotoRunnable implements Runnable {

    private final Handler handler;
    private List<Uri> photoPath;
    private Context context;
    private String photoAbsolutePath;
    private List<String> photoPathList=new ArrayList<>();
    private String uriStringPath;
    private LoadingDialog loadingDialog;

    public SavePhotoRunnable(List<Uri> photoPath, Context context) {
        handler = new Handler();
        this.context = context;
        this.photoPath = photoPath;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        File photoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photoCache");
        if (!photoFolder.exists()) {// 文件夹不存在，执行创建文件夹的操作
            photoFolder.mkdirs(); // 创建文件夹
        }
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showLoading();
                        loadingDialog.startAnim();
                    }
                });

                for (int i = 0; i < photoPath.size(); i++) {
                    Uri uri = photoPath.get(i);
                    uriStringPath = Base64.getUrlEncoder().encodeToString(uri.toString().getBytes());
                    File file = new File(photoFolder, uriStringPath);
                    if (file.exists()) {
                        photoAbsolutePath = file.getAbsolutePath();
                        photoPathList.add(photoAbsolutePath);
                        return;
                    }
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    // 将照片路径写入文件
                    FileWriter writer = new FileWriter(uriStringPath, true);
                    writer.write(file.getAbsolutePath() + "\n");
                    writer.close();
                    photoAbsolutePath = file.getAbsolutePath();
                    photoPathList.add(photoAbsolutePath);
                    //储存
                }

                getPhotoPathList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.endAnim();
                        clearLoading();
                    }
                });
            }
        }



    public List<String> getPhotoPathList() {
        return this.photoPathList;
    }

    private void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.show();
    }

    private void clearLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }


    }
}



