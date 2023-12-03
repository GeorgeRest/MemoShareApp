//package com.george.memoshareapp.utils;
//
//import android.app.Activity;
//
//import com.luck.picture.lib.basic.PictureSelector;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.luck.picture.lib.interfaces.OnResultCallbackListener;
//
//
//
//
//
//import java.util.List;
//
//public class PhotoSelector {
//
//    public static void selectPhotos(Activity activity, int maxSelectNum, OnPhotosSelectedListener listener) {
//        PictureSelector.create(activity)
//                .openGallery(PictureMimeType.ofImage())
//                .maxSelectNum(maxSelectNum)
//                .imageEngine(GlideEngine.createGlideEngine())
//                .forResult(new OnResultCallbackListener<List<LocalMedia>>() {
//                    @Override
//                    public void onResult(List<LocalMedia> result) {
//                        if (listener != null) {
//                            listener.onPhotosSelected(result);
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // 处理用户取消选择的情况
//                        if (listener != null) {
//                            listener.onPhotosCanceled();
//                        }
//                    }
//                });
//    }
//
//    public interface OnPhotosSelectedListener {
//        void onPhotosSelected(List<LocalMedia> selectedPhotos);
//
//        void onPhotosCanceled();
//    }
//}