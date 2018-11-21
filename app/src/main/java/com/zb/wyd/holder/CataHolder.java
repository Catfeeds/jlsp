package com.zb.wyd.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.listener.MyItemClickListener;


/**
 */
public class CataHolder extends RecyclerView.ViewHolder
{
    private TextView            mNameTv;
    private View                mLine;
    private LinearLayout        mItemLayout;
    private MyItemClickListener listener;
    private Context mContext;

    public CataHolder(View rootView, Context mContext,MyItemClickListener listener)
    {
        super(rootView);
        this.mContext = mContext;
        this.listener = listener;
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
        mLine = (View) rootView.findViewById(R.id.line);
    }


    public void setCataInfo(CategoryInfo mCataInfo, final int p)
    {
        mNameTv.setText(mCataInfo.getName());
        if (mCataInfo.isSelected())
        {
            mNameTv.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet));
            mLine.setVisibility(View.VISIBLE);
        }
        else
        {
            mNameTv.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet_normal));
            mLine.setVisibility(View.INVISIBLE);
        }
        mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v,p);
            }
        });
    }


}
