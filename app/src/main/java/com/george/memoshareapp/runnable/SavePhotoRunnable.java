package com.george.memoshareapp.runnable;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SavePhotoRunnable implements Runnable{
    private List<Uri> photoPath;
    private Context context;
    private String photoAbsolutePath;
    private List<String> photoPathList;

    public SavePhotoRunnable(List<Uri> photoPath, Context context) {
        this.context= context;

            this.photoPath = photoPath;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < photoPath.size(); i++) {
                Uri uri = photoPath.get(i);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                File file = new File(context.getFilesDir(), "photo" + i + ".jpg");
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                // 将照片路径写入文件
                FileWriter writer = new FileWriter("photos.txt", true);
                writer.write(file.getAbsolutePath() + "\n");
                writer.close();
                photoAbsolutePath = file.getAbsolutePath();
                photoPathList = new ArrayList<>();

                photoPathList.add(photoAbsolutePath);

            }
            getPhotoPathList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPhotoPathList() {
        return this.photoPathList;
    }


}
