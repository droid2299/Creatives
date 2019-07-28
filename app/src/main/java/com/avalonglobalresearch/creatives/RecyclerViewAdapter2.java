package com.avalonglobalresearch.creatives;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter2  extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {

    Context context2;
    List<ImageUploadInfo> MainImageUploadInfoList;
    String uidOfUploader;


    public RecyclerViewAdapter2(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context2 = context;
    }

    @Override
    public RecyclerViewAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items2, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter2.ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context2 , ProfileActivity.class);
                intent.putExtra("UID" , uidOfUploader);
                context2.startActivity(intent);
            }
        });


        Uri uri = Uri.parse(UploadInfo.getImageURL());
        Log.d("TAGGG" , ""+uri);
        holder.videoView.setVideoURI(uri);
        holder.username.setText(UploadInfo.getUsername());
        GlideApp.with(context2).load(UploadInfo.getProfile()).into(holder.profilepic);
        holder.videoView.canSeekBackward();
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                holder.videoView.start();}});
        holder.videoView.start();
        uidOfUploader = UploadInfo.getUid();
        Log.d("HOPE IT WORKS" , "UID of Uploader = "+uidOfUploader);


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageResource(R.drawable.likesel);
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        public TextView imageNameTextView , username;
        public ImageView like;
        public CircleImageView profilepic;



        public ViewHolder(View itemView) {
            super(itemView);

            videoView = (VideoView) itemView.findViewById(R.id.videoView2);
            like = (ImageView) itemView.findViewById(R.id.like);
            imageNameTextView = (TextView) itemView.findViewById(R.id.text12);
            username = (TextView) itemView.findViewById(R.id.username);
            profilepic  = (CircleImageView) itemView.findViewById(R.id.profilepic);
        }
    }
}
