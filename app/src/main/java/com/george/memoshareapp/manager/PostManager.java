package com.george.memoshareapp.manager;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.utils.SaveContent2DB;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class PostManager {
    private static final String TAG = "PostManager";
    private Context context;
    private List<Uri> imageUriList;
    private List<String> photoPathList = new ArrayList<>();
    private List<ImageParameters> imageParametersList = new ArrayList<>();


    public PostManager(Context context) {
        this.context = context;
    }
    public Post findUser(String id) {
        Post post = LitePal.where("id = ?", id).findFirst(Post.class);
        if (post != null) {
            return post;
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getDBParameter(List<Uri> imageUriList, String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime) {
        this.imageUriList = imageUriList;
        SaveContent2DB.saveContent2DB(imageParametersList,photoPathList,imageUriList,context,phoneNumber, editTextContent, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void saveContent2DB(String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime) {
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        File photoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photoCache");
//        if (!photoFolder.exists()) {// 文件夹不存在，执行创建文件夹的操作
//            photoFolder.mkdirs(); // 创建文件夹
//        }
//        for (int i = 0; i < imageUriList.size(); i++) {
//            uri = imageUriList.get(i);
//            uriStringPath = Base64.getUrlEncoder().encodeToString(uri.toString().getBytes());
//            file = new File(photoFolder, uriStringPath);
//            if (file.exists()) {
//                photoAbsolutePath = file.getAbsolutePath();
//                photoPathList.add(photoAbsolutePath);
//            } else {
//                // 将照片路径写入文件
//                FileWriter writer = null;
//                try {
//                    writer = new FileWriter(uriStringPath, true);
//                    writer.write(file.getAbsolutePath() + "\n");
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                photoAbsolutePath = file.getAbsolutePath();
//                photoPathList.add(photoAbsolutePath);
//            }
//            BitmapFactory.Options options = ImageUtil.getBitmapOptionsFromImageUri(context, uri);
//            if (options != null) {
//                int imageWidth = options.outWidth;
//                int imageHeight = options.outHeight;
//                imageParameters = new ImageParameters(photoAbsolutePath, imageWidth, imageHeight);
//                imageParametersList.add(imageParameters);
//            }
//
//            savePhotoRunnable = new SavePhotoRunnable(uri, file, photoFolder, imageUriList, context);
//            executor.execute(savePhotoRunnable);
//        }
//        Log.d(TAG, "saveContent2DB: " + photoPathList);
//
//        Post post = new Post(phoneNumber, editTextContent, photoPathList, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
//        post.setImageParameters(imageParametersList);
//        for (ImageParameters imageParameters : imageParametersList) {
//            imageParameters.setPost(post);
//            imageParameters.save();
//        }
//    if(record.size()!=0 ){
//            for (Recordings recordings : record) {
//                recordings.setPost(post);
//                recordings.save();
//            }
//        }
//        post.save();
//        post.setLike(0);
//        post.update(post.getId());
//        if (post.getIsPublic() != 0) {
//            HomeWholeRecyclerViewAdapter adapter = HomeWholeRecyclerViewAdapter.getInstance();
//            adapter.addData(post, imageUriList);
//            EventBus.getDefault().post(new ScrollToTopEvent());
//        }
//        executor.shutdown();
//    }
}