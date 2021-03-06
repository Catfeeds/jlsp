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
public class DyHolder extends RecyclerView.ViewHolder
{
    private TextView            mFollowTv;
    private TextView            mPopularityTv;
    private TextView            mNameTv;
    private RoundAngleImageView mImgIv;
    private RelativeLayout      mItemLayout;
    private MyItemClickListener listener;
    private Context             context;

    public DyHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mFollowTv = (TextView) rootView.findViewById(R.id.tv_follow);
        mPopularityTv = (TextView) rootView.findViewById(R.id.tv_popularity);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mImgIv = (RoundAngleImageView) rootView.findViewById(R.id.iv_user_pic);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.rl_item);
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dm_10) * 3;
        int width = (APPUtils.getScreenWidth(context) - spacingInPixels) / 2;
        mItemLayout.setLayoutParams(new LinearLayout.LayoutParams(width, width * 4/3));
        RelativeLayout.LayoutParams imgLayoutParams = new RelativeLayout.LayoutParams(width, width * 4/3);
        mImgIv.setLayoutParams(imgLayoutParams);
        mImgIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    public void setVideoInfo(VideoInfo mVideoInfo, final int p)
    {

        ImageLoader.getInstance().displayImage(mVideoInfo.getCover(), mImgIv);
        mFollowTv.setText(mVideoInfo.getFavour_count());
       // mPopularityTv.setText(mVideoInfo.getCoupon());
        mNameTv.setText(mVideoInfo.getV_name());

        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
    }


}
