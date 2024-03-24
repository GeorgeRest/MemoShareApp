package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.ReleasePhotoImageDetailActivity;
import com.george.memoshareapp.interfaces.PhotoChangedListener;
import com.george.memoshareapp.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;

import java.util.List;
public class ImageAdapter extends BaseMultiItemAdapter<Uri> implements DragAndSwipeDataCallback {
    public static final int VIEW_TYPE_IMAGE = 1;
    public static final int VIEW_TYPE_MOVE_BUTTON = 2;

    public static final int REQUEST_CODE_CHOOSE = 3;

    private Context context;
    private final List<Uri> imageUriList;
    private int moveButtonPosition = -1;
    private PhotoChangedListener photoChangedListener;
    private ButtonViewHolder buttonHolder;
    private ImageViewHolder imageHolder;

    public ImageAdapter(Context context, List<Uri> imageUriList) {
        super(imageUriList);
        this.context = context;
        this.imageUriList = imageUriList;
        addItemType(VIEW_TYPE_IMAGE, new OnMultiItemAdapterListener<Uri, RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public ImageAdapter.ImageViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                View view = View.inflate(context, R.layout.release_photo_item, null);
                return new ImageViewHolder(view);
            }

            @Override
            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable Uri uri) {
                imageHolder = (ImageViewHolder) viewHolder;
                setupViewImageButton(uri);
            }
        });
        addItemType(VIEW_TYPE_MOVE_BUTTON, new OnMultiItemAdapterListener<Uri, RecyclerView.ViewHolder>() {

            @NonNull
            @Override
            public ImageAdapter.ButtonViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                View view = View.inflate(context, R.layout.release_photo_button, null);
                return new ButtonViewHolder(view);
            }

            @Override
            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable Uri uri) {
                buttonHolder = (ButtonViewHolder) viewHolder;
                buttonHolder.moveButton.setOnClickListener(v -> startPictureSelector());
            }
        });
        onItemViewType(new OnItemViewTypeListener<Uri>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends Uri> list) {
                return list.get(i) == null ? VIEW_TYPE_MOVE_BUTTON : VIEW_TYPE_IMAGE;
            }
        });
        this.context = context;
    }
    private void setupViewImageButton(Uri imageUri) {
        Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .into(imageHolder.imageView);

        imageHolder.imageView.setOnClickListener(v -> viewImageDetail(imageUri));
    }
    private void viewImageDetail(Uri imageUri) {
        Intent intent = new Intent(context, ReleasePhotoImageDetailActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        context.startActivity(intent);
    }
    private void startPictureSelector(){
        int selectImageSize = 0;
        if (imageUriList.size() -1 > 0 && imageUriList.size() - 1 < 9) {
            selectImageSize = 9 - imageUriList.size() + 1;
        }else {
            selectImageSize = 9;
        }
        int selectVideoSize = 0;
        if (imageUriList.size() -1 > 0)
            selectVideoSize = 0;
        else
            selectVideoSize = 1;

        PictureSelector.create(context)
                .openGallery(SelectMimeType.TYPE_IMAGE)
                .isWithSelectVideoImage(false)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(selectImageSize)
                .setMaxVideoSelectNum(selectVideoSize)
                .isEmptyResultReturn(true)
                .isMaxSelectEnabledMask(true)
                .isPreviewVideo(true)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public void updateImageListAndButtonPosition(int maxImages) {
        if (imageUriList.size() <= maxImages) {
            moveButtonPosition = imageUriList.size() - 1;
        } else {
            moveButtonPosition = -1;
            if (imageUriList.get(imageUriList.size() - 1) == null) {
                imageUriList.remove(imageUriList.size() - 1);
            }
        }
        notifyDataSetChanged();

    }
    @Override
    public void dataRemoveAt(int position) {
        removeAt(position);
    }

    @Override
    public void dataSwap(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageView deleteButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chat_more_member_image_view);
//            deleteButton = itemView.findViewById(R.id.delete_button);
//            deleteButton.setOnClickListener(this);
        }

    }

    class ButtonViewHolder extends RecyclerView.ViewHolder{
        private ImageView moveButton;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            moveButton = itemView.findViewById(R.id.chat_group_add_member);
//            moveButton.setOnClickListener(this);
        }
    }

}
