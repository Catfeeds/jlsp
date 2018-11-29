package com.zb.wyd.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.activity.SpaceImageDetailActivity;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.widget.RoundAngleImageView;


/**
 */
public class PhotoDescHolder extends RecyclerView.ViewHolder
{
    private RoundAngleImageView mImgIv;
    private Context context;
    private MyItemClickListener listener;

    @SuppressLint("WrongViewCast")
    public PhotoDescHolder(View rootView, Context mContext, MyItemClickListener listener)
    {
        super(rootView);
        this.context = mContext;
        this.listener = listener;
        mImgIv = (RoundAngleImageView) rootView.findViewById(R.id.iv_photo);
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dm_10) * 5;
        int width = (APPUtils.getScreenWidth(context) - spacingInPixels) / 3;
        LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(width, width);
        mImgIv.setLayoutParams(imgLayoutParams);
        mImgIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    public void setPhotoInfo(PhotoInfo mPhotoInfo, final int p)
    {
        ImageLoader.getInstance().displayImage(mPhotoInfo.getPicUrl(), mImgIv);
        mImgIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onItemClick(view, p);
            }
        });
    }


}
