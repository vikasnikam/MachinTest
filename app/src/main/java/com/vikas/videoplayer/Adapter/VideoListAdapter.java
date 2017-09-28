package com.vikas.videoplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vikas.videoplayer.Model.VideoData;
import com.vikas.videoplayer.R;
import com.vikas.videoplayer.VideoActivity;

import java.util.List;

/**
 * Created by Vikas on 9/28/2017.
 */


public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    Context context;
    private List<VideoData> videolist;
    RecyclerView recyclerView;
    //final VideoData videoData;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    public class MyViewHolder extends RecyclerView.ViewHolder {
       public TextView title;
        public ImageView img_icon,fav_icon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_name);
            img_icon = (ImageView) view.findViewById(R.id.img_icon);
            fav_icon = (ImageView) view.findViewById(R.id.myfav);
        }
    }

    public VideoListAdapter(Context context, List<VideoData> videolist, RecyclerView recyclerView) {
        this.context = context;
        this.videolist = videolist;
        this.recyclerView = recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_row, parent, false);
        itemView.setOnClickListener(mOnClickListener);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final VideoData videoData = videolist.get(position);
        holder.title.setText(videoData.getName());
        String URL="http://genorainnovations.com/Audio/media/icon/"+videoData.getIcon_name();
        Picasso.with(context)
                .load(URL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.img_icon);
        if((String.valueOf(videoData.getFavourite()).equalsIgnoreCase("1"))){
            holder.fav_icon.setImageResource(R.drawable.ic_favorite);
        }else {
            holder.fav_icon.setImageResource(R.drawable.ic_favorite_white);
        }
       /* holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VideoActivity.class);
                intent.putExtra("VIDEO_FILE",videoData.getFile_name());
                System.out.println("Data"+videoData.getFile_name());
                context.startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return videolist.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildLayoutPosition(v);
            Intent intent=new Intent(context, VideoActivity.class);
            intent.putExtra("VIDEO_FILE",videolist.get(itemPosition).getFile_name());
            System.out.println("Data"+videolist.get(itemPosition).getFile_name());
            context.startActivity(intent);
        }
    }
}
