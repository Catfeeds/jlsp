package com.zb.wyd.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.donkingliang.banner.CustomBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.activity.BaseHandler;
import com.zb.wyd.activity.LiveActivity;
import com.zb.wyd.activity.LoginActivity;
import com.zb.wyd.activity.PhotoDetailActivity;
import com.zb.wyd.activity.VideoPlayActivity;
import com.zb.wyd.activity.VidoeListActivity;
import com.zb.wyd.adapter.CategoryAdapter;
import com.zb.wyd.adapter.IntegerAreaAdapter;
import com.zb.wyd.entity.AdInfo;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.AdInfoListHandler;
import com.zb.wyd.json.CataInfoListHandler;
import com.zb.wyd.json.VideoInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CategoryPopupWindow;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;
import com.zb.wyd.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述：
 */
public class VideoFragment1 extends BaseFragment implements IRequestListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener
{

    @BindView(R.id.banner)
    CustomBanner               mBanner;
    @BindView(R.id.iv_show)
    ImageView                  ivMore;
    @BindView(R.id.rv_cata)
    RecyclerView               rvCata;
    @BindView(R.id.topView)
    View                       topView;
    @BindView(R.id.rl_all_free)
    RelativeLayout             rlAllFree;
    @BindView(R.id.rv_free_time)
    MaxRecyclerView            rvFreeTime;
    @BindView(R.id.rl_all_integer)
    RelativeLayout             rlAllInteger;
    @BindView(R.id.rv_integer_area)
    MaxRecyclerView            rvIntegerArea;
    @BindView(R.id.swipeRefresh)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;

    private List<CategoryInfo> cataInfoList = new ArrayList<>();
    private CategoryAdapter mCataAdapter;
    private View rootView = null;
    private Unbinder unbinder;


    private List<String>    picList      = new ArrayList<>();
    private List<VideoInfo> newVideoList = new ArrayList<>();
    private List<VideoInfo> favVideoList = new ArrayList<>();
    private List<AdInfo>    adInfoList   = new ArrayList<>();


    private IntegerAreaAdapter mNewVideoAdapter;
    private IntegerAreaAdapter mFavVideoAdapter;


    private              String cta_id        = "0";
    private static final String GET_CATA_LIST = "get_cata_list";
    private static final String GET_NEW_VIDEO = "get_new_video";
    private static final String GET_FAV_VIDEO = "get_fav_video";
    private static final String GET_AD_LIST   = "get_ad_list";

    private static final int GET_NEW_LIVE_SUCCESS   = 0x01;
    private static final int REQUEST_FAIL           = 0x02;
    private static final int GET_VIDEO_LIST_SUCCESS = 0x03;
    private static final int GET_AD_LIST_SUCCESS    = 0x04;
    private static final int REQUEST_SUCCESS        = 0x05;
    private static final int GET_CATA_LIST_SUCCESS  = 0x06;


