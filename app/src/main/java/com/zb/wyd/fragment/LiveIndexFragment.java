package com.zb.wyd.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dalong.marqueeview.MarqueeView;
import com.donkingliang.banner.CustomBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.activity.BaseHandler;
import com.zb.wyd.activity.LiveActivity;
import com.zb.wyd.activity.LoginActivity;
import com.zb.wyd.activity.PhotoDetailActivity;
import com.zb.wyd.activity.VideoPlayActivity;
import com.zb.wyd.activity.WebViewActivity;
import com.zb.wyd.adapter.NewAdapter;
import com.zb.wyd.adapter.RecommendAdapter;
import com.zb.wyd.entity.AdInfo;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.NoticeInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.AdInfoListHandler;
import com.zb.wyd.json.LiveInfoListHandler;
import com.zb.wyd.json.NoticeListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MarqueeTextView;
import com.zb.wyd.widget.MaxRecyclerView;
import com.zb.wyd.widget.VerticalSwipeRefreshLayout;
import com.zb.wyd.widget.list.refresh.PullToRefreshBase;
import com.zb.wyd.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextView;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextViewUtil;

/**
 * 描述：一句话简单描述
 */
public class LiveIndexFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IRequestListener, View.OnClickListener,
        PullToRefreshBase.OnRefreshListener<RecyclerView>
{


    @BindView(R.id.banner)
    CustomBanner mBanner;


    @BindView(R.id.swipeRefresh)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.refreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private int pn = 1;
    private int mRefreshStatus;
    private View rootView = null;
    private Unbinder unbinder;


    private List<String> picList = new ArrayList<>();
    private List<LiveInfo> recommendLiveList = new ArrayList<>();
    private List<AdInfo> adInfoList = new ArrayList<>();
    private RecommendAdapter mRecommendAdapter;
    private int getFreeCount;


    private static final String GET_RECOMMEND_LIVE = "get_recommend_live";
    private static final String GET_AD_LIST = "get_ad_list";
    private static final int GET_RECOMMEND_LIVE_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;

    private static final int GET_AD_LIST_CODE = 0X10;
    private static final int GET_RECOMMEND_LIVE_CODE = 0X11;

    private static final int GET_RECOMMEND_LIVE_FAIL = 0X13;
    private static final int GET_AD_LIST_SUCCESS = 0x04;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_RECOMMEND_LIVE_SUCCESS:

                    LiveInfoListHandler mLiveInfoListHandler = (LiveInfoListHandler) msg.obj;

                    if (pn == 1)
                    {
                        recommendLiveList.clear();
                    }
                    recommendLiveList.addAll(mLiveInfoListHandler.getLiveInfoList());
                    mRecommendAdapter.notifyDataSetChanged();

                    break;


                case REQUEST_FAIL:
                    break;

                case GET_RECOMMEND_LIVE_CODE:
                    getRecommendLive();
                    break;


                case GET_AD_LIST_SUCCESS:
                    AdInfoListHandler mAdInfoListHandler = (AdInfoListHandler) msg.obj;
                    adInfoList.clear();
                    adInfoList.addAll(mAdInfoListHandler.getAdInfoList());

                    picList.clear();

                    for (int i = 0; i < adInfoList.size(); i++)
                    {
                        picList.add(adInfoList.get(i).getImage());
                    }

                    if (!picList.isEmpty())
                    {
                        initAd();
                    }
                    break;

                case GET_AD_LIST_CODE:
                    getAdList();
                    break;


                case GET_RECOMMEND_LIVE_FAIL:
                    getFreeCount++;
                    if (getFreeCount <= 30)
                    {
                        mHandler.sendEmptyMessage(GET_RECOMMEND_LIVE_CODE);
                    }

                    break;

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_live_index, null);
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
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initViewData()
    {
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        mRecommendAdapter = new RecommendAdapter(recommendLiveList, getContext(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (MyApplication.getInstance().isLogin())
                {
                    LiveInfo mLiveInfo = recommendLiveList.get(position);
                    startActivity(new Intent(getActivity(), LiveActivity.class).putExtra("biz_id", mLiveInfo.getId()));

                }
                else
                {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        //解决swipelayout与Recyclerview的冲突
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mRecyclerView.setAdapter(mRecommendAdapter);
        loadData();
        mHandler.sendEmptyMessage(GET_AD_LIST_CODE);
    }


    private void loadData()
    {
        //mHandler.sendEmptyMessage(GET_RECOMMEND_LIVE_CODE);
    }

    private void getAdList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pos_id", "1");
        DataRequest.instance().request(getActivity(), Urls.getAdListUrl(), this, HttpRequest.GET, GET_AD_LIST, valuePairs, new AdInfoListHandler());
    }


    private void getRecommendLive()
    {

        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "20");
        valuePairs.put("location", MyApplication.getInstance().getLocation());
        valuePairs.put("sort", "hot");
        DataRequest.instance().request(getActivity(), Urls.getNewLive(), this, HttpRequest.GET, GET_RECOMMEND_LIVE, valuePairs, new
                LiveInfoListHandler());
    }


    private void initAd()
    {
        int width = APPUtils.getScreenWidth(getActivity());
        int height = (int) (width * 0.4);
        if (null == mBanner)
        {
            return;
        }
        mBanner.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        mBanner.setVisibility(View.VISIBLE);
        mBanner.setPages(new CustomBanner.ViewCreator<String>()
        {
            @Override
            public View createView(Context context, int position)
            {
                //这里返回的是轮播图的项的布局 支持任何的布局
                //position 轮播图的第几个项
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String data)
            {
                //在这里更新轮播图的UI
                //position 轮播图的第几个项
                //view 轮播图当前项的布局 它是createView方法的返回值
                //data 轮播图当前项对应的数据
                //   Glide.with(context).load(data).into((ImageView) view);
                ImageLoader.getInstance().displayImage(picList.get(position), (ImageView) view);
            }
        }, picList);


        //设置指示器类型，有普通指示器(ORDINARY)、数字指示器(NUMBER)和没有指示器(NONE)三种类型。
        //这个方法跟在布局中设置app:indicatorStyle是一样的
        mBanner.setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY);

        //设置两个点图片作为翻页指示器，只有指示器为普通指示器(ORDINARY)时有用。
        //这个方法跟在布局中设置app:indicatorSelectRes、app:indicatorUnSelectRes是一样的。
        //第一个参数是指示器的选中的样式，第二个参数是指示器的未选中的样式。
        // mBanner.setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect);

        //设置指示器的指示点间隔，只有指示器为普通指示器(ORDINARY)时有用。
        //这个方法跟在布局中设置app:indicatorInterval是一样的。
        mBanner.setIndicatorInterval(20);

        //设置指示器的方向。
        //这个方法跟在布局中设置app:indicatorGravity是一样的。
        //        mBanner.setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER_HORIZONTAL);
        //设置轮播图自动滚动轮播，参数是轮播图滚动的间隔时间
        //轮播图默认是不自动滚动的，如果不调用这个方法，轮播图将不会自动滚动。
        mBanner.startTurning(3600);

        //停止轮播图的自动滚动
        // mBanner.stopTurning();

        //设置轮播图的滚动速度
        mBanner.setScrollDuration(500);

        //设置轮播图的点击事件
        mBanner.setOnPageClickListener(new CustomBanner.OnPageClickListener<String>()
        {
            @Override
            public void onPageClick(int position, String str)
            {
                //position 轮播图的第几个项
                //str 轮播图当前项对应的数据
                AdInfo mAdInfo = adInfoList.get(position);
                adClick(mAdInfo.getLink());
            }
        });

    }


    private void adClick(String link)
    {
        if (!TextUtils.isEmpty(link))
        {
            if (link.startsWith("video://"))
            {
                String id = link.replace("video://", "");
                VideoInfo mVideoInfo = new VideoInfo();
                mVideoInfo.setId(id);
                mVideoInfo.setV_name("点播");
                Bundle b = new Bundle();
                b.putSerializable("VideoInfo", mVideoInfo);
                startActivity(new Intent(getActivity(), VideoPlayActivity.class).putExtras(b));
            }
            else if (link.startsWith("live://"))
            {
                String id = link.replace("live://", "");
                startActivity(new Intent(getActivity(), LiveActivity.class).putExtra("biz_id", id));
            }
            else if (link.startsWith("photo://"))
            {
                String id = link.replace("photo://", "");
                startActivity(new Intent(getActivity(), PhotoDetailActivity.class).putExtra("biz_id", id));
            }
            else if (link.startsWith("http"))
            {
                startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "详情").putExtra(WebViewActivity
                        .IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, link));
            }
        }
    }

    @Override
    public void onRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            pn = 1;
            loadData();
            mSwipeRefreshLayout.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
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
        if (GET_RECOMMEND_LIVE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_RECOMMEND_LIVE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_RECOMMEND_LIVE_CODE, resultMsg));
            }
        }

        else if (GET_AD_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_AD_LIST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }


    }

    @Override
    public void onClick(View v)
    {
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
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

    private int getMaxElem(int[] arr)
    {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++)
        {
            if (arr[i] > maxVal) maxVal = arr[i];
        }
        return maxVal;
    }

}
