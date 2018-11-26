package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.AuthorPhotoInfo;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.holder.AnchorHolder;
import com.zb.wyd.holder.AuthorPhotoHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class AnchorPhotoAdapter extends RecyclerView.Adapter<AuthorPhotoHolder>
{

    private MyItemClickListener listener;
    private List<AuthorPhotoInfo> list;
    private Context mContext;

    public AnchorPhotoAdapter(List<AuthorPhotoInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public AuthorPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anchor_photo, parent, false);
        AuthorPhotoHolder mHolder = new AuthorPhotoHolder(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(AuthorPhotoHolder holder, int position)
    {
        AuthorPhotoInfo mAuthorPhotoInfo = list.get(position);
        holder.setAuthorPhotoInfo(mAuthorPhotoInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
