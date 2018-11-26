package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.holder.PhotoDescHolder;
import com.zb.wyd.holder.PhotoHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class PhotoDescAdapter extends RecyclerView.Adapter<PhotoDescHolder>
{

    private MyItemClickListener listener;
    private List<PhotoInfo> list;
    private Context mContext;

    public PhotoDescAdapter(List<PhotoInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public PhotoDescHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anchor_photo_desc, parent, false);
        PhotoDescHolder mHolder = new PhotoDescHolder(itemView, parent.getContext());
        return mHolder;
    }


    @Override
    public void onBindViewHolder(PhotoDescHolder holder, int position)
    {
        holder.setPhotoInfo(list.get(position), position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
