package com.zb.wyd.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.entity.SearchInfo;
import com.zb.wyd.listener.MyItemClickListener;


/**
 */
public class SearchHolder extends RecyclerView.ViewHolder
{
    private ImageView mTypeIv;
    private TextView mNameTv;
    private TextView mUpateTimeTv;
    private TextView mViewsTv;
    private Context context;
    private MyItemClickListener listener;
    private LinearLayout mItemLayout;

    @SuppressLint("WrongViewCast")
    public SearchHolder(View rootView, Context mContext, MyItemClickListener listener)
    {
        super(rootView);
        this.context = mContext;
        this.listener = listener;
        mTypeIv = (ImageView) rootView.findViewById(R.id.iv_type);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mUpateTimeTv = (TextView) rootView.findViewById(R.id.tv_update_time);
        mViewsTv = (TextView) rootView.findViewById(R.id.tv_views);
        mItemLayout= (LinearLayout) rootView.findViewById(R.id.ll_item);
    }


    public void setSearchInfo(SearchInfo mSearchInfo, final int p)
    {

        mNameTv.setText(mSearchInfo.getTitle());
        mUpateTimeTv.setText(mSearchInfo.getAdd_time());

        if ("1".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeIv.setImageResource(R.drawable.ic_search_live);
        }
        else if ("2".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeIv.setImageResource(R.drawable.ic_search_video);
        }
        else if ("3".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeIv.setImageResource(R.drawable.ic_search_video);
        }
        else if ("5".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeIv.setImageResource(R.drawable.ic_search_dy);
        }
        else if ("6".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeIv.setImageResource(R.drawable.ic_search_novel);
        }
        else if ("7".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeIv.setImageResource(R.drawable.ic_search_novel);
        }

        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v , p);
            }
        });
    }


}
