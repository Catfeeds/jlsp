package com.zb.wyd.holder.chat;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.widget.CircleImageView;


/**
 * DESC: 系统--聊天
 */
public class MyChatHolder extends ChatBaseHolder
{
    private TextView mContentTv;
    private Context mContext;
    private MyItemClickListener listener;
    private CircleImageView mUserPicIv;
    private TextView mTimeTv;

    public MyChatHolder(View rootView, Context mContext, MyItemClickListener listener)
    {
        super(rootView);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        mUserPicIv = (CircleImageView) rootView.findViewById(R.id.iv_user_pic);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);

        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public void setChatInfo(ChatInfo mChatInfo, int p)
    {
        mContentTv.setText(mChatInfo.getContent());
        mTimeTv.setText(mChatInfo.getAdd_time());
        if(null !=mChatInfo.getUserInfo())
        {
            ImageLoader.getInstance().displayImage(mChatInfo.getUserInfo().getUface(), mUserPicIv);
        }



    }


}