    private static final int GET_CATA_LIST_CODE = 0x10;
    private static final int GET_NEW_VIDEO_CODE = 0X11;
    private static final int GET_FAV_VIDEO_CODE = 0X12;
    private static final int GET_AD_lIST_CODE   = 0X13;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());

                    break;
                case GET_CATA_LIST_SUCCESS:
                    CataInfoListHandler mCataInfoListHandler = (CataInfoListHandler) msg.obj;
                    cataInfoList.clear();
                    cataInfoList.addAll(mCataInfoListHandler.getCataInfoList());

                    CategoryInfo mCataInfo = new CategoryInfo();
                    mCataInfo.setSelected(true);
                    mCataInfo.setId("0");
                    mCataInfo.setName("全部");
                    cataInfoList.add(0,mCataInfo);
                    mCataAdapter.notifyDataSetChanged();

                    if (!cataInfoList.isEmpty())
                    {
                        cta_id = cataInfoList.get(0).getId();
                        mHandler.sendEmptyMessage(GET_NEW_VIDEO_CODE);
                        mHandler.sendEmptyMessage(GET_FAV_VIDEO_CODE);
                    }

                    break;

                case GET_CATA_LIST_CODE:
                    getVideoCata();
                    break;

                case GET_NEW_LIVE_SUCCESS:
                    VideoInfoListHandler mVideoInfoListHandler = (VideoInfoListHandler) msg.obj;
                    newVideoList.clear();
                    newVideoList.addAll(mVideoInfoListHandler.getVideoInfoList());
                    mNewVideoAdapter.notifyDataSetChanged();
                    break;


                case GET_VIDEO_LIST_SUCCESS:
                    VideoInfoListHandler mVideoInfoListHandler1 = (VideoInfoListHandler) msg.obj;
                    favVideoList.clear();
                    favVideoList.addAll(mVideoInfoListHandler1.getVideoInfoList());
                    mFavVideoAdapter.notifyDataSetChanged();


                    break;


                case GET_NEW_VIDEO_CODE:
                    getNewLive();
                    break;

                case GET_FAV_VIDEO_CODE:
                    getVideoList();
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
                case GET_AD_lIST_CODE:
                    getAdList();
                    break;

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_video1, null);
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
        ivMore.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        rlAllFree.setOnClickListener(this);
        rlAllInteger.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCata.setLayoutManager(linearLayoutManager);

        mCataAdapter = new CategoryAdapter(cataInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                for (int i = 0; i < cataInfoList.size(); i++)
                {
                    if (i == position)
                    {
                        cataInfoList.get(position).setSelected(true);
                    }
                    else
                    {
                        cataInfoList.get(i).setSelected(false);
                    }
                }
                mCataAdapter.notifyDataSetChanged();

                cta_id = cataInfoList.get(position).getId();
                mHandler.sendEmptyMessage(GET_NEW_VIDEO_CODE);
                mHandler.sendEmptyMessage(GET_FAV_VIDEO_CODE);


            }
        });
        rvCata.setAdapter(mCataAdapter);


        mNewVideoAdapter = new IntegerAreaAdapter(newVideoList, getContext(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (MyApplication.getInstance().isLogin())
                {
                    Bundle b = new Bundle();
                    b.putSerializable("VideoInfo", newVideoList.get(position));
                    startActivity(new Intent(getActivity(), VideoPlayActivity.class).putExtras(b));

                }
                else
                {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

            }
        });

        rvFreeTime.setLayoutManager(new FullyGridLayoutManager(getActivity(), 2));
        // rvRecommend.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvFreeTime.setAdapter(mNewVideoAdapter);


        mFavVideoAdapter = new IntegerAreaAdapter(favVideoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Bundle b = new Bundle();
                b.putSerializable("VideoInfo", favVideoList.get(position));
                startActivity(new Intent(getActivity(), VideoPlayActivity.class).putExtras(b));
            }
        });
        rvIntegerArea.setLayoutManager(new FullyGridLayoutManager(getActivity(), 2));
        rvIntegerArea.setAdapter(mFavVideoAdapter);
        mHandler.sendEmptyMessage(GET_AD_lIST_CODE);
        loadData();

    }

    private void loadData()
    {
        mHandler.sendEmptyMessage(GET_CATA_LIST_CODE);

    }

    private void getVideoCata()
    {
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(getActivity(), Urls.getVideoCataUrl(), this, HttpRequest.POST, GET_CATA_LIST, valuePairs,
                new CataInfoListHandler());
    }

    private void getNewLive()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("sort", "new");
        valuePairs.put("pn", "1");
        valuePairs.put("num", "6");
        valuePairs.put("cta_id", cta_id);
        DataRequest.instance().request(getActivity(), Urls.getVideoListUrl(), this, HttpRequest.GET, GET_NEW_VIDEO, valuePairs,
        new VideoInfoListHandler());
}

    private void getVideoList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("sort", "fav");
        valuePairs.put("pn", "1");
        valuePairs.put("num", "6");
        valuePairs.put("cta_id", cta_id);
        DataRequest.instance().request(getActivity(), Urls.getVideoListUrl(), this, HttpRequest.GET, GET_FAV_VIDEO, valuePairs,
                new VideoInfoListHandler());
    }

    private void getAdList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pos_id", "1");
        DataRequest.instance().request(getActivity(), Urls.getAdListUrl(), this, HttpRequest.GET, GET_AD_LIST, valuePairs,
                new AdInfoListHandler());
    }

    private void initAd()
    {
        int width = APPUtils.getScreenWidth(getActivity());
        int height = (int) (width * 0.4);
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
                if (!TextUtils.isEmpty(mAdInfo.getLink()))
                {
                    if (mAdInfo.getLink().startsWith("video://"))
                    {
                        String id = mAdInfo.getLink().replace("video://", "");
                        VideoInfo mVideoInfo = new VideoInfo();
                        mVideoInfo.setId(id);
                        mVideoInfo.setV_name("点播");
                        Bundle b = new Bundle();
                        b.putSerializable("VideoInfo",mVideoInfo);
                        startActivity(new Intent(getActivity(), VideoPlayActivity.class).putExtras(b));
                    }
                    else if (mAdInfo.getLink().startsWith("live://"))
                    {
                        String id = mAdInfo.getLink().replace("live://", "");
                        startActivity(new Intent(getActivity(), LiveActivity.class).putExtra("biz_id",id));
                    }
                    else if (mAdInfo.getLink().startsWith("photo://"))
                    {
                        String id = mAdInfo.getLink().replace("photo://", "");
                        startActivity(new Intent(getActivity(), PhotoDetailActivity.class).putExtra("biz_id", id));
                    }
                }
            }
        });

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

        if (GET_CATA_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_CATA_LIST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_FAV_VIDEO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_VIDEO_LIST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_NEW_VIDEO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_NEW_LIVE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
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

    private CategoryPopupWindow mCataPopupWindow;

    @Override
    public void onClick(View v)
    {
        if (v == ivMore)
        {
            if (null == mCataPopupWindow)
            {
                mCataPopupWindow = new CategoryPopupWindow(getActivity(), cataInfoList, new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        for (int i = 0; i < cataInfoList.size(); i++)
                        {
                            if (i == position)
                            {
                                cataInfoList.get(position).setSelected(true);
                            }
                            else
                            {
                                cataInfoList.get(i).setSelected(false);
                            }
                        }
                        mCataAdapter.notifyDataSetChanged();
                        cta_id = cataInfoList.get(position).getId();
                        mHandler.sendEmptyMessage(GET_NEW_VIDEO_CODE);
                        mHandler.sendEmptyMessage(GET_FAV_VIDEO_CODE);
                    }
                });
            }

            if (!cataInfoList.isEmpty())
            {
                mCataPopupWindow.showAsDropDown(topView);
            }
        }
        else if(v == rlAllFree)
        {
            startActivity(new Intent(getActivity(), VidoeListActivity.class)
                    .putExtra("sort","new")
                    .putExtra("cta_id", cta_id)
            );
        }
        else if(v == rlAllInteger)
        {
            startActivity(new Intent(getActivity(), VidoeListActivity.class)
                    .putExtra("sort","fav")
                    .putExtra("cta_id", cta_id)
            );
        }
    }


    @Override
    public void onRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            loadData();
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
