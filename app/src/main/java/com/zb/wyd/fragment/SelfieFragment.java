package com.zb.wyd.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donkingliang.banner.CustomBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.activity.AddPhotoActivity;
import com.zb.wyd.activity.BaseHandler;
import com.zb.wyd.activity.LiveActivity;
import com.zb.wyd.activity.LoginActivity;
import com.zb.wyd.activity.PhotoDetailActivity;
import com.zb.wyd.activity.TaskActivity;
import com.zb.wyd.activity.VideoPlayActivity;
import com.zb.wyd.activity.WebViewActivity;
import com.zb.wyd.adapter.CategoryAdapter;
import com.zb.wyd.adapter.SelfieAdapter;
import com.zb.wyd.entity.AdInfo;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.entity.NoticeInfo;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.SelfieInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.AdInfoListHandler;
import com.zb.wyd.json.CataInfoListHandler;
import com.zb.wyd.json.PhotoInfoListHandler;
import com.zb.wyd.json.SelfieInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CategoryPopupWindow;
import com.zb.wyd.widget.MarqueeTextView;
import com.zb.wyd.widget.PhotoSortPopupWindow;
import com.zb.wyd.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextViewUtil;

/**
 * 描述：自拍
 */
public class SelfieFragment extends BaseFragment implements IRequestListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener
{

