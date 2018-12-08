package com.zb.wyd.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.entity.MessageInfo;
import com.zb.wyd.listener.MyItemClickListener;


/**
 */
public class MyMessageHolder extends RecyclerView.ViewHolder
{
    private ImageView mUserPic;
    private TextView mNameTv;
    private TextView mTimeTv;
    private ImageView mUserSex;
    private TextView mMsgmunTv;
    private RelativeLayout mLayout;
    private MyItemClickListener listener;
    public MyMessageHolder(View rootView,MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        mUserPic = (ImageView) rootView.findViewById(R.id.iv_user_pic);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mUserSex = (ImageView) rootView.findViewById(R.id.iv_user_sex);
        mMsgmunTv = (TextView) rootView.findViewById(R.id.tv_msg_mun);
        mLayout = (RelativeLayout)rootView.findViewById(R.id.rl_item);
    }


    public void setMessageInfo(MessageInfo mMessageInfo,final int p)
    {
        mTimeTv.setText(mMessageInfo.getMsg_time());
        mNameTv.setText(mMessageInfo.getUnick());
        ImageLoader.getInstance().displayImage(mMessageInfo.getUface(), mUserPic);
        if ("男".equals(mMessageInfo.getSex()))
        {
            mUserSex.setImageResource(R.drawable.ic_man);
        }
        else
        {
            mUserSex.setImageResource(R.drawable.ic_woman);
        }

        if ("0".equals(mMessageInfo.getMsgnum()))
        {
            mMsgmunTv.setVisibility(View.GONE);
        }
        else
        {
            mMsgmunTv.setVisibility(View.VISIBLE);
            mMsgmunTv.setText(mMessageInfo.getMsgnum());
        }
        mLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v,p);
            }
        });
    }


}
