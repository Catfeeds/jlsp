package com.zb.wyd.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.activity.SpaceImageDetailActivity;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.widget.RoundAngleImageView;


/**
 */
public class PhotoDescHolder extends RecyclerView.ViewHolder
{
    private RoundAngleImageView mImgIv;
    private Context mContext;

    public PhotoDescHolder(View rootView, Context mContext)
    {
        super(rootView);
        this.mContext = mContext;
        mImgIv = (RoundAngleImageView) rootView.findViewById(R.id.iv_photo);

    }


    public void setPhotoInfo(PhotoInfo mPhotoInfo, final int p)
    {
        ImageLoader.getInstance().displayImage(mPhotoInfo.getPicUrl(), mImgIv);
    }


}
