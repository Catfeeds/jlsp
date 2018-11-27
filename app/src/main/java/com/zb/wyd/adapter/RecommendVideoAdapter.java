package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.holder.RecommendVideoHolder;
import com.zb.wyd.holder.VideoHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class RecommendVideoAdapter extends RecyclerView.Adapter<RecommendVideoHolder>
{

    private MyItemClickListener listener;
    private List<VideoInfo>     list;
    private Context             mContext;
    private boolean  isNew;

    public void setNew(boolean aNew)
    {
        isNew = aNew;
    }

    public RecommendVideoAdapter(List<VideoInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public RecommendVideoHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_video, parent, false);
        RecommendVideoHolder mHolder = new RecommendVideoHolder(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(RecommendVideoHolder holder, int position)
    {
        VideoInfo mVideoInfo = list.get(position);
        mVideoInfo.setNew(isNew);
        holder.setVideoInfo(mVideoInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
