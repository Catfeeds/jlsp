package com.zb.wyd.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.entity.RankingInfo;
import com.zb.wyd.widget.CircleImageView;


/**
 */
public class RankingHolder extends RecyclerView.ViewHolder
{
    private TextView        mNameTv;
    private TextView        mNumTv;
    private CircleImageView mUserPicIv;

    public RankingHolder(View rootView)
    {
        super(rootView);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mNumTv = (TextView) rootView.findViewById(R.id.tv_num);
        mUserPicIv = (CircleImageView) rootView.findViewById(R.id.iv_user_pic);
    }


    public void setRankingInfo(RankingInfo mRankingInfo)
    {
        ImageLoader.getInstance().displayImage(mRankingInfo.getUface(), mUserPicIv);

        mNameTv.setText(mRankingInfo.getUnick());
        mNumTv.setText("积分:" + mRankingInfo.getCoupon());

    }


}
