package com.zb.wyd.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.holder.LabelChooseHolder;

import java.util.List;

/**
 */
public class LabelChooseAdapter extends RecyclerView.Adapter<LabelChooseHolder>
{

    private List<CategoryInfo> list;

    public LabelChooseAdapter(List<CategoryInfo> list)
    {
        this.list = list;
    }

    @Override
    public LabelChooseHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label_choose, parent, false);
        LabelChooseHolder mHolder = new LabelChooseHolder(itemView);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(LabelChooseHolder holder, int position)
    {
        CategoryInfo mCataInfo = list.get(position);
        holder.setCataInfo(mCataInfo);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
