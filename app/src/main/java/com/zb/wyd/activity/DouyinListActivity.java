package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.adapter.DyVideoAdapter;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.VideoInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.list.refresh.PullToRefreshBase;
import com.zb.wyd.widget.list.refresh.PullToRefreshRecyclerView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class DouyinListActivity extends BaseActivity implements IRequestListener, PullToRefreshBase
        .OnRefreshListener<RecyclerView>
{
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private RecyclerView mRecyclerView; //
    private List<VideoInfo> mVideoInfoList = new ArrayList<>();

    private int pn = 1;
    private int mRefreshStatus;

    private DyVideoAdapter mVideoAdapter;

    private static final String GET_DY_LIST = "get_douyin_list";
    private static final int GET_VIDEO_LIST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(DouyinListActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_FAIL:
                    ToastUtil.show(DouyinListActivity.this, msg.obj.toString());

                    break;

                case GET_VIDEO_LIST_SUCCESS:
                    VideoInfoListHandler mVideoInfoListHandler = (VideoInfoListHandler) msg.obj;
                    if (pn == 1)
                    {
                        mVideoInfoList.clear();
                    }
                    mVideoInfoList.addAll(mVideoInfoListHandler.getVideoInfoList());
                    mVideoAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_douyin_list);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(DouyinListActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("发布的抖音");
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(DouyinListActivity.this, 2));

        mVideoAdapter = new DyVideoAdapter(mVideoInfoList, DouyinListActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (MyApplication.getInstance().isLogin())
                {
                    if (mVideoInfoList.size() > position)
                    {
                        Bundle b = new Bundle();
                        b.putSerializable("VideoInfo", mVideoInfoList.get(position));
                        startActivity(new Intent(DouyinListActivity.this, DyVideoActivity.class).putExtras(b));
                    }
                }
                else
                {
                    startActivity(new Intent(DouyinListActivity.this, LoginActivity.class));
                }

            }
        });

        mRecyclerView.setAdapter(mVideoAdapter);
        loadData();
    }

    private void loadData()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "20");
        DataRequest.instance().request(DouyinListActivity.this, Urls.getDouyinListUrl(), this, HttpRequest.GET, GET_DY_LIST, valuePairs, new
                VideoInfoListHandler());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        mVideoInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
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
        if (GET_DY_LIST.equals(action))
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
    }
}
