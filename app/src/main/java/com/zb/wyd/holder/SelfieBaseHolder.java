package com.zb.wyd.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.entity.PhotoInfo;


public abstract class SelfieBaseHolder extends RecyclerView.ViewHolder
{
    public SelfieBaseHolder(View itemView)
    {
        super(itemView);
    }

    public abstract void setPhotoInfo(PhotoInfo mPhotoInfo, int p);
}
