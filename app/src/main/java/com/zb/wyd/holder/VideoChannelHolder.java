package com.zb.wyd.holder;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.entity.ChannelInfo;
import com.zb.wyd.entity.HostInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.widget.RoundAngleImageView;


/**
 */
public class VideoChannelHolder extends RecyclerView.ViewHolder
{
    private TextView mNameTv;
    private MyItemClickListener listener;
    private Context context;

    public VideoChannelHolder(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mNameTv = (TextView) rootView.findViewById(R.id.tv_channel);
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dm_10) * 6;
        int width = (APPUtils.getScreenWidth(context) - spacingInPixels) / 3;
        LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(width, ActionBar.LayoutParams.WRAP_CONTENT);
        mNameTv.setLayoutParams(imgLayoutParams);
        mNameTv.setGravity(Gravity.CENTER);
    }


    public void setHostInfo(HostInfo mHostInfo, final int p)
    {


        if (mHostInfo.isSelected())
        {
            mNameTv.setSelected(true);
        }
        else
        {
            mNameTv.setSelected(false);
        }
        mNameTv.setText(mHostInfo.getTitle());

        mNameTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
    }


}
