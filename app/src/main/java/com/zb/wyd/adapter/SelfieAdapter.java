package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.SelfieInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.holder.IntegerAreaHolder;
import com.zb.wyd.holder.NewSelfieHolder;
import com.zb.wyd.holder.SelfieBaseHolder;
import com.zb.wyd.holder.SelfieHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class SelfieAdapter extends RecyclerView.Adapter<SelfieBaseHolder>
{

    private MyItemClickListener listener;
    private List<PhotoInfo> list;
    private Context mContext;

    public SelfieAdapter(List<PhotoInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public SelfieBaseHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        SelfieBaseHolder mHolder = null;
        switch (viewType)
        {
            case 1:
                mHolder = new SelfieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selfie, parent, false), parent.getContext
                        (), listener);
                break;

            case 0:
                mHolder = new NewSelfieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_selfie, parent, false), parent
                        .getContext(), listener);
                break;

        }
        return mHolder;
    }


    @Override
    public void onBindViewHolder(SelfieBaseHolder holder, int position)
    {

        PhotoInfo mPhotoInfo = list.get(position);
        holder.setPhotoInfo(mPhotoInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }


    @Override
    public int getItemViewType(int position)
    {
        if ("fav".equals(list.get(position).getSort()))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
