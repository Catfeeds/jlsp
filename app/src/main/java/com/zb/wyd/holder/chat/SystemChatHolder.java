package com.zb.wyd.holder.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zb.wyd.R;
import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.listener.MyItemClickListener;


/**
 * DESC: 系统--聊天
 */
public class SystemChatHolder extends ChatBaseHolder
{
    private TextView mContentTv;
private Context mContext;
    private MyItemClickListener listener;
    public SystemChatHolder(View rootView,Context mContext, MyItemClickListener listener)
    {
        super(rootView);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        this.mContext=mContext;
        this.listener = listener;
    }

    @Override
    public void setChatInfo(ChatInfo mChatInfo, final int p)
    {

        mContentTv.setText(mChatInfo.getData());
        mContentTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p)
                ;
            }
        });
    }


}
