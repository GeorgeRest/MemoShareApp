package com.george.memoshareapp.runnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavePhotoRunnable implements Runnable{
    private String photoPath;
    public SavePhotoRunnable(String photoPath) {
        this.photoPath = photoPath;
    }
    @Override
    public void run() {
        try {
            // 创建一个FileWriter对象来写入照片路径
            FileWriter writer = new FileWriter("photos.txt", true);

            // 将照片路径写入文件
            writer.write(photoPath + "\n");

            // 关闭FileWriter对象
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
