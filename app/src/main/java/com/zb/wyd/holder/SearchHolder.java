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
    private TextView mTypeTv;
    private TextView mNameTv;
    private TextView mUpateTimeTv;
    private TextView mViewsTv;
    private Context context;
    private MyItemClickListener listener;
    private LinearLayout mItemLayout;
    private int labelBgArr[] = {R.drawable.lable1_3dp, R.drawable.lable2_3dp, R.drawable.lable3_3dp, R.drawable.lable4_3dp, R.drawable.lable5_3dp,
            R.drawable.lable6_3dp,};

    @SuppressLint("WrongViewCast")
    public SearchHolder(View rootView, Context mContext, MyItemClickListener listener)
    {
        super(rootView);
        this.context = mContext;
        this.listener = listener;
        mTypeTv = (TextView) rootView.findViewById(R.id.tv_type);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mUpateTimeTv = (TextView) rootView.findViewById(R.id.tv_update_time);
        mViewsTv = (TextView) rootView.findViewById(R.id.tv_views);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
    }


    public void setSearchInfo(SearchInfo mSearchInfo, final int p)
    {

        mNameTv.setText(mSearchInfo.getTitle());
        mUpateTimeTv.setText(mSearchInfo.getAdd_time());

        if ("1".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeTv.setText("直播");
            mTypeTv.setBackgroundResource(labelBgArr[0]);
        }
        else if ("2".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeTv.setText("视频");
            mTypeTv.setBackgroundResource(labelBgArr[1]);
        }
        else if ("3".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeTv.setText("自拍");
            mTypeTv.setBackgroundResource(labelBgArr[2]);
        }
        else if ("5".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeTv.setText("抖音");
            mTypeTv.setBackgroundResource(labelBgArr[3]);
        }
        else if ("6".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeTv.setText("小说");
            mTypeTv.setBackgroundResource(labelBgArr[4]);
        }
        else if ("7".endsWith(mSearchInfo.getCo_biz()))
        {
            mTypeTv.setText("宝箱");
            mTypeTv.setBackgroundResource(labelBgArr[5]);
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
