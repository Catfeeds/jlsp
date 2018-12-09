package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.adapter.SearchAdapter;
import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.entity.SearchInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.SearchInfoListHandler;
import com.zb.wyd.json.VideoInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.list.refresh.PullToRefreshBase;
import com.zb.wyd.widget.list.refresh.PullToRefreshRecyclerView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

//搜索结果
public class SearchListActivity extends BaseActivity implements IRequestListener, PullToRefreshBase.OnRefreshListener<RecyclerView>
{
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_content)
    EditText etContent;
    private RecyclerView mRecyclerView; //
    private List<SearchInfo> mSearchInfoList = new ArrayList<>();

    private int pn = 1;
    private int mRefreshStatus;

    private SearchAdapter mSearchAdapter;

    private static final String GET_SEARCH_LIST = "get_search_list";
    private static final int GET_SEARCH_LIST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(SearchListActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_FAIL:
                    ToastUtil.show(SearchListActivity.this, msg.obj.toString());

                    break;

                case GET_SEARCH_LIST_SUCCESS:
                    SearchInfoListHandler mSearchInfoListHandler = (SearchInfoListHandler) msg.obj;
                    if (pn == 1)
                    {
                        mSearchInfoList.clear();
                    }
                    mSearchInfoList.addAll(mSearchInfoListHandler.getSearchInfoList());
                    mSearchAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_search_list);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(SearchListActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        ivBack.setOnClickListener(this);

        etContent.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String mContent = etContent.getText().toString();

                    if (!TextUtils.isEmpty(mContent)) loadData(mContent);
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    protected void initViewData()
    {
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchAdapter = new SearchAdapter(mSearchInfoList, SearchListActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                SearchInfo searchInfo = mSearchInfoList.get(position);


                if (!MyApplication.getInstance().isLogin())
                {
                    startActivity(new Intent(SearchListActivity.this, LoginActivity.class));
                    return;
                }
                switch (Integer.parseInt(searchInfo.getCo_biz()))
                {
                    //1直播，2电影，3自拍，5抖音，6小说，语音

                    case 1:
                        startActivity(new Intent(SearchListActivity.this, LiveActivity.class).putExtra("biz_id", searchInfo.getBiz_id()));
                        break;

                    case 2:
                        VideoInfo mVideoInfo = new VideoInfo();
                        mVideoInfo.setId(searchInfo.getBiz_id());
                        mVideoInfo.setV_name(searchInfo.getTitle());
                        Bundle b = new Bundle();
                        b.putSerializable("VideoInfo", mVideoInfo);
                        startActivity(new Intent(SearchListActivity.this, VideoPlayActivity.class).putExtras(b));
                        break;

                    case 3:
                        startActivity(new Intent(SearchListActivity.this, PhotoDetailActivity.class).putExtra("biz_id", searchInfo.getBiz_id()));
                        break;

                    case 5:
                        VideoInfo mDyVideoInfo = new VideoInfo();
                        mDyVideoInfo.setId(searchInfo.getBiz_id());
                        mDyVideoInfo.setV_name(searchInfo.getTitle());
                        Bundle mDyBundle = new Bundle();
                        mDyBundle.putSerializable("VideoInfo", mDyVideoInfo);
                        startActivity(new Intent(SearchListActivity.this, DyVideoActivity.class).putExtras(mDyBundle));
                        break;

                    case 6:
                    case 7:
                        String url = searchInfo.getUrl();

                        if (!TextUtils.isEmpty(url))
                        {
                            url = url.replace("pear://h5/", "");
                            startActivity(new Intent(SearchListActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "详情")
                                    .putExtra(WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, url));
                        }

                        break;

                }
            }
        });

        mRecyclerView.setAdapter(mSearchAdapter);

    }

    private String kw;

    private void loadData(String kw)
    {
        if (TextUtils.isEmpty(kw))
        {
            return;
        }
        this.kw = kw;
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("kw", kw);
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "20");
        DataRequest.instance().request(SearchListActivity.this, Urls.getDataSearchUrl(), this, HttpRequest.GET, GET_SEARCH_LIST, valuePairs, new
                SearchInfoListHandler());
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
        mSearchInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        loadData(kw);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        loadData(kw);
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
        if (GET_SEARCH_LIST.equals(action))
        {

            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_SEARCH_LIST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

}
