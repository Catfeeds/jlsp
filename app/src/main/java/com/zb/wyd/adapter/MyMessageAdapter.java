package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.MessageInfo;
import com.zb.wyd.holder.MessageHolder;
import com.zb.wyd.holder.MyMessageHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageHolder>
{

    private List<MessageInfo> list;
    private Context           mContext;
    private MyItemClickListener listener;

    public MyMessageAdapter(List<MessageInfo> list,MyItemClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public MyMessageHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false);
        MyMessageHolder mHolder = new MyMessageHolder(itemView,listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(MyMessageHolder holder, int position)
    {
        holder.setMessageInfo(list.get(position),position);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


}
