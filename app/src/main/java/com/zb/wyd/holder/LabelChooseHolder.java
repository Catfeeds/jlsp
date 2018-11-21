package com.zb.wyd.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.entity.CategoryInfo;


/**
 */
public class LabelChooseHolder extends RecyclerView.ViewHolder
{
    private TextView            mNameTv;

    public LabelChooseHolder(View rootView)
    {
        super(rootView);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
    }


    public void setCataInfo(CategoryInfo mCataInfo)
    {
        mNameTv.setText(mCataInfo.getName());

    }

}
