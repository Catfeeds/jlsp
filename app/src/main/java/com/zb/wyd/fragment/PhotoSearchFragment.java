package com.zb.wyd.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.activity.BaseHandler;
import com.zb.wyd.activity.LoginActivity;
import com.zb.wyd.activity.PhotoDetailActivity;
import com.zb.wyd.activity.SearchActivity;
import com.zb.wyd.adapter.SelfieAdapter;
import com.zb.wyd.entity.SelfieInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.SelfieInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.listener.SearchListener;
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
 * 描述：搜索自拍列表
 */
public class PhotoSearchFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView>, IRequestListener, View.OnClickListener
{
    @BindView(R.id.refreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private int pn = 1;
    private int mRefreshStatus;


    private List<SelfieInfo> selfieInfoList = new ArrayList<>();
    private SelfieAdapter mSelfieAdapter;
    private View rootView = null;
    private Unbinder unbinder;
    private static final String GET_PHOTO_LIST = "get_photo_list";

    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL    = 0x02;

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
                    SelfieInfoListHandler mSelfieInfoListHandler = (SelfieInfoListHandler) msg.obj;
                    if (!mSelfieInfoListHandler.getSelfieInfoList().isEmpty())
                    {
                        selfieInfoList.addAll(mSelfieInfoListHandler.getSelfieInfoList());
                        mSelfieAdapter.notifyDataSetChanged();
                    }

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());

                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_collect_photo, null);
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
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    //获取最后一个完全显示的ItemPosition
                    int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部
                    if (lastVisiblePos == (totalItemCount - 1) && isSlidingToLast)
                    {
                        //加载更多功能的代码
                        //                        Ln.e("howes right="+manager.findLastCompletelyVisibleItemPosition());
                        //                        Toast.makeText(getActivityContext(),"加载更多",0).show();

                        pn += 1;
                        loadData();


                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dy > 0)
                {
                    //大于0表示，正在向下滚动
                    isSlidingToLast = true;
                }
                else
                {
                    //小于等于0 表示停止或向上滚动
                    isSlidingToLast = false;
                }

            }
        });

    }

    private int getMaxElem(int[] arr)
    {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++)
        {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    @Override
    protected void initViewData()
    {
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        mSelfieAdapter = new SelfieAdapter(selfieInfoList, getActivity(), new MyItemClickListener()
//        {
//            @Override
//            public void onItemClick(View view, int position)
//            {
//                if (MyApplication.getInstance().isLogin())
//                {
//                    startActivity(new Intent(getActivity(), PhotoDetailActivity.class).putExtra("biz_id", selfieInfoList.get(position).getId()));
//
//                }
//                else
//                {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//
//            }
//        });
        mRecyclerView.setAdapter(mSelfieAdapter);

        ( (SearchActivity)getActivity()).setPhotoSearchListener(new SearchListener() {
            @Override
            public void setKeyword(String keyword)
            {
                PhotoSearchFragment.this.keyword = keyword;
                selfieInfoList.clear();
                pn = 1;
                mRefreshStatus = 0;
                loadData();
            }
        });
    }

    private String keyword;


    private void loadData()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "20");
        valuePairs.put("co_biz", "photo");
        valuePairs.put("keyword", keyword);
        DataRequest.instance().request(getActivity(), Urls.getSearchUrl(), this, HttpRequest.GET, GET_PHOTO_LIST, valuePairs,
                new SelfieInfoListHandler());
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

        if (GET_PHOTO_LIST.equals(action))
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
        selfieInfoList.clear();
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
    }
}
