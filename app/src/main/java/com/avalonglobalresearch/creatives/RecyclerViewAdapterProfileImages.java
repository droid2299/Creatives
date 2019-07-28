package com.avalonglobalresearch.creatives;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterProfileImages extends RecyclerView.Adapter<RecyclerViewAdapterProfileImages.ViewHolder> {

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;
    String uidOfUploader;

    public RecyclerViewAdapterProfileImages(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }


    @Override
    public RecyclerViewAdapterProfileImages.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterProfileImages.ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());
        holder.username.setText(UploadInfo.getUsername());
        GlideApp.with(context).load(UploadInfo.getProfile()).into(holder.profilepic);
        uidOfUploader = UploadInfo.getUid();

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageResource(R.drawable.likesel);
            }
        });
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView , like;
        public TextView imageNameTextView , username;
        public CircleImageView profilepic;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            like = (ImageView) itemView.findViewById(R.id.like);
            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            username = (TextView) itemView.findViewById(R.id.username);
            profilepic  = (CircleImageView) itemView.findViewById(R.id.profilepic);
        }
    }
}

