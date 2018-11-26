package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.SearchInfo;
import com.zb.wyd.holder.AnchorHolder;
import com.zb.wyd.holder.SearchHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchHolder>
{

    private MyItemClickListener listener;
    private List<SearchInfo>      list;
    private Context             mContext;

    public SearchAdapter(List<SearchInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        SearchHolder mHolder = new SearchHolder(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(SearchHolder holder, int position)
    {
        SearchInfo mSearchInfo = list.get(position);
        holder.setSearchInfo(mSearchInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
