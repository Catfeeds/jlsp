package com.zb.wyd.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.adapter.PhotoDescAdapter;
import com.zb.wyd.entity.AuthorPhotoInfo;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class NewSelfieHolder extends SelfieBaseHolder
{
    private TextView mUserNameTv;
    private TextView mDescTv;
    private TextView mUpdateTimeTv;
    private ImageView mSexIv;
    private CircleImageView mUserPicIv;
    private MaxRecyclerView mRecyclerView;
    private LinearLayout itemLayout;
    private MyItemClickListener listener;
    private Context context;

    public NewSelfieHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mDescTv = (TextView) rootView.findViewById(R.id.tv_desc);
        mUpdateTimeTv = (TextView) rootView.findViewById(R.id.tv_update_time);
        mUserPicIv = (CircleImageView) rootView.findViewById(R.id.iv_user_pic);
        mSexIv = (ImageView) rootView.findViewById(R.id.iv_user_sex);
        mRecyclerView = (MaxRecyclerView) rootView.findViewById(R.id.rv_photo);
        itemLayout =  (LinearLayout) rootView.findViewById(R.id.ll_item);
    }


    @Override
    public void setPhotoInfo(PhotoInfo basePhotoInfo, int p)
    {
        mDescTv.setText(basePhotoInfo.getDesc());
        mUpdateTimeTv.setText(basePhotoInfo.getAdd_time());
        UserInfo userInfo = basePhotoInfo.getUserInfo();
        if (null != userInfo)
        {
            mUserNameTv.setText(userInfo.getUnick());
            ImageLoader.getInstance().displayImage(userInfo.getUface(), mUserPicIv);

            if ("男".equals(userInfo.getSex()))
            {
                mSexIv.setImageResource(R.drawable.ic_man);
            }
            else
            {
                mSexIv.setImageResource(R.drawable.ic_woman);
            }
        }
        itemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v,p);
            }
        });
        List<PhotoInfo> list = new ArrayList<>();
        mRecyclerView.setLayoutManager(new FullyGridLayoutManager(context, 3));

        for (int i = 0; i < basePhotoInfo.getFreePic().size(); i++)
        {
            if (i < 3)
            {
                PhotoInfo mPhotoInfo = new PhotoInfo();
                mPhotoInfo.setPicUrl(basePhotoInfo.getFreePic().get(i));
                list.add(mPhotoInfo);
            }
        }

        mRecyclerView.setAdapter(new PhotoDescAdapter(list, listener));
    }
}
