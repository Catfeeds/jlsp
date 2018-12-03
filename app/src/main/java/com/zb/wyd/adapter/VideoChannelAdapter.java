package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.HostInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.holder.RecommendVideoHolder;
import com.zb.wyd.holder.VideoChannelHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class VideoChannelAdapter extends RecyclerView.Adapter<VideoChannelHolder>
{

    private MyItemClickListener listener;
    private List<HostInfo>     list;
    private Context             mContext;


    public VideoChannelAdapter(List<HostInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public VideoChannelHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_channel, parent, false);
        VideoChannelHolder mHolder = new VideoChannelHolder(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(VideoChannelHolder holder, int position)
    {
        HostInfo mHostInfo = list.get(position);
        holder.setHostInfo(mHostInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
