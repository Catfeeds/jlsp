package com.zb.wyd.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.activity.VideoPlayActivity;
import com.zb.wyd.activity.VidoeListActivity;
import com.zb.wyd.adapter.IntegerAreaAdapter;
import com.zb.wyd.adapter.PhotoDescAdapter;
import com.zb.wyd.entity.AuthorPhotoInfo;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.StyleInfo;
import com.zb.wyd.entity.TaskInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class AuthorPhotoHolder extends RecyclerView.ViewHolder
{
    private TextView mUserNameTv;
    private TextView mDescTv;
    private TextView mUpdateTimeTv;
    private ImageView mSexIv;
    private CircleImageView mUserPicIv;
    private MaxRecyclerView mRecyclerView;

    private MyItemClickListener listener;
    private Context context;
    private List<PhotoInfo> list = new ArrayList<>();

    public AuthorPhotoHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mDescTv = (TextView) rootView.findViewById(R.id.tv_desc);
        mUpdateTimeTv = (TextView) rootView.findViewById(R.id.tv_update_time);
        mUserPicIv = (CircleImageView) rootView.findViewById(R.id.iv_user_pic);
        mSexIv = (ImageView) rootView.findViewById(R.id.iv_sex);
        mRecyclerView = (MaxRecyclerView) rootView.findViewById(R.id.rv_photo);
    }


    public void setAuthorPhotoInfo(AuthorPhotoInfo mAuthorPhotoInfo, final int p)
    {


        mRecyclerView.setLayoutManager(new FullyGridLayoutManager(context, 3));

        for (int i = 0; i < 6; i++)
        {
            PhotoInfo mPhotoInfo = new PhotoInfo();
            mPhotoInfo.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543244505361&di=187c6deab8b0b3aa8da96109c19fe3e5&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2Fc161025%2F14M32W35M640-31Q8.jpg");
            list.add(mPhotoInfo);
        }

        mRecyclerView.setAdapter(new PhotoDescAdapter(list, listener));


    }


}
