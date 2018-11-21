package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.holder.PhotoHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>
{

    private MyItemClickListener listener;
    private List<String>        list;
    private Context             mContext;

    public PhotoAdapter(List<String> list,MyItemClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        PhotoHolder mHolder = new PhotoHolder(itemView,parent.getContext());
        return mHolder;
    }


    @Override
    public void onBindViewHolder(PhotoHolder holder, int position)
    {
        holder.setPhoto(list.get(position),position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
