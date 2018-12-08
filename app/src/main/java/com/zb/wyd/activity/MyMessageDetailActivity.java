package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.adapter.MyChatChatAdapter;
import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.ChatInfoListHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.DividerDecoration;
import com.zb.wyd.widget.list.refresh.PullToRefreshBase;
import com.zb.wyd.widget.list.refresh.PullToRefreshRecyclerView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 */
public class MyMessageDetailActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<RecyclerView>, View.OnClickListener,
        IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView mBackIv;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @BindView(R.id.et_content)
    EditText etContent;


    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private RecyclerView mRecyclerView; //

    private MyChatChatAdapter mSystemChatAdapter;
    private List<ChatInfo> mChatInfoList = new ArrayList<>();

    private int pn = 1;
    private int mRefreshStatus;

    private static final String GET_MESSAGE_LIST = "get_message_list";
    private static final String GET_NEW_MESSAGE_LIST = "get_new_message_list";
    private static final String SEND_MSG = "send_msg";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int GET_NEW_MESSAGE_SUCCESS = 0x03;
    private static final int SEND_MSG_SUCCESS = 0x04;
    private static final int GET_NEW_MESSAGE = 0x05;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ChatInfoListHandler mChatInfoListHandler = (ChatInfoListHandler) msg.obj;
                    mChatInfoList.addAll(mChatInfoListHandler.getChatInfoList());
                    mSystemChatAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mChatInfoList.size() - 1);
                    break;

                case GET_NEW_MESSAGE_SUCCESS:

                    ChatInfoListHandler mChatInfoListHandler1 = (ChatInfoListHandler) msg.obj;
                    mChatInfoList.addAll(mChatInfoListHandler1.getChatInfoList());
                    mSystemChatAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mChatInfoList.size() - 1);

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(MyMessageDetailActivity.this, msg.obj.toString());

                    break;

                case SEND_MSG_SUCCESS:
                    getNewMessageList();
                    break;

                case GET_NEW_MESSAGE:
                    getNewMessageList();
                    mHandler.sendEmptyMessageDelayed(GET_NEW_MESSAGE, 30 * 1000);

                    break;

            }
        }
    };

    private String roomid;

    @Override
    protected void initData()
    {
        roomid = getIntent().getStringExtra("ROOM");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_my_message_detail);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(MyMessageDetailActivity.this, false);
    }


    @Override
    protected void initEvent()
    {
        mBackIv.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        etContent.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                    String msg = etContent.getText().toString();

                    if (!TextUtils.isEmpty(msg))
                    {
                        sendMsg(msg);
                        etContent.setText("");

                    }
                    return true;
                }
                return false;
            }

        });

    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("聊天");
        tvSubmit.setVisibility(View.VISIBLE);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSystemChatAdapter = new MyChatChatAdapter(mChatInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }
        });
        mRecyclerView.setAdapter(mSystemChatAdapter);
        getMessageList();
        mHandler.sendEmptyMessageDelayed(GET_NEW_MESSAGE, 30 * 1000);
    }


    private void getMessageList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "50");
        valuePairs.put("friend", roomid);
        DataRequest.instance().request(MyMessageDetailActivity.this, Urls.getfriendMessageUrl(), this, HttpRequest.GET, GET_MESSAGE_LIST,
                valuePairs, new ChatInfoListHandler());
    }


    private void sendMsg(String content)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("content", content);
        DataRequest.instance().request(MyMessageDetailActivity.this, Urls.getSendMsgUrl(this, roomid), this, HttpRequest.POST, SEND_MSG,
                valuePairs, new ResultHandler());
    }


    private void getNewMessageList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "50");
        valuePairs.put("friend", roomid);
        DataRequest.instance().request(MyMessageDetailActivity.this, Urls.getNewMessageUrl(), this, HttpRequest.GET, GET_NEW_MESSAGE_LIST,
                valuePairs, new ChatInfoListHandler());
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        mChatInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        // getMessageList();
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        //  getMessageList();
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }
    }


    @Override
    public void onClick(View v)
    {
        if (v == mBackIv)
        {
            finish();
        }
        else if (v == tvSubmit)
        {
            // startActivity(new Intent(MyMessageDetailActivity.this, QuestionActivity.class));
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

        if (GET_MESSAGE_LIST.equals(action))
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

        else if (GET_NEW_MESSAGE_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_NEW_MESSAGE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (SEND_MSG.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(SEND_MSG_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

}
