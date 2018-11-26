package com.zb.wyd.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.activity.SpaceImageDetailActivity;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.widget.RoundAngleImageView;


/**
 */
public class PhotoHolder extends RecyclerView.ViewHolder
{
    private ImageView           mImgIv;
    private Context mContext;
    public PhotoHolder(View rootView, Context mContext)
    {
        super(rootView);
        this.mContext = mContext;
        mImgIv = (ImageView) rootView.findViewById(R.id.iv_photo);
        int spacingInPixels = mContext.getResources().getDimensionPixelSize(R.dimen.dm_10) * 5;
        int width = (APPUtils.getScreenWidth(mContext) - spacingInPixels) / 3;
        RelativeLayout.LayoutParams imgLayoutParams = new RelativeLayout.LayoutParams(width, width );
        mImgIv.setLayoutParams(imgLayoutParams);
        mImgIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    public void setPhoto(String picUri, final int p)
    {
        ImageLoader.getInstance().displayImage(picUri, mImgIv);

        mImgIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
                intent.putExtra("url", picUri);
                int[] location = new int[2];
                mImgIv.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);

                intent.putExtra("width", mImgIv.getWidth());
                intent.putExtra("height", mImgIv.getHeight());
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(0, 0);
            }
        });
    }


}
