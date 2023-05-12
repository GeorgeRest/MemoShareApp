package com.george.memoshareapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.ReleasePhotoImageDetailActivity;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_MOVE_BUTTON = 2;

    public static final int REQUEST_CODE_CHOOSE = 2;

    private final Context context;
    private final List<Uri> imageUriList;
    private int moveButtonPosition = -1;

    public ImageAdapter(Context context, List<Uri> imageUriList) {
        this.context = context;
        this.imageUriList = imageUriList;
        updateImageListAndButtonPosition(9);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        if (viewType == VIEW_TYPE_IMAGE) {
            itemView = inflater.inflate(R.layout.release_photo_item, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.release_photo_button, parent, false);
        }

        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return imageUriList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == moveButtonPosition ? VIEW_TYPE_MOVE_BUTTON : VIEW_TYPE_IMAGE;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private ImageView deleteButton;
        private ImageView moveButton;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_IMAGE) {
                imageView = itemView.findViewById(R.id.image_view);
                deleteButton = itemView.findViewById(R.id.delete_button);
                deleteButton.setOnClickListener(this);
            } else {
                moveButton = itemView.findViewById(R.id.button_move);
                moveButton.setOnClickListener(this);
            }
        }

        public void bind(int position) {
            if (getItemViewType() == VIEW_TYPE_IMAGE) {
                Uri imageUri = imageUriList.get(position);
                    setupViewImageButton(imageUri);
                    setupDeleteButton(position);
//                if (imageUri == null) {
////                    setupAddImageButton();
//                    setupMoveButton(position);
//                    deleteButton.setVisibility(View.GONE);
//                } else {
//                    setupViewImageButton(imageUri);
//                    setupDeleteButton(position);
//                }
            } else {
                    setupMoveButton(position);
//                if(imageUriList == null){
//                    setupAddImageButton();
//                }

            }
        }

        private void setupAddImageButton() {
            imageView.setImageResource(R.mipmap.picture);
            imageView.setOnClickListener(v -> startImageSelectionActivity());
        }

        private void setupViewImageButton(Uri imageUri) {
            Glide.with(context)
                    .load(imageUri)
                    .centerCrop()
                    .into(imageView);

            imageView.setOnClickListener(v -> viewImageDetail(imageUri));
        }

        private void setupDeleteButton(int position) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> deleteImage(position));
        }

        private void setupMoveButton(int position) {
            if (position == moveButtonPosition) {
                moveButton.setVisibility(View.VISIBLE);
                moveButton.setOnClickListener(v -> startImageSelectionActivity());
            } else {
                moveButton.setVisibility(View.GONE);
            }
        }

        private void startImageSelectionActivity() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_CHOOSE);
            updateImageListAndButtonPosition(9);
        }

        private void viewImageDetail(Uri imageUri) {
            Intent intent = new Intent(context, ReleasePhotoImageDetailActivity.class);
            intent.putExtra("imageUri", imageUri.toString());
            context.startActivity(intent);
        }

        private void deleteImage(int position) {
            imageUriList.remove(position);
            moveButtonPosition = imageUriList.size() - 1;
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, imageUriList.size() - position);
            if (imageUriList.isEmpty() || imageUriList.get(imageUriList.size() - 1) != null) {
                imageUriList.add(null);
            }
            updateImageListAndButtonPosition(9);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (getItemViewType() == VIEW_TYPE_IMAGE) {
                if (v.getId() == R.id.delete_button) {
                    deleteImage(position);
                }
            } else if (getItemViewType() == VIEW_TYPE_MOVE_BUTTON && v.getId() == R.id.button_move) {
                startImageSelectionActivity();
            }
        }
    }
}

