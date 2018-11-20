package com.zb.wyd.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.widget.RoundAngleImageView;


/**
 */
public class RecommendHolder extends RecyclerView.ViewHolder
{
    private TextView            mFollowTv;
    private TextView            mPopularityTv;
    private TextView            mNameTv;
    private TextView            mStatusTv;
    private RoundAngleImageView mImgIv;
    private RelativeLayout      mItemLayout;
    private MyItemClickListener listener;
    private Context             context;

    public RecommendHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mFollowTv = (TextView) rootView.findViewById(R.id.tv_follow);
        mPopularityTv = (TextView) rootView.findViewById(R.id.tv_popularity);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mImgIv = (RoundAngleImageView) rootView.findViewById(R.id.iv_user_pic);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.rl_item);
        mStatusTv = (TextView) rootView.findViewById(R.id.tv_status);
    }


    public void setLiveInfo(LiveInfo mLiveInfo, final int p)
    {
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dm_10) * 3;
        int width = (APPUtils.getScreenWidth(context) - spacingInPixels) / 2;
        mItemLayout.setLayoutParams(new LinearLayout.LayoutParams(width, (int)(width*0.78)));
        ImageLoader.getInstance().displayImage(mLiveInfo.getFace(), mImgIv);
        mFollowTv.setText(mLiveInfo.getFavour_count());
        mPopularityTv.setText(mLiveInfo.getOnline());
        mNameTv.setText(mLiveInfo.getNick());

        if("1".equals(mLiveInfo.getIs_live()))
        {
            mStatusTv.setBackgroundResource(R.drawable.common_orange_3dp);
        }
        else
        {
            mStatusTv.setBackgroundResource(R.drawable.common_gray_3dp);
        }
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
