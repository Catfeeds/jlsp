package com.zb.wyd.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.listener.MyItemClickListener;


/**
 */
public class LabelHolder extends RecyclerView.ViewHolder
{
    private TextView            mNameTv;
    private LinearLayout        mItemLayout;
    private ImageView           mSelectedIv;
    private MyItemClickListener listener;

    public LabelHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
        mSelectedIv = (ImageView) rootView.findViewById(R.id.iv_selected);
    }


    public void setCataInfo(CategoryInfo mCataInfo, final int p)
    {
        mNameTv.setText(mCataInfo.getName());

        if (mCataInfo.isSelected())
        {
            mNameTv.setSelected(true);
            mSelectedIv.setVisibility(View.VISIBLE);
        }
        else
        {
            mNameTv.setSelected(false);
            mSelectedIv.setVisibility(View.GONE);
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