    @BindView(R.id.banner)
    CustomBanner mBanner;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.rl_category)
    View mCategoryLayout;

    @BindView(R.id.tv_category_name)
    TextView tvCategoryName;
    @BindView(R.id.iv_opened)
    ImageView ivOpened;


    @BindView(R.id.rl_sort)
    View mSortLayout;
    @BindView(R.id.tv_sort_name)
    TextView tvSortName;
    @BindView(R.id.iv_sort_opened)
    ImageView ivSortOpened;


    @BindView(R.id.swipeRefresh)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    private List<CategoryInfo> cataInfoList = new ArrayList<>();

    private int getPhotoCount;
    private List<PhotoInfo> selfieInfoList = new ArrayList<>();
    private SelfieAdapter mSelfieAdapter;

    private List<String> picList = new ArrayList<>();
    private List<AdInfo> adInfoList = new ArrayList<>();

    private View rootView = null;
    private Unbinder unbinder;
    private int pn = 1;

    private String photoTag = "";
    private String sort = "new";
    private static final String GET_AD_LIST = "get_ad_list";
    private static final String GET_CATA_LIST = "get_cata_list";
    private static final String GET_PHPTO_LIST = "get_phpto_list";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int GET_CATA_LIST_SUCCESS = 0x04;
    private static final int GET_AD_LIST_SUCCESS = 0x03;

    private static final int GET_CATA_LIST_CODE = 0x10;
    private static final int GET_PHOTO_LIST_CODE = 0x11;
    private static final int GET_AD_LIST_CODE = 0X12;
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
                    PhotoInfoListHandler mPhotoInfoListHandler = (PhotoInfoListHandler) msg.obj;
                    if (pn == 1)
                    {
                        selfieInfoList.clear();
                        if ("new".equals(sort))
                        {
                            rvPhoto.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                        else
                        {
                            rvPhoto.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        }
                    }
                    selfieInfoList.addAll(mPhotoInfoListHandler.getPhotoInfoList());

                    mSelfieAdapter.notifyDataSetChanged();

                    break;

                case REQUEST_FAIL:

                    getPhotoCount++;
                    if (getPhotoCount <= 30)
                    {
                        if (pn == 1)
                        {
                            getPhotoList();
                        }

                    }
                    //ToastUtil.show(getActivity(), msg.obj.toString());

                    break;
                case GET_CATA_LIST_SUCCESS:
                    CataInfoListHandler mCataInfoListHandler = (CataInfoListHandler) msg.obj;
                    cataInfoList.clear();
                    cataInfoList.addAll(mCataInfoListHandler.getCataInfoList());


                    CategoryInfo mCataInfo = new CategoryInfo();
                    mCataInfo.setSelected(true);
                    mCataInfo.setId("0");
                    mCataInfo.setName("全部");
                    cataInfoList.add(0, mCataInfo);
                    if (!cataInfoList.isEmpty())
                    {
                        photoTag = cataInfoList.get(0).getName();
                        cataInfoList.get(0).setSelected(true);
                    }


                    break;

                case GET_CATA_LIST_CODE:
                    getPhotoCata();
                    break;

                case GET_PHOTO_LIST_CODE:
                    getPhotoList();
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
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_selfie, null);
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
        mCategoryLayout.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        mSortLayout.setOnClickListener(this);
        rvPhoto.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                RecyclerView.LayoutManager manager;
                if ("new".equals(sort))
                {
                    manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                }
                else
                {
                    manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                }

                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    //获取最后一个完全显示的ItemPosition
                    int[] lastVisiblePositions = null;
                    if ("new".equals(sort))
                    {
                        lastVisiblePositions = new int[]{((LinearLayoutManager) manager).findLastVisibleItemPosition()};
                    }
                    else
                    {
                        lastVisiblePositions = ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(new int[(
                                (StaggeredGridLayoutManager) manager).getSpanCount()]);
                    }

                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部
                    if (lastVisiblePos == (totalItemCount - 1) && isSlidingToLast)
                    {
                        //加载更多功能的代码
                        //                        Ln.e("howes right="+manager
                        // .findLastCompletelyVisibleItemPosition());
                        //                        Toast.makeText(getActivityContext(),"加载更多",0)
                        // .show();

                        pn += 1;
                        getPhotoList();


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
            if (arr[i] > maxVal) maxVal = arr[i];
        }
        return maxVal;
    }

    @Override
    protected void initViewData()
    {

        rvPhoto.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSelfieAdapter = new SelfieAdapter(selfieInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (MyApplication.getInstance().isLogin())
                {
                    if (null != selfieInfoList && position < selfieInfoList.size())
                        startActivity(new Intent(getActivity(), PhotoDetailActivity.class).putExtra("biz_id", selfieInfoList.get(position).getId()));

                }
                else
                {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

            }
        });
        rvPhoto.setAdapter(mSelfieAdapter);


        //解决swipelayout与Recyclerview的冲突
        rvPhoto.addOnScrollListener(new RecyclerView.OnScrollListener()
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
        mHandler.sendEmptyMessage(GET_AD_LIST_CODE);
        mHandler.sendEmptyMessage(GET_CATA_LIST_CODE);
        mHandler.sendEmptyMessage(GET_PHOTO_LIST_CODE);

    }


    private void getPhotoCata()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", "1");
        valuePairs.put("num", "15");
        valuePairs.put("co_biz", "photo");
        DataRequest.instance().request(getActivity(), Urls.getTagsUrl(), this, HttpRequest.GET, GET_CATA_LIST, valuePairs, new CataInfoListHandler());
    }


    private void getPhotoList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("tag", photoTag);
        valuePairs.put("sort", sort);
        valuePairs.put("num", "20");
        DataRequest.instance().request(getActivity(), Urls.getPhotoListUrl(), this, HttpRequest.GET, GET_PHPTO_LIST, valuePairs, new
                PhotoInfoListHandler());
    }


    private void getAdList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pos_id", "3");
        DataRequest.instance().request(getActivity(), Urls.getAdListUrl(), this, HttpRequest.GET, GET_AD_LIST, valuePairs, new AdInfoListHandler());
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
                adClick(mAdInfo.getLink());

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
        mHandler.removeCallbacksAndMessages(null);
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
                //  mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_PHPTO_LIST.equals(action))
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
        else if (GET_AD_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_AD_LIST_SUCCESS, obj));
            }
            else
            {
                //  mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    private CategoryPopupWindow mCataPopupWindow;
    private PhotoSortPopupWindow mPhotoSortPopupWindow;

    @Override
    public void onClick(View v)
    {
        if (v == mCategoryLayout)
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

                        photoTag = cataInfoList.get(position).getName();
                        tvCategoryName.setText(photoTag);
                        pn = 1;
                        selfieInfoList.clear();
                        mHandler.sendEmptyMessage(GET_PHOTO_LIST_CODE);
                    }
                });
            }
            if (!mCataPopupWindow.isShowing())
            {

                mCataPopupWindow.showAsDropDown(mCategoryLayout);
                ivOpened.setImageResource(R.drawable.ic_arrow_up);
            }
            else
            {
                mCataPopupWindow.dismiss();
                ivOpened.setImageResource(R.drawable.ic_arrow_down);
            }


        }
        else if (v == mSortLayout)
        {
            if (null == mPhotoSortPopupWindow)
            {
                mPhotoSortPopupWindow = new PhotoSortPopupWindow(getActivity(), new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        mPhotoSortPopupWindow.dismiss();
                        switch (position)
                        {
                            case 0:
                                sort = "new";
                                tvSortName.setText("最新发布");
                                break;
                            case 1:
                                sort = "fav";
                                tvSortName.setText("最多收藏");
                                break;
                            case 2:
                                sort = "score";
                                tvSortName.setText("评分最高");
                                break;
                        }
                        pn = 1;
                        mHandler.sendEmptyMessage(GET_PHOTO_LIST_CODE);
                    }
                });
            }


            if (mPhotoSortPopupWindow.isShowing())
            {
                mPhotoSortPopupWindow.dismiss();
                ivSortOpened.setImageResource(R.drawable.ic_arrow_up);
            }
            else
            {
                mPhotoSortPopupWindow.showAsDropDown(mSortLayout);
                ivSortOpened.setImageResource(R.drawable.ic_arrow_up);
            }
        }
        else if (v == ivAdd)
        {

            if (MyApplication.getInstance().isLogin())
            {
                startActivity(new Intent(getActivity(), AddPhotoActivity.class));
            }
            else
            {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }
    }


    @Override
    public void onRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            pn = 1;
            mHandler.sendEmptyMessage(GET_AD_LIST_CODE);
            mHandler.sendEmptyMessage(GET_CATA_LIST_CODE);
            mHandler.sendEmptyMessage(GET_PHOTO_LIST_CODE);
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
}
