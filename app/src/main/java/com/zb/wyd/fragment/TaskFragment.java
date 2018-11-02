package com.zb.wyd.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.activity.AddPhotoActivity;
import com.zb.wyd.activity.BaseHandler;
import com.zb.wyd.activity.BindEmailActivity;
import com.zb.wyd.activity.MainActivity;
import com.zb.wyd.activity.RankingActivity;
import com.zb.wyd.activity.RegisterActivity;
import com.zb.wyd.activity.UserDetailActivity;
import com.zb.wyd.activity.WealthListActivity;
import com.zb.wyd.activity.WebViewActivity;
import com.zb.wyd.adapter.TaskAdapter;
import com.zb.wyd.entity.FortuneInfo;
import com.zb.wyd.entity.LocationInfo;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.PicInfo;
import com.zb.wyd.entity.SignInfo;
import com.zb.wyd.entity.TaskInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.LocationInfoHandler;
import com.zb.wyd.json.PhotoInfoHandler;
import com.zb.wyd.json.ResultHandler;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述：一句话简单描述
 */
public class TaskFragment extends BaseFragment implements IRequestListener, View.OnClickListener
{


    private View rootView = null;
    private Unbinder unbinder;


    @BindView(R.id.iv_detail)
    ImageView       ivDetail;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_user_fortune)
    TextView        tvUserFortune;
    @BindView(R.id.tv_signIn)
    TextView        tvSignIn;
    @BindView(R.id.rv_task_incomplete)
    MaxRecyclerView rvTaskIncomplete;
    @BindView(R.id.ll_task1)
    LinearLayout    llTask1;
    @BindView(R.id.rv_task_complete)
    MaxRecyclerView rvTaskComplete;

    @BindView(R.id.tv_take_cash)
    TextView tvTakeCash;

    @BindView(R.id.tv_balance)
    TextView tvBalance;

    private List<TaskInfo> mIncompleteList = new ArrayList<>();
    private List<TaskInfo> mCompleteList   = new ArrayList<>();


    private TaskAdapter mIncompleteAdapter;
    private TaskAdapter mCompleteAdapter;

    private static final String      GET_USER_DETAIL      = "get_user_detail";
    private static final String      USER_SIGN_REQUEST    = "user_sign_request";
    private static final String      GET_TASK_REQUEST     = "get_task_request";
    private static final int         REQUEST_SUCCESS      = 0x01;
    private static final int         REQUEST_FAIL         = 0x02;
    private static final int         USER_SIGN_SUCCESS    = 0x03;
    private static final int         GET_TASK_CODE        = 0x04;
    private static final int         GET_TASK_SUCCESS     = 0x05;
    private static final int         GET_USER_DETAIL_CODE = 0x06;
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler             = new BaseHandler(getActivity())
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

                        if (null != fortuneInfo)
                        {
                            tvBalance.setText(fortuneInfo.getGift());
                        }
                    }
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;

                case USER_SIGN_SUCCESS:
                    SignInfoHandler mSignInfoHandler = (SignInfoHandler) msg.obj;
                    SignInfo signInfo = mSignInfoHandler.getSignInfo();

                    if (null != signInfo)
                    {
                        String title = "签到成功";
                        String desc = signInfo.getVal() + "积分";
                        String task = "连续签到" + signInfo.getSeries() + "天";
                        DialogUtils.showTaskDialog(getActivity(), title, desc, task);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_task, null);
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

    private void getUserDetail()
    {
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(getActivity(), Urls.getUserInfoUrl(), this, HttpRequest.GET, GET_USER_DETAIL, valuePairs,
                new UserInfoHandler());
    }

    private void getTaskList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(getActivity(), Urls.getTaskUrl(), this, HttpRequest.GET, GET_TASK_REQUEST, valuePairs,
                new TaskInfoListHandler());
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
        tvSignIn.setOnClickListener(this);
        ivDetail.setOnClickListener(this);
        tvTakeCash.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        rvTaskIncomplete.setLayoutManager(new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mIncompleteAdapter = new TaskAdapter(mIncompleteList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                TaskInfo mTaskInfo = mIncompleteList.get(position);

                if ("user://register".equals(mTaskInfo.getAction()))
                {
                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                }
                else if ("user://email".equals(mTaskInfo.getAction()))
                {
                    startActivity(new Intent(getActivity(), BindEmailActivity.class));
                }
                else if ("user://profile".equals(mTaskInfo.getAction()))
                {
                    startActivity(new Intent(getActivity(), UserDetailActivity.class));
                }
                else if ("live://index".equals(mTaskInfo.getAction()))
                {
                    ((MainActivity) getActivity()).setTab(0);
                }
                else if (mTaskInfo.getAction().startsWith("video"))
                {
                    ((MainActivity) getActivity()).setTab(1);
                }
                else if ("photo://create".equals(mTaskInfo.getAction()))
                {
                    startActivity(new Intent(getActivity(), AddPhotoActivity.class));
                }

                else if (mTaskInfo.getAction().startsWith("http") || mTaskInfo.getAction().startsWith("https"))
                {
                    startActivity(new Intent(getActivity(), WebViewActivity.class)
                            .putExtra(WebViewActivity.EXTRA_TITLE, mTaskInfo.getTname())
                            .putExtra(WebViewActivity.IS_SETTITLE, true)
                            .putExtra(WebViewActivity.EXTRA_URL, mTaskInfo.getAction())
                    );
                }


                //                        Intent textIntent2 = new Intent(Intent.ACTION_SEND);
                //                        textIntent2.setType("text/plain");
                //                        textIntent2.putExtra(Intent.EXTRA_TEXT, "这是一段分享的文字");
                //                        startActivity(Intent.createChooser(textIntent2, "分享"));

            }
        });
        rvTaskIncomplete.setAdapter(mIncompleteAdapter);


        rvTaskComplete.setLayoutManager(new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mCompleteAdapter = new TaskAdapter(mCompleteList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }
        });

        rvTaskComplete.setAdapter(mCompleteAdapter);
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
    public void onClick(View v)
    {
        if (v == tvSignIn)
        {
            showProgressDialog(getActivity());
            Map<String, String> valuePairs = new HashMap<>();
            DataRequest.instance().request(getActivity(), Urls.getUserSignUrl(), this, HttpRequest.POST, USER_SIGN_REQUEST, valuePairs,
                    new SignInfoHandler());
        }
        else if (v == ivDetail)
        {
            startActivity(new Intent(getActivity(), RankingActivity.class));
        }
        else if (v == tvTakeCash)
        {
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "积分取现").putExtra(WebViewActivity
                    .IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, Urls.getWithdrawUrl()));
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());
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
