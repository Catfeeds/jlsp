package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.adapter.SelfieAdapter;
import com.zb.wyd.entity.SelfieInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.SelfieInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.VerticalSwipeRefreshLayout;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 描述：一句话简单描述
 */
public class PhotoListActivity extends BaseActivity  implements IRequestListener, SwipeRefreshLayout.OnRefreshListener
{
    @BindView(R.id.iv_back)
    ImageView                  ivBack;
    @BindView(R.id.tv_title)
    TextView                   tvTitle;
    @BindView(R.id.rv_photo)
    RecyclerView               rvPhoto;
    @BindView(R.id.swipeRefresh)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;


    private List<SelfieInfo> selfieInfoList = new ArrayList<>();
    private SelfieAdapter mSelfieAdapter;

    private String cat_id;


    private              String photoTag              = "0";
    private              String sort                  = "new";
    private static final String GET_PHPTO_LIST        = "get_phpto_list";
    private static final int    REQUEST_SUCCESS       = 0x01;
    private static final int    REQUEST_FAIL          = 0x02;
    private static final int         GET_PHOTO_LIST_CODE = 0x11;
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler            = new BaseHandler(PhotoListActivity.this)
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
                    ToastUtil.show(PhotoListActivity.this, msg.obj.toString());
                    break;

                case GET_PHOTO_LIST_CODE:
                    getPhotoList();
                    break;
            }
        }
    };



    @Override
    protected void initData()
    {
        photoTag = getIntent().getStringExtra("cat_id");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_photo_list);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(PhotoListActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ivBack.setOnClickListener(this);
        rvPhoto.setOnScrollListener(new RecyclerView.OnScrollListener()
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
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }
    @Override
    protected void initViewData()
    {
        tvTitle.setText("图片列表");
        rvPhoto.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        mSelfieAdapter = new SelfieAdapter(selfieInfoList, PhotoListActivity.this, new MyItemClickListener()
//        {
//            @Override
//            public void onItemClick(View view, int position)
//            {
//                if (MyApplication.getInstance().isLogin())
//                {
//                    startActivity(new Intent(PhotoListActivity.this, PhotoDetailActivity.class).putExtra("biz_id", selfieInfoList.get(position).getId()));
//
//                }
//                else
//                {
//                    startActivity(new Intent(PhotoListActivity.this, LoginActivity.class));
//                }
//
//            }
//        });
        rvPhoto.setAdapter(mSelfieAdapter);


        //解决swipelayout与Recyclerview的冲突
        rvPhoto.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mHandler.sendEmptyMessage(GET_PHOTO_LIST_CODE);
    }
    private int pn = 1;
    private void getPhotoList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("tag", photoTag);
        valuePairs.put("sort", sort);
        valuePairs.put("num", "20");
        DataRequest.instance().request(this, Urls.getPhotoListUrl(), this, HttpRequest.POST, GET_PHPTO_LIST, valuePairs,
                new SelfieInfoListHandler());
    }
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if(v == ivBack)
        {
            finish();
        }
    }
    @Override
    public void onRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            pn = 1;
            selfieInfoList.clear();
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
    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
   if (GET_PHPTO_LIST.equals(action))
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
}
