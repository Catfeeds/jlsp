package com.zb.wyd.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
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
public class PhotoSortPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener,View.OnClickListener
{
    View rootView;
    private Activity mContext;
    private MyItemClickListener listener;
    private TextView tvNew;
    private TextView tvCollection;
    private TextView tvScore;

    public PhotoSortPopupWindow(Activity context, MyItemClickListener listener)
    {
        super(context);
        this.mContext = context;
        this.listener = listener;
        rootView = LayoutInflater.from(context).inflate(R.layout.pop_photo_sort, null);
        setContentView(rootView);
        setOutsideTouchable(false);
        setFocusable(false);
        setBackgroundDrawable(new ColorDrawable());
        setWidth(500);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();
        initEvent();
        //        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        //        lp.alpha = 0.5f; //0.0-1.0
        //        mContext.getWindow().setAttributes(lp);

    }


    private void initView()
    {
        tvNew = (TextView)rootView.findViewById(R.id.tv_new);
        tvCollection = (TextView)rootView.findViewById(R.id.tv_collection);
        tvScore = (TextView)rootView.findViewById(R.id.tv_score);

    }

    private void initEvent()
    {
        tvNew.setOnClickListener(this);
        tvCollection.setOnClickListener(this);
        tvScore.setOnClickListener(this);
    }


    @Override
    public void onDismiss()
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v)
    {
        if(v == tvNew)
        {
            tvNew.setTextColor(ContextCompat.getColor(mContext,R.color.text_blue2));
            tvCollection.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet_normal));
            tvScore.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet_normal));
            listener.onItemClick(v,0);
        }
        else if(v == tvCollection)
        {
            tvNew.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet_normal));
            tvCollection.setTextColor(ContextCompat.getColor(mContext,R.color.text_blue2));
            tvScore.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet_normal));
            listener.onItemClick(v,1);
        }
        else if(v == tvScore)
        {
            tvNew.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet_normal));
            tvCollection.setTextColor(ContextCompat.getColor(mContext,R.color.text_violet_normal));
            tvScore.setTextColor(ContextCompat.getColor(mContext,R.color.text_blue2));
            listener.onItemClick(v,2);
        }
    }
}
