package com.zb.wyd.holder;

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
import com.zb.wyd.entity.PicInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;


/**
 */
public class PhotoItemHolder extends PhotoBaseHolder
{
    private ImageView           mImgIv;
    private MyItemClickListener listener;
    private RelativeLayout      mItemLayout;

    private ImageView mDelIv;

    public PhotoItemHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView, context, listener);
        this.listener = listener;
        mImgIv = (ImageView) rootView.findViewById(R.id.iv_photo);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.ll_item);
        mDelIv = (ImageView) rootView.findViewById(R.id.iv_del);
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dm_10) * 3;
        int width = (APPUtils.getScreenWidth(context) - spacingInPixels) / 4;
        int height = width * 8 / 7;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.topMargin=10;
        mItemLayout.setLayoutParams(layoutParams);

    }


    public void setPhoto(PicInfo mPicInfo, final int p)
    {
        ImageLoader.getInstance().displayImage(mPicInfo.getAbsolutelyPath(), mImgIv);

        mDelIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
    }


}
