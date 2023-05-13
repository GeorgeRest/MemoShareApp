package com.george.memoshareapp.runnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SavePhotoRunnable implements Runnable{
    private List photoPath;
    public SavePhotoRunnable(List photoPath) {
        for (int i = 0; i < photoPath.size(); i++) {
            this.photoPath = photoPath;
        }

    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < photoPath.size(); i++) {
                String photoPath1 = photoPath.get(i).toString();
                // 创建一个FileWriter对象来写入照片路径
                FileWriter writer = new FileWriter("photos.txt", true);

                // 将照片路径写入文件
                writer.write(photoPath1 + "\n");

                // 关闭FileWriter对象
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
