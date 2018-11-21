package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.holder.LabelHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class LabelAdapter extends RecyclerView.Adapter<LabelHolder>
{

    private MyItemClickListener listener;
    private List<CategoryInfo>      list;
    private Context             mContext;

    public LabelAdapter(List<CategoryInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public LabelHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label, parent, false);
        LabelHolder mHolder = new LabelHolder(itemView,listener);
        return mHolder;
}


    @Override
    public void onBindViewHolder(LabelHolder holder, int position)
    {
        CategoryInfo mCataInfo = list.get(position);
        holder.setCataInfo(mCataInfo,position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
