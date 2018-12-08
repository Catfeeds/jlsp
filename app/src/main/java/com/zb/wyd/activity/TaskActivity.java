package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.adapter.TaskAdapter;
import com.zb.wyd.entity.FortuneInfo;
import com.zb.wyd.entity.SignInfo;
import com.zb.wyd.entity.TaskInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.SignInfoHandler;
import com.zb.wyd.json.TaskInfoListHandler;
import com.zb.wyd.json.UserInfoHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.FullyLinearLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TaskActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_user_fortune)
    TextView tvUserFortune;
    @BindView(R.id.tv_signIn)
    TextView tvSignIn;
    @BindView(R.id.rv_task_incomplete)
    MaxRecyclerView rvTaskIncomplete;
    @BindView(R.id.ll_task1)
    LinearLayout llTask1;
    @BindView(R.id.rv_task_complete)
    MaxRecyclerView rvTaskComplete;

    @BindView(R.id.tv_balance)
    TextView tvBalance;

    private List<TaskInfo> mIncompleteList = new ArrayList<>();
    private List<TaskInfo> mCompleteList = new ArrayList<>();


    private TaskAdapter mIncompleteAdapter;
    private TaskAdapter mCompleteAdapter;

    private static final String GET_USER_DETAIL = "get_user_detail";
    private static final String USER_SIGN_REQUEST = "user_sign_request";
    private static final String GET_TASK_REQUEST = "get_task_request";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int USER_SIGN_SUCCESS = 0x03;
    private static final int GET_TASK_CODE = 0x04;
    private static final int GET_TASK_SUCCESS = 0x05;
    private static final int GET_USER_DETAIL_CODE = 0x06;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(TaskActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:

                    UserInfoHandler mUserInfoHandler = (UserInfoHandler) msg.obj;
                    UserInfo userInfo = mUserInfoHandler.getUserInfo();

                    if (null != userInfo)
                    {
                      //  ImageLoader.getInstance().displayImage(userInfo.getUface(), ivUserPic);
                        tvUserFortune.setText(userInfo.getTotal_score());

                        FortuneInfo fortuneInfo = userInfo.getFortuneInfo();

                        if(null !=fortuneInfo)
                        {
                            tvBalance.setText(fortuneInfo.getGift());
                        }
                    }
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(TaskActivity.this, msg.obj.toString());
                    break;

                case USER_SIGN_SUCCESS:
                    SignInfoHandler mSignInfoHandler = (SignInfoHandler) msg.obj;
                    SignInfo signInfo = mSignInfoHandler.getSignInfo();

                    if (null != signInfo)
                    {
                        String title = "签到成功";
                        String desc = signInfo.getVal() + "积分";
                        String task = "连续签到" + signInfo.getSeries() + "天";
                        DialogUtils.showTaskDialog(TaskActivity.this, title, desc, task);
                        tvSignIn.setText("已签到");
                        tvSignIn.setEnabled(false);
                        getUserDetail();
                    }

                    break;
                case GET_TASK_CODE:
                    getTaskList();
                    break;

                case GET_TASK_SUCCESS:
                    TaskInfoListHandler mTaskInfoListHandler = (TaskInfoListHandler) msg.obj;

                    TaskInfo mTaskInfo = mTaskInfoListHandler.getTaskInfo();

//                    if ("1".equals(mTaskInfo.getHas_finish()))
//                    {
//                        tvSignIn.setText("已签到");
//                        tvSignIn.setEnabled(false);
//
//                    }

                    mIncompleteList.clear();
                    mCompleteList.clear();

                    mIncompleteList.addAll(mTaskInfoListHandler.getIncompleteList());
                    mCompleteList.addAll(mTaskInfoListHandler.getCompleteList());
                    mIncompleteAdapter.notifyDataSetChanged();
                    mCompleteAdapter.notifyDataSetChanged();


                    break;

                case GET_USER_DETAIL_CODE:
                    getUserDetail();
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
        setContentView(R.layout.activity_task);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(TaskActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
        ivDetail.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        rvTaskIncomplete.setLayoutManager(new FullyLinearLayoutManager(TaskActivity.this, LinearLayoutManager.VERTICAL, false));
        mIncompleteAdapter = new TaskAdapter(mIncompleteList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                TaskInfo mTaskInfo = mIncompleteList.get(position);

                if ("user://register".equals(mTaskInfo.getAction()))
                {
                    startActivity(new Intent(TaskActivity.this, RegisterActivity.class));
                }
                else if ("user://email".equals(mTaskInfo.getAction()))
                {
                    startActivity(new Intent(TaskActivity.this, BindEmailActivity.class));
                }
                else if ("user://profile".equals(mTaskInfo.getAction()))
                {
                    startActivity(new Intent(TaskActivity.this, UserDetailActivity.class));
                }
                else if ("live://index".equals(mTaskInfo.getAction()))
                {
                    sendBroadcast(new Intent(MainActivity.TAB_LIVE));
                    finish();
                }
                else if (mTaskInfo.getAction().startsWith("video"))
                {
                    sendBroadcast(new Intent(MainActivity.TAB_VIDEO));
                    finish();
                }
                else if ("photo://create".equals(mTaskInfo.getAction()))
                {
                    if(MyApplication.getInstance().isLogin())
                    {
                        startActivity(new Intent(TaskActivity.this, AddPhotoActivity.class));
                    }
                    else
                    {
                        startActivity(new Intent(TaskActivity.this, LoginActivity.class));
                    }
                }

                else if (mTaskInfo.getAction().startsWith("http") || mTaskInfo.getAction().startsWith("https"))
                {
                    startActivity(new Intent(TaskActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, mTaskInfo.getTname()).putExtra(WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, mTaskInfo.getAction()));
                }


                //                        Intent textIntent2 = new Intent(Intent.ACTION_SEND);
                //                        textIntent2.setType("text/plain");
                //                        textIntent2.putExtra(Intent.EXTRA_TEXT, "这是一段分享的文字");
                //                        startActivity(Intent.createChooser(textIntent2, "分享"));

            }
        });
        rvTaskIncomplete.setAdapter(mIncompleteAdapter);


        rvTaskComplete.setLayoutManager(new FullyLinearLayoutManager(TaskActivity.this, LinearLayoutManager.VERTICAL, false));
        mCompleteAdapter = new TaskAdapter(mCompleteList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }
        });

        rvTaskComplete.setAdapter(mCompleteAdapter);
    }

    private void getUserDetail()
    {
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(TaskActivity.this, Urls.getUserInfoUrl(), this, HttpRequest.GET, GET_USER_DETAIL, valuePairs, new UserInfoHandler());
    }

    private void getTaskList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(TaskActivity.this, Urls.getTaskUrl(), this, HttpRequest.GET, GET_TASK_REQUEST, valuePairs, new TaskInfoListHandler());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (MyApplication.getInstance().isLogin())
        {
            mHandler.sendEmptyMessage(GET_USER_DETAIL_CODE);
            mHandler.sendEmptyMessage(GET_TASK_CODE);
        }

    }

    @Override
    public void onClick(View v)
    {
        if (v == tvSignIn)
        {
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            DataRequest.instance().request(TaskActivity.this, Urls.getUserSignUrl(), this, HttpRequest.POST, USER_SIGN_REQUEST, valuePairs, new SignInfoHandler());
        }
        else if (v == ivDetail)
        {
            startActivity(new Intent(TaskActivity.this, RankingActivity.class));
        }
        else if (v == ivBack)
        {
            finish();
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_USER_DETAIL.equals(action))
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
        else if (USER_SIGN_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(USER_SIGN_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_TASK_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_TASK_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
