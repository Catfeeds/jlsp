package com.zb.wyd.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
public class FreeTimeHolder extends RecyclerView.ViewHolder
{
    private TextView            mTitleTv;
    private TextView            mIndexTv;
    private TextView            mNameTv;
    private TextView            mBrowseTv;
    private RoundAngleImageView mImgIv;
    private RelativeLayout      mItemLayout;
    private MyItemClickListener listener;
    private Context             context;

    public FreeTimeHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mImgIv = (RoundAngleImageView) rootView.findViewById(R.id.iv_user_pic);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.rl_item);

    }


    public void setVideoInfo(VideoInfo mVideoInfo)
    {
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dm_10) * 4;
        int width = (APPUtils.getScreenWidth(context) - spacingInPixels) / 3;
        mItemLayout.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        ImageLoader.getInstance().displayImage("http://img3.imgtn.bdimg.com/it/u=3326312922,67097319&fm=27&gp=0.jpg", mImgIv);
    }


}
