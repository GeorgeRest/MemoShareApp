package com.george.memoshareapp.runnable;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.george.memoshareapp.activities.ReleaseActivity;
import com.george.memoshareapp.dialog.LoadingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SavePhotoRunnable implements Runnable {

    private final Handler handler;
    private final File photoFolder;
    private final File file;
    private final Uri uri;
    private List<Uri> photoPath;
    private Context context;
    private String photoAbsolutePath;
    private List<String> photoPathList = new ArrayList<>();
    private String uriStringPath;
    private LoadingDialog loadingDialog;

    public SavePhotoRunnable(Uri uri, File file, File photoFolder, List<Uri> photoPath, Context context) {
        handler = new Handler();
        this.context = context;
        this.photoPath = photoPath;
        this.photoFolder = photoFolder;
        this.file = file;
        this.uri = uri;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showLoading();
                    loadingDialog.startAnim();
                }
            });
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.endAnim();
                    clearLoading();
                }
            });
        }
    }


    private void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.show();
    }

    private void clearLoading() {
        if (!((ReleaseActivity) context).isFinishing() && !((ReleaseActivity) context).isDestroyed()) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        }
    }
}


