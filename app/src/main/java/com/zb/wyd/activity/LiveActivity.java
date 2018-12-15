package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.zb.wyd.R;
import com.zb.wyd.adapter.ChatAdapter;
import com.zb.wyd.adapter.GiftAdapter;
import com.zb.wyd.adapter.OnlineAdapter;
import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.entity.FortuneInfo;
import com.zb.wyd.entity.GiftInfo;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.PriceInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.LiveInfoHandler;
import com.zb.wyd.json.LivePriceInfoHandler;
import com.zb.wyd.json.OnlinerListHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.UserInfoHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.listener.MyOnClickListener;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.StringUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.LiveVideoPlayer;
import com.zb.wyd.widget.gift.AnimMessage;
import com.zb.wyd.widget.gift.LPAnimationManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * 描述：一句话简单描述
 */
public class LiveActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.detail_player)
    LiveVideoPlayer videoPlayer;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_favour_count)
    TextView tvFavourCount;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.rv_online)
    RecyclerView rvOnline;

    @BindView(R.id.rl_content)
    RelativeLayout mContentLayout;
    @BindView(R.id.iv_closed)
    ImageView ivClosed;
    @BindView(R.id.tv_dm)
    TextView tvDm;
    @BindView(R.id.iv_report)
    ImageView ivReport;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.et_say)
    EditText etSay;

    @BindView(R.id.tv_location)
    TextView tvLocation;


    @BindView(R.id.rl_live_bottom)
    RelativeLayout mLiveBottomLayout;
    @BindView(R.id.ll_gift_container)
    LinearLayout llGiftContainer;

    @BindView(R.id.rv_system)
    RecyclerView mSystemRecyclerView;
    @BindView(R.id.rv_chat)
    RecyclerView mChatRecyclerView;


    private String biz_id, location;
    private long startTime, endTime;
    private String playUrl;

    private String has_favorite;
    private boolean dmShow;
    private List<UserInfo> onlineList = new ArrayList<>();
    private OnlineAdapter mOnlineAdapter;
    private List<GiftInfo> giftInfoList = new ArrayList<>();

    private List<ChatInfo> mSystemChatInfoList = new ArrayList<>();
    private List<ChatInfo> mChatInfoList = new ArrayList<>();

    private ChatAdapter mSystemChatAdapter;
    private ChatAdapter mChatAdapter;
    private int mCashCount;//砖石
    private int mGiftCount;//积分点
    private int mGiftPrice;

    private static final String GET_USER_DETAIL = "get_user_detail";
    private static final String GET_ANCHOR_INFO = "get_anchor_info";
    private static final String GET_LIVE_PRICE = "get_live_price";
    private static final String GET_LIVE_STREAM = "get_live_stream";
    private static final String GET_ONLINER = "get_onliner";
    private static final String BUY_LIVE = "buy_live";
    private static final String FAVORITE_LIKE = "favorite_like";
    private final String UN_FAVORITE_LIKE = "un_favorite_like";
    private final String GET_FORTUNE_GIFT = "get_fortune_gift";
    private final String FORTUNE_BUY = "fortune_buy";


    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int GET_LIVE_PRICE_SUCCESS = 0x03;
    private static final int BUY_LIVE_SUCCESS = 0x05;
    private static final int SET_STATISTICS = 0x06;
    private static final int GET_ONLINER_SUCCESS = 0x07;
    private static final int GET_ONLINER_REQUEST = 0x08;
    private static final int GET_STREAM_REQUEST = 0x09;
    private static final int GET_ANCHOR_REQUEST = 0x10;
    private static final int FAVORITE_LIKE_SUCCESS = 0x11;
    private static final int UN_FAVORITE_LIKE_SUCCESS = 0x15;
    private static final int FAVORITE_LIKE_FAIL = 0x14;
    private static final int GET_ANCHOR_SUCCESS = 0x12;
    //    private static final int SHOW_SYSTEM_TV = 0x13;
    private static final int GET_FORTUNE_GIFT_SUCCESS = 0x16;
    private static final int FORTUNE_BUY_SUCCESS = 0x17;
    private static final int GET_FORTUNE_GIFT_FAIL = 0x18;
    private static final int FORTUNE_BUY_SUCCESS_FAIL = 0x19;
    private static final int WEBSOCKET_CONNECT = 0x20;
    private static final int SHOW_TOAST = 0X21;
    private static final int GET_USER_DETAIL_REQUEST = 0X022;
    private static final int GET_USER_DETAIL_SUCCESS = 0X023;

    @SuppressLint("HandlerLeak")
    private NoLeakHandler mHandler = new NoLeakHandler(LiveActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    LiveInfoHandler mLiveInfoHandler = (LiveInfoHandler) msg.obj;
                    LiveInfo mLiveInfo = mLiveInfoHandler.getLiveInfo();
                    if (null != mLiveInfo)
                    {

                        playUrl = mLiveInfo.getUri();
                        LogUtil.e("TAG", playUrl);
                        videoPlayer.setUp(playUrl, false, "");
                        videoPlayer.startPlayLogic();
                        mHandler.sendEmptyMessageDelayed(WEBSOCKET_CONNECT, 1 * 1000);
                        mHandler.sendEmptyMessage(GET_USER_DETAIL_REQUEST);
                    }
                    break;


                case REQUEST_FAIL:
                    DialogUtils.showPromptDialog(LiveActivity.this, msg.obj.toString(), new MyItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {
                            finish();
                        }
                    });
                    break;

                case GET_LIVE_PRICE_SUCCESS:
                    LivePriceInfoHandler mLivePriceInfoHandler = (LivePriceInfoHandler) msg.obj;

                    final PriceInfo mLivePriceInfo = mLivePriceInfoHandler.getLivePriceInfo();

                    if (null != mLivePriceInfo)
                    {
                        DialogUtils.showVideoPriceDialog1(LiveActivity.this, mLivePriceInfo, new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                finish();
                            }
                        }, new MyOnClickListener.OnSubmitListener()
                        {
                            @Override
                            public void onSubmit(String content)
                            {
                                if ("1".equals(content))//兑换
                                {

                                    buyLive(mLivePriceInfo.getFinger());


                                }
                                //购买VIP
                                else if ("3".equals(content))
                                {
                                    startActivity(new Intent(LiveActivity.this, MemberActivity.class));
                                }
                                else//去做任务
                                {

                                    // sendBroadcast(new Intent(MainActivity.TAB_TASK));
                                    //                                    startActivity(new Intent(LiveActivity.this, TaskActivity.class));
                                    startActivity(new Intent(LiveActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE,
                                            "充值会员").putExtra(WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, Urls
                                            .getTaskIndexUrl()));
                                    finish();
                                }
                            }
                        }).show();
                    }
                    break;

                case BUY_LIVE_SUCCESS:
                    getLiveStream();
                    break;

                case SET_STATISTICS:
                    setStatistics(0);
                    break;

                case GET_ONLINER_SUCCESS:
                    OnlinerListHandler mOnlinerListHandler = (OnlinerListHandler) msg.obj;
                    onlineList.clear();
                    onlineList.addAll(mOnlinerListHandler.getUserInfoList());
                    mOnlineAdapter.notifyDataSetChanged();

                    break;

                case GET_ONLINER_REQUEST:
                    getOnLiner();
                    break;

                case GET_STREAM_REQUEST:
                    getLiveStream();
                    break;

                case GET_ANCHOR_REQUEST:
                    getAnchorInfo();

                    break;

                case FAVORITE_LIKE_SUCCESS:
                    has_favorite = "1";
                    tvFollow.setText("已关注");
                    ToastUtil.show(LiveActivity.this, "关注成功");
                    break;
                case UN_FAVORITE_LIKE_SUCCESS:
                    has_favorite = "0";
                    tvFollow.setText("关注");
                    ToastUtil.show(LiveActivity.this, "取消关注成功");
                    break;
                case FAVORITE_LIKE_FAIL:
                    ToastUtil.show(LiveActivity.this, "操作失败");
                    break;
                case GET_ANCHOR_SUCCESS:

                    UserInfoHandler mAnchorHandler = (UserInfoHandler) msg.obj;

                    UserInfo mUserInfo = mAnchorHandler.getUserInfo();

                    if (null != mUserInfo)
                    {
                        if (null != ivUserPic)
                        {
                            ImageLoader.getInstance().displayImage(mUserInfo.getFace(), ivUserPic);
                        }
                        if (null != tvUserName) tvUserName.setText(mUserInfo.getNick());
                        if (null != tvFavourCount) tvFavourCount.setText(mUserInfo.getFavour_count());
                        has_favorite = mUserInfo.getHas_favorite();
                        if (null != tvFollow)
                        {

                            if ("1".equals(mUserInfo.getHas_favorite()))
                            {
                                tvFollow.setText("已关注");
                            }
                            else
                            {
                                tvFollow.setText("关注");
                            }
                        }
                    }
                    break;

                //                case SHOW_SYSTEM_TV:
                //                    dmShow = true;
                //                    tvSystem.setVisibility(View.VISIBLE);
                //                    break;

                case GET_FORTUNE_GIFT_SUCCESS:

                    LivePriceInfoHandler mHandler1 = (LivePriceInfoHandler) msg.obj;
                    PriceInfo mPriceInfo = mHandler1.getLivePriceInfo();
                    if (null != mPriceInfo) buyGift(mPriceInfo.getId(), mPriceInfo.getFinger());
                    break;

                case GET_FORTUNE_GIFT_FAIL:

                    LivePriceInfoHandler resultHandler = (LivePriceInfoHandler) msg.obj;
                    String resultCode = resultHandler.getResultCode();

                    //1102  余额不足，引导充值
                    //1112  需要购买会员（此刻引导购买会员）
                    //1113 需要邀请5人（此刻需要引导去任务大厅）
                    if ("1102".equals(resultCode))
                    {
                        DialogUtils.showToastDialog2Button(LiveActivity.this, resultHandler.getResultMsg(), new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                startActivity(new Intent(LiveActivity.this, MyMasonryActivity.class));
                            }
                        });
                    }
                    else if ("1112".equals(resultCode))
                    {
                        DialogUtils.showToastDialog2Button(LiveActivity.this, resultHandler.getResultMsg(), new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                startActivity(new Intent(LiveActivity.this, MemberActivity.class));
                            }
                        });
                    }
                    else if ("1113".equals(resultCode))
                    {
                        DialogUtils.showToastDialog2Button(LiveActivity.this, resultHandler.getResultMsg(), new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                startActivity(new Intent(LiveActivity.this, TaskActivity.class));
                            }
                        });
                    }
                    break;

                case FORTUNE_BUY_SUCCESS:

                    mGiftCount -= mGiftPrice;
                    ToastUtil.show(LiveActivity.this, "感谢您的支持");


                    break;
                case FORTUNE_BUY_SUCCESS_FAIL:
                    ToastUtil.show(LiveActivity.this, msg.obj.toString());
                    break;

                case WEBSOCKET_CONNECT:

                    initWebSocket();

                    break;

                case SHOW_TOAST:
                    ToastUtil.show(LiveActivity.this, msg.obj.toString());
                    initWebSocket();
                    break;

                case GET_USER_DETAIL_REQUEST:
                    getUserDetail();
                    break;

                case GET_USER_DETAIL_SUCCESS:
                    UserInfoHandler mUserInfoHandler = (UserInfoHandler) msg.obj;
                    UserInfo userInfo = mUserInfoHandler.getUserInfo();

                    if (null != userInfo)
                    {

                        FortuneInfo fortuneInfo = userInfo.getFortuneInfo();

                        if (null != fortuneInfo)
                        {
                            mCashCount = Integer.parseInt(fortuneInfo.getCash());
                            mGiftCount = Integer.parseInt(fortuneInfo.getGift());
                        }

                    }
                    break;
            }
        }
    };


    @Override
    protected void initData()
    {
        biz_id = getIntent().getStringExtra("biz_id");
        location = getIntent().getStringExtra("location");


        String[] giftNameArr = getResources().getStringArray(R.array.gift_name);
        String[] giftPriceArr = getResources().getStringArray(R.array.gift_price);
        String[] giftStyleArr = getResources().getStringArray(R.array.gift_style);
        String[] giftIdArr = getResources().getStringArray(R.array.gift_id);
        int[] giftDrawableArr = new int[]{R.drawable.ic_gift_pear, R.drawable.ic_gift_666, R.drawable.ic_gift_blanana, R.drawable.ic_gift_cannon, R
                .drawable.ic_gift_ring, R.drawable.ic_gift_car, R.drawable.ic_gift_car1, R.drawable.ic_gift_love};

        for (int i = 0; i < giftNameArr.length; i++)
        {
            GiftInfo giftInfo = new GiftInfo();
            giftInfo.setDrawableId(giftDrawableArr[i]);
            giftInfo.setGiftName(giftNameArr[i]);
            giftInfo.setGiftPrice(giftPriceArr[i]);
            giftInfo.setGiftStyle(giftStyleArr[i]);
            giftInfo.setGiftId(giftIdArr[i]);
            giftInfoList.add(giftInfo);

        }


    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live);
    }

    @Override
    protected void initEvent()
    {
        mContentLayout.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        ivClosed.setOnClickListener(this);
        ivGift.setOnClickListener(this);
        ivReport.setOnClickListener(this);
        tvDm.setOnClickListener(this);
        ivUserPic.setOnClickListener(this);
        etSay.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                    String sendMsg = etSay.getText().toString();

                    if (!TextUtils.isEmpty(sendMsg) && null != mWebSocket && mWebSocket.isOpen())
                    {
                        String sendContent = "{\"type\":\"say\",\"data\":\"" + sendMsg + "\"," + "\"action\":\"\"}";
                        LogUtil.e("TAG", "sendContent-->" + sendContent);
                        mWebSocket.sendText(sendContent);
                        etSay.setText("");

                        try
                        {
                            ChatInfo chatInfo = new ChatInfo(new JSONObject(sendContent));
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUface(ConfigManager.instance().getUserPic());
                            String userName = ConfigManager.instance().getUserNickName();
                            userInfo.setUnick(TextUtils.isEmpty(userName) ? ConfigManager.instance().getUserName() : userName);
                            userInfo.setVip_level(ConfigManager.instance().getVipLevelVIE() + "");
                            chatInfo.setUserInfo(userInfo);
                            mChatInfoList.add(chatInfo);
                            mChatAdapter.notifyItemChanged(mChatInfoList.size());
                            mChatRecyclerView.scrollToPosition(mChatInfoList.size() - 1);
                            //                                    }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

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
        if (StringUtils.stringIsEmpty(location))
        {
            tvLocation.setVisibility(View.GONE);
        }
        else
        {
            tvLocation.setText("主播正在" + location + "进行直播");
            tvLocation.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvOnline.setLayoutManager(layoutmanager);
        mOnlineAdapter = new OnlineAdapter(onlineList);
        rvOnline.setAdapter(mOnlineAdapter);
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(false);
        videoPlayer.setVideoAllCallBack(new VideoAllCallBack()
        {
            //开始加载，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onStartPrepared(String s, Object... objects)
            {

            }

            //加载成功，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onPrepared(String s, Object... objects)
            {
                setStatistics(0);
            }

            //点击了开始按键播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickStartIcon(String s, Object... objects)
            {

            }

            //点击了错误状态下的开始按键，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickStartError(String s, Object... objects)
            {

            }

            //点击了播放状态下的开始按键--->停止，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickStop(String s, Object... objects)
            {

            }

            //点击了暂停状态下的开始按键--->播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickStopFullscreen(String s, Object... objects)
            {

            }

            //点击了暂停状态下的开始按键--->播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickResume(String s, Object... objects)
            {

            }

            //点击了全屏暂停状态下的开始按键--->播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickResumeFullscreen(String s, Object... objects)
            {

            }

            //点击了空白弹出seekbar，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickSeekbar(String s, Object... objects)
            {

            }

            //点击了全屏的seekbar，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickSeekbarFullscreen(String s, Object... objects)
            {

            }


            //播放完了，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onAutoComplete(String s, Object... objects)
            {

            }


            //进去全屏，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onEnterFullscreen(String s, Object... objects)
            {

            }

            //退出全屏，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onQuitFullscreen(String s, Object... objects)
            {

            }

            //进入小窗口，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onQuitSmallWidget(String s, Object... objects)
            {

            }

            //退出小窗口，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onEnterSmallWidget(String s, Object... objects)
            {

            }

            //触摸调整声音，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onTouchScreenSeekVolume(String s, Object... objects)
            {

            }

            //触摸调整进度，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onTouchScreenSeekPosition(String s, Object... objects)
            {

            }

            //触摸调整亮度，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onTouchScreenSeekLight(String s, Object... objects)
            {

            }

            //播放错误，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onPlayError(String s, Object... objects)
            {
                LogUtil.e("TAG", "播放错误111111111111111111111111111111111111111111111111111");

                DialogUtils.showPromptDialog(LiveActivity.this, "当前主播不在线", new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        finish();
                    }
                });
            }

            //点击了空白区域开始播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickStartThumb(String s, Object... objects)
            {

            }

            //点击了播放中的空白区域，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickBlank(String s, Object... objects)
            {

            }

            //点击了全屏播放中的空白区域，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onClickBlankFullscreen(String s, Object... objects)
            {

            }
        });

        //tvWelcomeName.setText("欢迎 " + ConfigManager.instance().getUserNickName() + " 进入直播间");

        tvDm.setSelected(true);
        initChat();
        startTime = System.currentTimeMillis();

        mHandler.sendEmptyMessage(GET_ONLINER_REQUEST);
        mHandler.sendEmptyMessage(GET_ANCHOR_REQUEST);
        LPAnimationManager.init(this);
        LPAnimationManager.addGiftContainer(llGiftContainer);


    }


    private void initChat()
    {

        mSystemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSystemChatAdapter = new ChatAdapter(mSystemChatInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                ChatInfo chatInfo = mSystemChatInfoList.get(position);
                if ("live://index".equals(chatInfo.getAction()))
                {
                    sendBroadcast(new Intent(MainActivity.TAB_LIVE));
                    finish();
                }
                else if (chatInfo.getAction().startsWith("video"))
                {
                    sendBroadcast(new Intent(MainActivity.TAB_VIDEO));
                    finish();
                }

                else if (chatInfo.getAction().startsWith("http") || chatInfo.getAction().startsWith("https"))
                {
                    startActivity(new Intent(LiveActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "详情").putExtra
                            (WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, chatInfo.getAction()));
                }

            }
        });
        mSystemRecyclerView.setAdapter(mSystemChatAdapter);


        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mChatAdapter = new ChatAdapter(mChatInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }
        });

        mChatRecyclerView.setAdapter(mChatAdapter);
    }


    private void getUserDetail()
    {
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(LiveActivity.this, Urls.getUserInfoUrl(), this, HttpRequest.GET, GET_USER_DETAIL, valuePairs, new
                UserInfoHandler());
    }

    private void getLivePrice()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "live");
        DataRequest.instance().request(LiveActivity.this, Urls.getLivePriceUrl(), this, HttpRequest.GET, GET_LIVE_PRICE, valuePairs, new
                LivePriceInfoHandler());

    }

    //获取直播地址
    private void getLiveStream()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        DataRequest.instance().request(LiveActivity.this, Urls.getLiveStreamUrl(), this, HttpRequest.GET, GET_LIVE_STREAM, valuePairs, new
                LiveInfoHandler());
    }

    //兑换
    private void buyLive(String finger)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "live");
        valuePairs.put("finger", finger);
        DataRequest.instance().request(LiveActivity.this, Urls.getBuyLiveUrl(), this, HttpRequest.POST, BUY_LIVE, valuePairs, new ResultHandler());
    }


    //通知单服务器
    private void setStatistics(long duration)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "live");
        valuePairs.put("duration", String.valueOf(duration));
        DataRequest.instance().request(LiveActivity.this, Urls.getStatisticsUrl(), this, HttpRequest.POST, "SET_STATISTICS", valuePairs, new
                ResultHandler());
    }

    private void getAnchorInfo()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        DataRequest.instance().request(LiveActivity.this, Urls.getAnchorDetailUrl(), this, HttpRequest.GET, GET_ANCHOR_INFO, valuePairs, new
                UserInfoHandler());
    }

    private void getOnLiner()
    {

        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "live");
        DataRequest.instance().request(LiveActivity.this, Urls.getOnlinerUrl(), this, HttpRequest.GET, GET_ONLINER, valuePairs, new
                OnlinerListHandler());


    }

    private void favoriteLike()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "live");
        DataRequest.instance().request(LiveActivity.this, Urls.getCollectionRequestUrl(), this, HttpRequest.POST, FAVORITE_LIKE, valuePairs, new
                ResultHandler());
    }

    private void unFavoriteLike()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "live");
        DataRequest.instance().request(LiveActivity.this, Urls.getFavoriteUnLikeUrl(), this, HttpRequest.POST, UN_FAVORITE_LIKE, valuePairs, new
                ResultHandler());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == tvFollow)
        {
            if ("1".equals(has_favorite))
            {
                unFavoriteLike();
            }
            else
            {
                favoriteLike();
            }

        }
        else if (v == tvDm)
        {
            if (dmShow)
            {
                dmShow = false;
                mChatRecyclerView.setVisibility(View.GONE);
                tvDm.setSelected(false);
            }
            else
            {
                dmShow = true;
                mChatRecyclerView.setVisibility(View.VISIBLE);
                tvDm.setSelected(true);
            }
        }
        else if (v == ivReport)
        {

            ToastUtil.show(LiveActivity.this, "该功能内测中，只能内测用户使用");
        }
        else if (v == ivClosed)
        {
            DialogUtils.showToastDialog2Button(LiveActivity.this, "是否退出观看该直播", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }
        else if (v == ivUserPic)
        {
            if (!tvFollow.isShown())
            {
                tvFollow.setVisibility(View.VISIBLE);
            }
        }
        else if (v == ivGift)
        {
            mGiftDialog = showGiftDialog(LiveActivity.this, giftInfoList);

            mGiftDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialogInterface)
                {
                    mLiveBottomLayout.setVisibility(View.VISIBLE);
                }
            });

            mLiveBottomLayout.setVisibility(View.GONE);
            mGiftDialog.show();

        }
        //WS sendMsg
        // mSocketClient.send("");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //videoPlayer.onVideoResume();
        mHandler.sendEmptyMessage(GET_STREAM_REQUEST);
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        //        videoPlayer.onVideoReset();
        //        if (!TextUtils.isEmpty(playUrl))
        //        {
        //            videoPlayer.setUp(playUrl, false, "");
        //            videoPlayer.startPlayLogic();
        //        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 100;
        setStatistics(duration);

        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        if (mWebSocket != null)
        {
            mWebSocket.sendClose();
        }
        LPAnimationManager.release();
    }


    private WebSocket mWebSocket;
    private Timer timer = new Timer();
    private TimerTask webSocketTask;

    int mWSSocketConnectCount = 0;

    class WsListener extends WebSocketAdapter
    {
        @Override
        public void onTextMessage(WebSocket websocket, String message) throws Exception
        {
            super.onTextMessage(websocket, message);
            LogUtil.e("onTextMessage", message);
            if (!TextUtils.isEmpty(message))
            {
                try
                {
                    ChatInfo chatInfo = new ChatInfo(new JSONObject(message));

                    if (null != chatInfo)
                    {


                        String action = chatInfo.getAction();
                        String data = chatInfo.getData();
                        String type = chatInfo.getType();

                        if ("sys".equals(type))
                        {
                            if (mSystemChatInfoList.size() >= 2)
                            {
                                mSystemChatInfoList.remove(0);
                            }
                            mSystemChatInfoList.add(chatInfo);
                            mSystemChatAdapter.notifyDataSetChanged();
                        }
                        else if ("say".equals(type))
                        {
                            mChatInfoList.add(chatInfo);
                            mChatAdapter.notifyItemChanged(mChatInfoList.size());
                            mChatRecyclerView.scrollToPosition(mChatInfoList.size() - 1);
                            //                                    }
                        }
                        else
                        {
                            //礼物
                            if ("gift".equals(action))
                            {
                                UserInfo userInfo = chatInfo.getUserInfo();

                                String giftStr = chatInfo.getData();

                                if (!TextUtils.isEmpty(giftStr) && giftStr.contains("@"))
                                {
                                    String gift[] = giftStr.split("@");
                                    if (null != userInfo)
                                    {
                                        LPAnimationManager.addAnimalMessage(new AnimMessage(userInfo.getUnick(), userInfo.getFace(), 1, getGifName
                                                (gift[1]), getGiftDrawable(gift[1])));
                                    }
                                }


                            }
                            else
                            {
                                if ("liveads".equals(chatInfo.getAction()))
                                {
                                    ToastUtil.show(LiveActivity.this, chatInfo.getData());
                                    finish();
                                }
                                else
                                {
                                    if ("nosay".equals(action))
                                    {
                                        etSay.setHint("SVIP可发言");
                                        etSay.setEnabled(false);
                                    }
                                    else
                                    {
                                        mChatInfoList.add(chatInfo);
                                        mChatAdapter.notifyItemChanged(mChatInfoList.size());

                                        if (null != mChatRecyclerView) mChatRecyclerView.scrollToPosition(mChatInfoList.size() - 1);
                                    }


                                }

                            }
                        }


                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception
        {
            super.onConnected(websocket, headers);
            LogUtil.e("onTextMessage", "连接成功");
            timer.schedule(webSocketTask, 0, 30000);
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception
        {
            super.onConnectError(websocket, exception);
            LogUtil.e("onTextMessage", "连接错误：" + exception.getMessage());

            if (mWSSocketConnectCount < 10)
            {
                mWSSocketConnectCount++;
                mHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        initWebSocket();
                    }
                }, 30 * 1000);
            }

        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer)
                throws Exception
        {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            LogUtil.e("onTextMessage", "断开连接：");
            initWebSocket();
        }

    }

    private void initWebSocket()
    {
        webSocketTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (null != mWebSocket && mWebSocket.isOpen())
                {

                    mWebSocket.sendPing();
                }
            }
        };

        try
        {
            String wsUri = ConfigManager.instance().getChatUrl() + "/" + ConfigManager.instance().getUniqueCode() + "?co_biz=live&biz_id=" + biz_id;
            LogUtil.e("wsUri", "wsUri == " + wsUri);
            mWebSocket = new WebSocketFactory().createSocket(wsUri, 30 * 1000) //ws地址，和设置超时时间
                    .setFrameQueueSize(5)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(new LiveActivity.WsListener())//添加回调监听
                    .connectAsynchronously();//异步连接

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed()
    {
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (GET_LIVE_STREAM.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else if ("1502".endsWith(resultCode))
            {
                getLivePrice();
            }
            else
            {

                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_LIVE_PRICE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_LIVE_PRICE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (BUY_LIVE.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(BUY_LIVE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_ONLINER.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ONLINER_SUCCESS, obj));
            }
            else
            {
                // mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (FAVORITE_LIKE.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(FAVORITE_LIKE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(FAVORITE_LIKE_FAIL, resultMsg));
            }
        }
        else if (UN_FAVORITE_LIKE.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(UN_FAVORITE_LIKE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(FAVORITE_LIKE_FAIL, resultMsg));
            }
        }
        else if (GET_ANCHOR_INFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ANCHOR_SUCCESS, obj));
            }
            else
            {
                //mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_FORTUNE_GIFT.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_FORTUNE_GIFT_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_FORTUNE_GIFT_FAIL, obj));
            }
        }

        else if (FORTUNE_BUY.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(FORTUNE_BUY_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(FORTUNE_BUY_SUCCESS_FAIL, resultMsg));
            }
        }
        else if (GET_USER_DETAIL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_USER_DETAIL_SUCCESS, obj));
            }
            //            else
            //            {
            //                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            //            }
        }


    }


    private static class NoLeakHandler extends Handler
    {
        private WeakReference<LiveActivity> mActivity;

        public NoLeakHandler(LiveActivity activity)
        {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
        }
    }


    /***************** 礼物 ***********************************************/

    private Dialog mGiftDialog;
    private GiftAdapter giftAdapter;

    private int giftSelectedItem = -1;

    /**
     * 礼物
     *
     * @return
     */
    private Dialog showGiftDialog(Context mContext, final List<GiftInfo> giftInfoList)
    {
        final Dialog dialog = new Dialog(mContext, R.style.DialogStyle);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_gift, null);
        dialog.setContentView(v);

        TextView tvGift = (TextView) v.findViewById(R.id.tv_gift);
        TextView tvCash = (TextView) v.findViewById(R.id.tv_cash);

        tvGift.setText("积分:" + mGiftCount);
        tvCash.setText(mCashCount + "");

        //  ImageView ivMasonry = (ImageView) v.findViewById(R.id.iv_masonry);
        TextView tvSubmit = (TextView) v.findViewById(R.id.tv_submit);

        tvSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                giveGift();
            }
        });


        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new FullyGridLayoutManager(mContext, 4));


        //changeGiftPrice(tvGiftPrice, ivMasonry);
        giftAdapter = new GiftAdapter(mContext, giftInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                giftSelectedItem = position;
                for (int i = 0; i < giftInfoList.size(); i++)
                {

                    if (i == position)
                    {
                        giftInfoList.get(position).setSelected(true);
                    }
                    else
                    {
                        giftInfoList.get(i).setSelected(false);
                    }
                }

                if (null != giftAdapter)
                {
                    giftAdapter.notifyDataSetChanged();
                }

                //changeGiftPrice(tvGiftPrice, ivMasonry);

            }
        });
        recyclerView.setAdapter(giftAdapter);


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mWindow.setAttributes(lp);
        return dialog;

    }


    private void changeGiftPrice(TextView mGiftPriceTv, ImageView mMasonryIv)
    {
        if (giftSelectedItem < 0)
        {
            return;
        }


        GiftInfo giftInfo = giftInfoList.get(giftSelectedItem);


        if ("0".equals(giftInfo.getGiftStyle()))
        {
            mMasonryIv.setVisibility(View.GONE);
            mGiftPriceTv.setText(giftInfo.getGiftPrice() + "积分");
        }
        else
        {
            mMasonryIv.setVisibility(View.VISIBLE);
            mGiftPriceTv.setText(giftInfo.getGiftPrice());
        }
    }


    private void giveGift()
    {
        if (giftSelectedItem < 0)
        {
            ToastUtil.show(this, "请选择您要赠送的你礼物");
            return;
        }
        mGiftDialog.dismiss();

        GiftInfo giftInfo = giftInfoList.get(giftSelectedItem);
        mGiftPrice = Integer.parseInt(giftInfo.getGiftPrice());
        getFortuneGift(giftInfo.getGiftId());


        //
        //        LPAnimationManager.addAnimalMessage(new AnimMessage("yu11", "", 10, "飞机"));
        //        LPAnimationManager.addAnimalMessage(new AnimMessage("hellokitty11", "", 10,
        // "飞机"));
    }


    private void getFortuneGift(String giftid)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("giftid", giftid);
        DataRequest.instance().request(LiveActivity.this, Urls.getFortuneGiftUrl(), this, HttpRequest.POST, GET_FORTUNE_GIFT, valuePairs, new
                LivePriceInfoHandler());
    }


    private void buyGift(String giftId, String finger)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", giftId);
        valuePairs.put("co_biz", "gift");
        valuePairs.put("finger", finger);
        DataRequest.instance().request(LiveActivity.this, Urls.getFortuneBuyUrl(), this, HttpRequest.POST, FORTUNE_BUY, valuePairs, new
                ResultHandler());
    }

    /***************** 礼物 ***********************************************/

    private String getGifName(String giftId)
    {
        String[] giftNameArr = getResources().getStringArray(R.array.gift_name);
        String[] giftIdArr = getResources().getStringArray(R.array.gift_id);
        String giftName = "";
        for (int i = 0; i < giftIdArr.length; i++)
        {
            if (giftId.equals(giftIdArr[i]))
            {
                giftName = giftNameArr[i];
                break;
            }
        }

        return giftName;
    }


    private int getGiftDrawable(String giftId)
    {
        int[] giftDrawableArr = new int[]{R.drawable.ic_gift_pear, R.drawable.ic_gift_666, R.drawable.ic_gift_blanana, R.drawable.ic_gift_cannon, R
                .drawable.ic_gift_ring, R.drawable.ic_gift_car, R.drawable.ic_gift_car1, R.drawable.ic_gift_love};
        String[] giftIdArr = getResources().getStringArray(R.array.gift_id);
        int mDrawableId = giftDrawableArr[0];
        for (int i = 0; i < giftIdArr.length; i++)
        {
            if (giftId.equals(giftIdArr[i]))
            {
                mDrawableId = giftDrawableArr[i];
                break;
            }
        }

        return mDrawableId;
    }
}
