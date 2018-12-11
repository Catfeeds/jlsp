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
import com.zb.wyd.entity.CommentInfo;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.RoundAngleImageView;


/**
 */
public class CommentHolder extends RecyclerView.ViewHolder
{
    private TextView mTimeTv;
    private TextView mNameTv;
    private TextView mContentTv;
    private CircleImageView mImgIv;

    public CommentHolder(View rootView)
    {
        super(rootView);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        mImgIv = (CircleImageView) rootView.findViewById(R.id.iv_user_pic);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
    }


    public void setCommentInfo(CommentInfo mComment)
    {
        UserInfo userInfo = mComment.getUserInfo();

        if (null != userInfo)
        {
            ImageLoader.getInstance().displayImage(userInfo.getUface(), mImgIv);
            mNameTv.setText(userInfo.getUnick());
        }
        mTimeTv.setText(mComment.getTime());
        mContentTv.setText(mComment.getContent());
    }


}
