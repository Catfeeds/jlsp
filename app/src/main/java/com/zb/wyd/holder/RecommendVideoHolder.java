package com.zb.wyd.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.widget.RoundAngleImageView;


/**
 */
public class RecommendVideoHolder extends RecyclerView.ViewHolder
{
    private TextView            mNameTv;
    private RoundAngleImageView mImgIv;
    private MyItemClickListener listener;
    private Context             context;

    public RecommendVideoHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mImgIv = (RoundAngleImageView) rootView.findViewById(R.id.iv_user_pic);
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dm_10) * 3;
        int width = (APPUtils.getScreenWidth(context) - spacingInPixels) / 2;
        LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(width, width * 13 / 20);
        mImgIv.setLayoutParams(imgLayoutParams);
        mImgIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    public void setVideoInfo(VideoInfo mVideoInfo, final int p)
    {

        ImageLoader.getInstance().displayImage(mVideoInfo.getCover(), mImgIv);
        mNameTv.setText(mVideoInfo.getV_name());

        mImgIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
    }


}
