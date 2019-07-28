package com.avalonglobalresearch.creatives;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterProfileFragmentTexts extends RecyclerView.Adapter<RecyclerViewAdapterProfileFragmentTexts.ViewHolder> {

    Context context3;
    List<ImageUploadInfo> MainImageUploadInfoList;
    String uidOfUploader;


    public RecyclerViewAdapterProfileFragmentTexts(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context3 = context;
    }

    public RecyclerViewAdapterProfileFragmentTexts.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items3, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterProfileFragmentTexts.ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        Uri uri = Uri.parse(UploadInfo.getImageURL());
        Log.d("TAGGG" , ""+uri);

        holder.userText.setText(UploadInfo.getImageURL());
        holder.imageNameTextView.setText(UploadInfo.getImageName());
        holder.username.setText(UploadInfo.getUsername());
        GlideApp.with(context3).load(UploadInfo.getProfile()).into(holder.profilepic);
        uidOfUploader = UploadInfo.getUid();
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageResource(R.drawable.likesel);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView imageNameTextView , username ;
        public TextView userText;
        public ImageView like;
        public CircleImageView profilepic;


        public ViewHolder(View itemView) {
            super(itemView);

            userText = (TextView) itemView.findViewById(R.id.textView13);
            like = (ImageView) itemView.findViewById(R.id.like);
            imageNameTextView = (TextView) itemView.findViewById(R.id.caption);
            username = (TextView) itemView.findViewById(R.id.username);
            profilepic  = (CircleImageView) itemView.findViewById(R.id.profilepic);
        }
    }
}



