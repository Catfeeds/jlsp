package com.zb.wyd.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.listener.MyItemClickListener;


/**
 */
public class CataFilterHolder extends RecyclerView.ViewHolder
{
    private TextView            mNameTv;
    private LinearLayout        mItemLayout;
    private MyItemClickListener listener;

    public CataFilterHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
    }


    public void setCataInfo(CategoryInfo mCataInfo, final int p)
    {
        mNameTv.setText(mCataInfo.getName());
        mNameTv.setSelected(mCataInfo.isSelected());
        mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v,p);
            }
        });
    }


}
