package com.zb.wyd.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.zb.wyd.R;
import com.zb.wyd.adapter.CataFilterAdapter;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件筛选
 */
public class CategoryPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener
{
    View rootView;
    private MaxRecyclerView     mCataRv;
    private Activity            mContext;
    private CataFilterAdapter   mAdapter;
    private TextView            mClosedTv;
    private MyItemClickListener listener;
    List<CategoryInfo> mFilterList = new ArrayList<>();

    public CategoryPopupWindow(Activity context, List<CategoryInfo> mFilterList, MyItemClickListener listener)
    {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.mFilterList = mFilterList;
        rootView = LayoutInflater.from(context).inflate(R.layout.pop_cata, null);
        setContentView(rootView);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();
        initEvent();
//        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//        lp.alpha = 0.5f; //0.0-1.0
//        mContext.getWindow().setAttributes(lp);

    }


    private void initView()
    {
        mClosedTv = (TextView) rootView.findViewById(R.id.tv_closed);
        mCataRv = (MaxRecyclerView) rootView.findViewById(R.id.rv_cata);
        mCataRv.setLayoutManager(new FullyGridLayoutManager(mContext, 5));
        mAdapter = new CataFilterAdapter(mFilterList, mContext, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                listener.onItemClick(view, position);
                dismiss();
            }
        });
        mCataRv.setAdapter(mAdapter);
    }

    private void initEvent()
    {
        mClosedTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

    }


    @Override
    public void onDismiss()
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }
}
