package com.zb.wyd.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.activity.BaseHandler;
import com.zb.wyd.activity.LiveActivity;
import com.zb.wyd.activity.LoginActivity;
import com.zb.wyd.adapter.AnchorAdapter;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.LocationInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.LiveInfoListHandler;
import com.zb.wyd.json.LocationInfoHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.list.refresh.PullToRefreshBase;
import com.zb.wyd.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述：主播列表
 */
public class AnchorListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView>, IRequestListener, View.OnClickListener
{
    @BindView(R.id.tv_recommend)
    TextView                  tvRecommend;
    @BindView(R.id.tv_new)
    TextView                  tvNew;
    @BindView(R.id.tv_casual)
    TextView                  tvCasual;
    @BindView(R.id.iv_new)
    ImageView                 ivNew;
    @BindView(R.id.refreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private List<TextView> tabList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private int pn = 1;
    private int mRefreshStatus;

    private String sort = "hot";

    private List<LiveInfo> liveInfoList = new ArrayList<>();
    private AnchorAdapter mAnchorAdapter;
    private View rootView = null;
    private Unbinder unbinder;

    private static final String GET_ANCHOR_LIST = "get_anchor_list";
    private static final int    REQUEST_SUCCESS = 0x01;
    private static final int    REQUEST_FAIL    = 0x02;
    private static final int GET_ANCHOR_CODE      = 0X14;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    LiveInfoListHandler mOrderListHandler = (LiveInfoListHandler) msg.obj;

                    if (pn == 1)
                    {
                        liveInfoList.clear();
                    }

                    liveInfoList.addAll(mOrderListHandler.getLiveInfoList());
                    mAnchorAdapter.notifyDataSetChanged();
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;

                case GET_ANCHOR_CODE:
                    getAnchorList();
                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_anchor_list, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }


    private static AnchorListFragment instance = null;

    public static AnchorListFragment newInstance()
    {
        if (instance == null)
        {
            instance = new AnchorListFragment();
        }
        return instance;
    }


    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        tvRecommend.setOnClickListener(this);
        tvNew.setOnClickListener(this);
        tvCasual.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tabList.add(tvRecommend);
        tabList.add(tvNew);
        tabList.add(tvCasual);
        setTabSelected(0);
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAnchorAdapter = new AnchorAdapter(liveInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (MyApplication.getInstance().isLogin())
                {
                    LiveInfo mLiveInfo = liveInfoList.get(position);
                    startActivity(new Intent(getActivity(), LiveActivity.class)
                            .putExtra("biz_id", mLiveInfo.getId())
                            .putExtra("location", mLiveInfo.getLocation())
                    );
                }
                else
                {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

            }
        });
        mRecyclerView.setAdapter(mAnchorAdapter);
        loadData();
    }


    private void loadData()
    {
        getAnchorList();
    }

    private void getAnchorList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "20");
        valuePairs.put("sort", sort);
        valuePairs.put("location", MyApplication.getInstance().getLocation());
        DataRequest.instance().request(getActivity(), Urls.getNewLive(), this, HttpRequest.GET, GET_ANCHOR_LIST, valuePairs,
                new LiveInfoListHandler());
    }


    private void setTabSelected(int p)
    {

        for (int i = 0; i < tabList.size(); i++)
        {
            if (i == p)
            {
                tabList.get(i).setSelected(true);
            }
            else
            {
                tabList.get(i).setSelected(false);
            }
        }


        switch (p)
        {
            case 0:
                sort = "hot";
                break;
            case 1:
                sort = "fav";
                break;
            case 2:
                sort = "rand";
                break;
        }
        pn = 1;
        mRefreshStatus = 0;
        loadData();
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

        if (GET_ANCHOR_LIST.equals(action))
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
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        liveInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn = pn + 1;
        mRefreshStatus = 1;
        loadData();
    }

    @Override
    public void onClick(View v)
    {
        if (v == tvRecommend)
        {
            setTabSelected(0);
        }
        else if (v == tvNew)
        {
            setTabSelected(1);
        }
        else if (v == tvCasual)
        {
            setTabSelected(2);
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != unbinder)
        {
            unbinder.unbind();
            unbinder = null;
        }

        mHandler.removeCallbacksAndMessages(null);
    }
}
