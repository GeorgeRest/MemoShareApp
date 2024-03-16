package com.george.memoshareapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class ImageDownLoadTask extends AsyncTask<String, Void, Bitmap> {

    private Context context;

    public ImageDownLoadTask(Context context){
        this.context = context;
    }

    protected Bitmap doInBackground(String... urls) {
        String imageUrl = urls[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            // 将下载的图片保存到相册
            saveImageToGallery(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            Toasty.info(context, "图片下载成功").show();
        } else {
            // 下载失败时显示Toast消息
            Toasty.info(context, "图片下载失败").show();
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        // 检查外部存储是否可用
        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            // 创建一个文件对象来保存图片
            File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "memo_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg");

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();

                // 将图片添加到相册中
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        pictureFile.getAbsolutePath(), pictureFile.getName(), null);
            } catch (Exception e) {
                Log.e("zxtesterror", "saveImageToGallery: ", e);
                e.printStackTrace();
            }
        }
    }
}
