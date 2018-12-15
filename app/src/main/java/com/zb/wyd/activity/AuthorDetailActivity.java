package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.entity.FortuneInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.UserInfoHandler;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.StringUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//作者
public class AuthorDetailActivity extends BaseActivity implements IRequestListener
{

    @BindView(R.id.tv_vip_expire)
    TextView tVipExpire;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_logout)
    TextView tvLogout;


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_xufei)
    TextView tvXufei;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_user_location)
    TextView tvUserLocation;
    @BindView(R.id.ll_user_detail)
    LinearLayout llUserDetail;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_invitation_code)
    TextView tvInvitationCode;
    @BindView(R.id.ll_my_works)
    LinearLayout llMyWorks;
    @BindView(R.id.ll_dy)
    LinearLayout llDy;
    @BindView(R.id.ll_block_one)
    LinearLayout llBlockOne;
    @BindView(R.id.ll_collection)
    LinearLayout llCollection;
    @BindView(R.id.ll_share_friend)
    LinearLayout llShareFriend;
    @BindView(R.id.ll_block_two)
    LinearLayout llBlockTwo;
    @BindView(R.id.ll_yumin)
    LinearLayout llYumin;
    @BindView(R.id.ll_safe)
    LinearLayout llSafe;
    @BindView(R.id.ll_block_three)
    ImageView mHasMsgIv;
    @BindView(R.id.iv_has_msg)

    LinearLayout llBlockThree;
    private UserInfo userInfo;

    private String shareCnontent;

    private static final String GET_USER_DETAIL = "get_user_detail";
    private static final String GET_INVITATION_CODE = "get_invitation_code";
    private static final String GET_SHARE = "GET_SHARE";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int GET_INVITATION_SUCCESS = 0X03;
    private static final int GET_SHARE_SUCCESS = 0x12;
    private static final int GET_SHARE_CODE = 0x11;

    private static final int SHARE_PHOTO_REQUEST_CODE = 0x91;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_SHARE_CODE:
                    getShareUrl();
                    break;

                case GET_SHARE_SUCCESS:
                    ResultHandler mResultHandler = (ResultHandler) msg.obj;
                    shareCnontent = mResultHandler.getContent();

                    Intent intent1 = new Intent(Intent.ACTION_SEND);
                    intent1.putExtra(Intent.EXTRA_TEXT, shareCnontent);
                    intent1.setType("text/plain");
                    startActivityForResult(Intent.createChooser(intent1, "分享"), SHARE_PHOTO_REQUEST_CODE);
                    break;

                case GET_INVITATION_SUCCESS:
                    ResultHandler mResultHandler1 = (ResultHandler) msg.obj;
                    ToastUtil.show(AuthorDetailActivity.this, "邀请码申请成功");
                    tvInvitationCode.setText(mResultHandler1.getContent());
                    break;
                case REQUEST_SUCCESS:
                    UserInfoHandler mUserInfoHandler = (UserInfoHandler) msg.obj;
                    userInfo = mUserInfoHandler.getUserInfo();

                    if (null != userInfo)
                    {
                        String unick = userInfo.getUnick();
                        int vip_type = userInfo.getVip_type();
                        ImageLoader.getInstance().displayImage(ConfigManager.instance().getDomainName() + userInfo.getUface(), ivUserPic);
                        if ("-".equals(unick) || StringUtils.stringIsEmpty(unick))
                        {
                            tvUserName.setText(userInfo.getUname());
                        }
                        else
                        {
                            tvUserName.setText(unick);
                        }

                        if(userInfo.getMsgnum() ==0)
                        {
                            mHasMsgIv.setVisibility(View.GONE);
                        }
                        else
                        {
                            mHasMsgIv.setVisibility(View.VISIBLE);
                        }

                        if ("男".equals(userInfo.getSex()))
                        {
                            ivSex.setImageResource(R.drawable.ic_man);
                        }
                        else
                        {
                            ivSex.setImageResource(R.drawable.ic_woman);
                        }
                        tvUserLocation.setText(userInfo.getLocation());
                        tvInvitationCode.setText("@".equals(userInfo.getInvite())?"申请邀请码":userInfo.getInvite());

                        switch (vip_type)
                        {
                            //普通会员
                            case 0:
                                tvVip.setBackgroundResource(R.drawable.common_gray_45dp);
                                tvVip.setText("VIP");
                                tVipExpire.setVisibility(View.GONE);
                                tvXufei.setText("开通");
                                tvXufei.setVisibility(View.VISIBLE);
                                break;

                            case 1:
                                tvVip.setBackgroundResource(R.drawable.common_yellow_45dp);
                                tvVip.setText("VIP");
                                tVipExpire.setVisibility(View.VISIBLE);
                                tVipExpire.setText("到期日期:" + userInfo.getVip_expire());
                                tvXufei.setText("续费");
                                tvXufei.setVisibility(View.VISIBLE);

                                break;

                            case 2:
                                tvVip.setBackgroundResource(R.drawable.common_yellow_45dp);
                                tvVip.setText("SVIP");
                                tVipExpire.setVisibility(View.VISIBLE);
                                tVipExpire.setText("到期日期:" + userInfo.getVip_expire());
                                tvXufei.setText("续费");
                                tvXufei.setVisibility(View.VISIBLE);
                                break;
                        }


                    }
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(AuthorDetailActivity.this, msg.obj.toString());
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
        setContentView(R.layout.activity_author_detail);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(AuthorDetailActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
    }

    @Override
    protected void initViewData()
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (MyApplication.getInstance().isLogin())
        {
            tvLogin.setVisibility(View.GONE);
            tvLogout.setVisibility(View.VISIBLE);
            Map<String, String> valuePairs = new HashMap<>();
            DataRequest.instance().request(AuthorDetailActivity.this, Urls.getUserInfoUrl(), this, HttpRequest.GET, GET_USER_DETAIL, valuePairs,
                    new UserInfoHandler());
        }
        else
        {
            tvLogout.setVisibility(View.GONE);
            tvLogin.setVisibility(View.VISIBLE);
        }
    }
    private void getShareUrl()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("co_biz", "app");
        DataRequest.instance().request(AuthorDetailActivity.this, Urls.getShareApiUrl(), this, HttpRequest.GET, GET_SHARE, valuePairs, new ResultHandler());
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
        else if (GET_INVITATION_CODE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_INVITATION_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_SHARE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_SHARE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }

        }
    }

    @OnClick({R.id.iv_back, R.id.iv_edit, R.id.tv_xufei, R.id.tv_vip, R.id.ll_my_works, R.id.ll_dy, R.id.ll_block_one, R.id.ll_my_msg, R.id
            .ll_collection, R.id.ll_share_friend, R.id.ll_block_two, R.id.ll_yumin, R.id.ll_safe, R.id.tv_login, R.id.tv_logout, R.id
            .ll_block_three, R.id.ll_code})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_code:

                checkLogin();
                if ("申请邀请码".equals(tvInvitationCode.getText().toString()))
                {
                    showProgressDialog();
                    Map<String, String> valuePairs = new HashMap<>();
                    DataRequest.instance().request(AuthorDetailActivity.this, Urls.getInvitationCodeUrl(), this, HttpRequest.GET,
                            GET_INVITATION_CODE, valuePairs, new ResultHandler());
                }
                else
                {
                    ToastUtil.show(AuthorDetailActivity.this, "邀请码已复制到剪贴板");
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvInvitationCode.getText().toString());
                }


                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_edit:
                checkLogin();
                startActivity(new Intent(AuthorDetailActivity.this, UserDetailActivity.class));
                break;
            case R.id.tv_xufei:
                checkLogin();
                startActivity(new Intent(AuthorDetailActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "充值会员").putExtra
                        (WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, Urls.getPayUrl("vip")));
                break;
            case R.id.tv_vip:
                checkLogin();
                startActivity(new Intent(AuthorDetailActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "充值会员").putExtra
                        (WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, Urls.getPayUrl("vip")));
                break;
            case R.id.ll_my_works:
                checkLogin();
                startActivity(new Intent(AuthorDetailActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "我的作品").putExtra
                        (WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, Urls.getMyWorkUrl(AuthorDetailActivity.this)));
                break;
            case R.id.ll_dy:
                break;
            case R.id.ll_block_one:
                break;
            case R.id.ll_my_msg:
                checkLogin();
                startActivity(new Intent(AuthorDetailActivity.this, MyMessageListActivity.class));
                break;
            case R.id.ll_collection:
                checkLogin();
                startActivity(new Intent(AuthorDetailActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "我的收藏").putExtra
                        (WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, Urls.getMyFavourUrl(AuthorDetailActivity.this)));
                break;
            case R.id.ll_share_friend:
                mHandler.sendEmptyMessage(GET_SHARE_CODE);
                break;
            case R.id.ll_yumin:
                startActivity(new Intent(AuthorDetailActivity.this, DomainNameActivity.class));
                break;
            case R.id.ll_safe:
                startActivity(new Intent(AuthorDetailActivity.this, SafeSettingActivity.class));
                break;
            case R.id.ll_block_three:
                break;
            case R.id.tv_login:
                startActivity(new Intent(AuthorDetailActivity.this, LoginActivity.class));
                break;
            case R.id.tv_logout:

                ConfigManager.instance().setUniqueCode("");
                finish();

                break;
        }
    }


    private void checkLogin()
    {
        if (!MyApplication.getInstance().isLogin())
        {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DemoActivity", "requestCode=" + requestCode + " resultCode=" + resultCode);
        if ((int) (Math.random() * 100) <= 20) mHandler.sendEmptyMessage(GET_SHARE_CODE);

    }
}
