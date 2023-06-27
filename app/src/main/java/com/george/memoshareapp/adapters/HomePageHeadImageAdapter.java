package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ContactInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageHeadImageAdapter extends RecyclerView.Adapter<HomePageHeadImageAdapter.ViewHolder> {

    private Context context;
    private List<ContactInfo> contactPicture;
    private List<String> contactName;
    private Map<String, Integer> nameToPictureMap;


    public HomePageHeadImageAdapter(Context context, List<ContactInfo> contactPicture, List<String> contactName) {
        this.context = context;
        this.contactPicture = contactPicture;
//        this.contactName = contactName;
        this.contactName = contactName != null ? contactName : new ArrayList<>();

        nameToPictureMap = new HashMap<>();
        for (ContactInfo info : contactPicture) {
            nameToPictureMap.put(info.getName(), info.getPicture());
        }
    }

    @NonNull
    @Override
    public HomePageHeadImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homepage_head_image_item, parent, false);
        return new HomePageHeadImageAdapter.ViewHolder(view);
    }

//@Override
//public void onBindViewHolder(@NonNull HomePageHeadImageAdapter.ViewHolder holder, int position) {
//    // 首先，将所有ImageView和TextView重置为默认状态
//    holder.ivHeadImage1.setVisibility(View.GONE);
//    holder.ivHeadImage2.setVisibility(View.GONE);
//    holder.ivHeadImage3.setVisibility(View.GONE);
//    holder.tvMoreCount.setVisibility(View.GONE);
//    holder.tvMoreCount.setText("");
//
//    // 计算这一项中应该显示的第一个头像的索引
//    int start = position * 3;
//
//    // 遍历这一项中应该显示的所有头像
//    for (int i = 0; i < 3; i++) {
//        // 计算当前头像的索引
//        int index = start + i;
//
//        // 如果当前头像的索引小于总的头像数量，那么我们就可以显示这个头像
//        if (index < contactName.size()) {
//            String name = contactName.get(index);
//            Integer picture = nameToPictureMap.get(name);
//            if (picture != null) {
//                switch (i) {
//                    case 0:
//                        holder.ivHeadImage1.setVisibility(View.VISIBLE);
//                        holder.ivHeadImage1.setImageResource(picture);
//                        break;
//                    case 1:
//                        holder.ivHeadImage2.setVisibility(View.VISIBLE);
//                        holder.ivHeadImage2.setImageResource(picture);
//                        break;
//                    case 2:
//                        // 在最后一个位置上，我们要特别处理超过三个头像的情况
//                        if (index < contactName.size() - 1) {
//                            holder.ivHeadImage3.setVisibility(View.VISIBLE);
//                            holder.tvMoreCount.setVisibility(View.VISIBLE);
//                            holder.tvMoreCount.setText("+" + (contactName.size() - index - 1));
//                        } else {
//                            holder.ivHeadImage3.setVisibility(View.VISIBLE);
//                            holder.ivHeadImage3.setImageResource(picture);
//                        }
//                        break;
//                }
//            }
//        }
//    }
//}
@Override
public void onBindViewHolder(@NonNull HomePageHeadImageAdapter.ViewHolder holder, int position) {
    holder.ivHeadImage1.setVisibility(View.GONE);
    holder.ivHeadImage2.setVisibility(View.GONE);
    holder.flHeadImage3.setVisibility(View.GONE);
    holder.ivHeadImage3.setVisibility(View.GONE);
    holder.tvMoreCount.setVisibility(View.GONE);
    holder.tvMoreCount.setText("");

    if (contactName != null && position < contactName.size()) {
        for (int i = 0; i < 3; i++) {
            int index = position * 3 + i;
            if (index < contactName.size()) {
                String name = contactName.get(index);
                Integer picture = nameToPictureMap.get(name);
                if (picture != null) {
                    switch (i) {
                        case 0:
                            holder.ivHeadImage1.setVisibility(View.VISIBLE);
                            holder.ivHeadImage1.setImageResource(picture);
                            break;
                        case 1:
                            holder.ivHeadImage2.setVisibility(View.VISIBLE);
                            holder.ivHeadImage2.setImageResource(picture);
                            break;
                        case 2:
                            holder.flHeadImage3.setVisibility(View.VISIBLE);
                            holder.ivHeadImage3.setVisibility(View.VISIBLE);
                            holder.ivHeadImage3.setImageResource(picture);
                            if (contactName.size() > position * 3 + 3) {
                                holder.tvMoreCount.setVisibility(View.VISIBLE);
                                holder.tvMoreCount.setText("+" + (contactName.size() - position * 3 - 3));
                                return;
                            }
                            break;
                    }
                }
            }
        }
    }
}



    @Override
    public int getItemCount() {
        return contactName.size();
    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//
////        public ImageView imageView;
////        public LinearLayout linearLayout;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
////            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_head_images);
////            imageView = (ImageView) itemView.findViewById(R.id.iv_head_image);
//
//        }
//    }
public class ViewHolder extends RecyclerView.ViewHolder {

    private FrameLayout flHeadImage3;
    public ImageView ivHeadImage1;
    public ImageView ivHeadImage2;
    public ImageView ivHeadImage3;
    public TextView tvMoreCount;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        ivHeadImage1 = (ImageView) itemView.findViewById(R.id.homewhole_iv_head_image_1);
        ivHeadImage2 = (ImageView) itemView.findViewById(R.id.iv_head_image_2);
        ivHeadImage3 = (ImageView) itemView.findViewById(R.id.iv_head_image_3);
        tvMoreCount = (TextView) itemView.findViewById(R.id.tv_more_count);
        flHeadImage3 = (FrameLayout) itemView.findViewById(R.id.fl_head_image_3);
    }
}

}
