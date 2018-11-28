package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.adapter.LabelAdapter;
import com.zb.wyd.adapter.LabelChooseAdapter;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.CataInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：标签
 */
public class LabelActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    TextView        tvTitle;
    @BindView(R.id.tv_submit)
    TextView        tvSubmit;
    @BindView(R.id.rv_label_choose)
    MaxRecyclerView rvLabelChoose;
    @BindView(R.id.rv_label_list)
    RecyclerView    mLabelRecyclerView;


    private List<CategoryInfo> labelList       = new ArrayList<>();
    private List<CategoryInfo> labelChooseList = new ArrayList<>();

    private LabelAdapter       mLabelAdapter;
    private LabelChooseAdapter mLabelChooseAdapter;


    private static final String GET_CATA_LIST = "get_cata_list";


    private static final int         REQUEST_SUCCESS = 0x01;
    private static final int         REQUEST_FAIL    = 0x02;
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler        = new BaseHandler(LabelActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    CataInfoListHandler mCataInfoListHandler = (CataInfoListHandler) msg.obj;
                    labelList.addAll(mCataInfoListHandler.getCataInfoList());
                    mLabelAdapter.notifyDataSetChanged();
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(LabelActivity.this, msg.obj.toString());

                    break;


            }
        }
    };

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_label);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(LabelActivity.this, false);

    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("标签");
        tvSubmit.setText("完成");
        tvSubmit.setVisibility(View.VISIBLE);
        rvLabelChoose.setLayoutManager(new FullyGridLayoutManager(LabelActivity.this, 3));
        mLabelChooseAdapter = new LabelChooseAdapter(labelChooseList);
        rvLabelChoose.setAdapter(mLabelChooseAdapter);


        mLabelRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mLabelAdapter = new LabelAdapter(labelList, LabelActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (labelList.get(position).isSelected())
                {
                    labelList.get(position).setSelected(false);
                }
                else
                {
                    if (labelChooseList.size() < 6)
                    {
                        labelList.get(position).setSelected(true);
                    }
                    else
                    {
                        ToastUtil.show(LabelActivity.this, "最多选择6个标签");
                    }
                }

                mLabelAdapter.notifyDataSetChanged();

                labelChooseList.clear();
                for (int i = 0; i < labelList.size(); i++)
                {
                    if (labelList.get(i).isSelected())
                    {
                        labelChooseList.add(labelList.get(i));
                    }

                }

                mLabelChooseAdapter.notifyDataSetChanged();

            }
        });
        mLabelRecyclerView.setAdapter(mLabelAdapter);

        getLabelList();
    }

    private void getLabelList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", "1");
        valuePairs.put("num", "150");
        valuePairs.put("co_biz","photo");
        DataRequest.instance().request(LabelActivity.this, Urls.getTagsUrl(), this, HttpRequest.GET, GET_CATA_LIST, valuePairs,
                new CataInfoListHandler());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvSubmit)
        {
            setResult(Activity.RESULT_OK, new Intent().putExtra("LABEL_LIST", (Serializable) labelChooseList));
            finish();
        }
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {

        if (GET_CATA_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
