//package com.george.memoshareapp.adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseMultiItemAdapter;
//import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback;
//import com.george.memoshareapp.R;
//import com.george.memoshareapp.activities.ContactListActivity;
//import com.george.memoshareapp.beans.ContactInfo;
//import com.george.memoshareapp.interfaces.PhotoChangedListener;
//import com.george.memoshareapp.utils.GlideEngine;
//import com.luck.picture.lib.basic.PictureSelector;
//import com.luck.picture.lib.config.SelectMimeType;
//
//import java.util.List;
//public class ChatGroupMemberImageAdapter extends BaseMultiItemAdapter<Uri> implements DragAndSwipeDataCallback {
//    public static final int VIEW_TYPE_IMAGE = 1;
//    public static final int VIEW_TYPE_MOVE_BUTTON = 2;
//
//    public static final int REQUEST_CODE_CHOOSE = 3;
//
//    private Context context;
//    private final List<Uri> imageUriList;
//    private  String  memberName;
//
//    private int moveButtonPosition = -1;
//    private PhotoChangedListener photoChangedListener;
//    private ButtonViewHolder buttonHolder;
//    private ChatGroupMemberImageViewHolder imageHolder;
//
//    public ChatGroupMemberImageAdapter(Context context, List<ContactInfo> contactInfoList, String MemberName) {
//        for (ContactInfo c:contactInfoList) {
//            this.memberName=c.getName();
//            this.picture=c.getPicture();
//            this.imageUriList.add()
//        super(imageUriList);
//        this.context = context;
//        this.imageUriList = imageUriList;
//
//        addItemType(VIEW_TYPE_IMAGE, new OnMultiItemAdapterListener<Uri, RecyclerView.ViewHolder>() {
//            @NonNull
//            @Override
//            public ChatGroupMemberImageAdapter.ChatGroupMemberImageViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
//                View view = View.inflate(context, R.layout.release_photo_item, null);
//                TextView chat_group_member_name = (TextView) view.findViewById(R.id.chat_group_member_name);
//                chat_group_member_name.setText(memberName);
//                return new ChatGroupMemberImageViewHolder(view);
//            }
//
//            @Override
//            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable Uri uri) {
//                imageHolder = (ChatGroupMemberImageViewHolder) viewHolder;
//                setupViewImageButton(uri);
//
//            }
//        });
//        addItemType(VIEW_TYPE_MOVE_BUTTON, new OnMultiItemAdapterListener<Uri, RecyclerView.ViewHolder>() {
//
//            @NonNull
//            @Override
//            public ChatGroupMemberImageAdapter.ButtonViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
//                View view = View.inflate(context, R.layout.release_photo_button, null);
//                return new ButtonViewHolder(view);
//            }
//
//            @Override
//            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable Uri uri) {
//                buttonHolder = (ButtonViewHolder) viewHolder;
//                buttonHolder.chat_group_add_member.setOnClickListener(v -> startPictureSelector());
//            }
//        });
//        onItemViewType(new OnItemViewTypeListener<Uri>() {
//            @Override
//            public int onItemViewType(int i, @NonNull List<? extends Uri> list) {
//                return list.get(i) == null ? VIEW_TYPE_MOVE_BUTTON : VIEW_TYPE_IMAGE;
//            }
//        });
//        this.context = context;
//    }
//    private void setupViewImageButton(Uri imageUri) {
//        Glide.with(context)
//                .load(imageUri)
//                .centerCrop()
//                .into(imageHolder.imageView);
//
//        imageHolder.imageView.setOnClickListener(v -> viewImageDetail(imageUri));
//
//    }
//    private void viewImageDetail(Uri imageUri) {
//        Intent intent = new Intent(context, ContactListActivity.class);
//        intent.putExtra("imageUri", imageUri.toString());
//        context.startActivity(intent);
//    }
//    private void startPictureSelector(){
//        int selectImageSize = 0;
//        if (imageUriList.size() -1 > 0 && imageUriList.size() - 1 < 9) {
//            selectImageSize = 9 - imageUriList.size() + 1;
//        }else {
//            selectImageSize = 9;
//        }
//        int selectVideoSize = 0;
//        if (imageUriList.size() -1 > 0)
//            selectVideoSize = 0;
//        else
//            selectVideoSize = 1;
//
//        PictureSelector.create(context)
//                .openGallery(SelectMimeType.ofAll())
//                .isWithSelectVideoImage(false)
//                .setImageEngine(GlideEngine.createGlideEngine())
//                .setMaxSelectNum(selectImageSize)
//                .setMaxVideoSelectNum(selectVideoSize)
//                .isEmptyResultReturn(true)
//                .isMaxSelectEnabledMask(true)
//                .isPreviewVideo(true)
//                .forResult(REQUEST_CODE_CHOOSE);
//    }
//
//    public void updateImageListAndButtonPosition(int maxImages) {
//        if (imageUriList.size() <= maxImages) {
//            moveButtonPosition = imageUriList.size() - 1;
//        } else {
//            moveButtonPosition = -1;
//            if (imageUriList.get(imageUriList.size() - 1) == null) {
//                imageUriList.remove(imageUriList.size() - 1);
//            }
//        }
//        notifyDataSetChanged();
//
//    }
//    @Override
//    public void dataRemoveAt(int position) {
//        removeAt(position);
//    }
//
//    @Override
//    public void dataSwap(int fromPosition, int toPosition) {
//        swap(fromPosition, toPosition);
//    }
//
//    public class ChatGroupMemberImageViewHolder extends RecyclerView.ViewHolder{
//        private  ImageView chat_group_member_name;
//        private ImageView imageView;
//
//
//        public ChatGroupMemberImageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.image_view);
//            chat_group_member_name = itemView.findViewById(R.id.chat_group_member_name);
//
//        }
//
//    }
//
//    class ButtonViewHolder extends RecyclerView.ViewHolder{
//        private ImageView chat_group_add_member;
//
//        public ButtonViewHolder(@NonNull View itemView) {
//            super(itemView);
//            chat_group_add_member = itemView.findViewById(R.id.chat_group_add_member);
////
//        }
//    }
//
//}
