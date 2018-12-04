package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kd.easybarrage.Barrage;
import com.kd.easybarrage.BarrageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.zb.wyd.R;
import com.zb.wyd.adapter.RecommendVideoAdapter;
import com.zb.wyd.adapter.VideoChannelAdapter;
import com.zb.wyd.entity.ChannelInfo;
import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.entity.DanmuInfo;
import com.zb.wyd.entity.HostInfo;
import com.zb.wyd.entity.MybizInfo;
import com.zb.wyd.entity.PriceInfo;
import com.zb.wyd.entity.ShareInfo;
import com.zb.wyd.entity.SignInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.entity.VideoDetailInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.DanmuInfoListHandler;
import com.zb.wyd.json.LivePriceInfoHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.ShareInfoHandler;
import com.zb.wyd.json.SignInfoHandler;
import com.zb.wyd.json.VideoDetailHandler;
import com.zb.wyd.json.VideoInfoListHandler;
import com.zb.wyd.json.VideoStreamHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.listener.MyOnClickListener;
import com.zb.wyd.listener.SocketListener;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.MySocketConnection;
import com.zb.wyd.utils.SystemUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.ChannelPopupWindow;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;
import com.zb.wyd.widget.MyVideoPlayer;
import com.zb.wyd.widget.StarBar;
import com.zb.wyd.widget.gift.AnimMessage;
import com.zb.wyd.widget.gift.LPAnimationManager;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moe.codeest.enviews.ENPlayView;

/**
 * 描述：一句话简单描述
 */
public class VideoPlayActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.detail_player)
    MyVideoPlayer videoPlayer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_collection)
    TextView tvCollection;


    @BindView(R.id.tv_video_name)
    TextView tvVideoName;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.starBar)
    StarBar starBar;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_collection_count)
    TextView tvCollectionCount;
    @BindView(R.id.iv_play_count)
    ImageView ivPlayCount;
    @BindView(R.id.tv_play_count)
    TextView tvPlayCount;

    @BindView(R.id.tv_add_time)
    TextView tvAddTime;
    @BindView(R.id.iv_video_follow)
    ImageView ivVideoFollow;
    @BindView(R.id.rv_recommend_video)
    MaxRecyclerView rvRecommendVideo;
    @BindView(R.id.tv_tangmu)
    TextView tvTangmu;
    @BindView(R.id.iv_hover)
    ImageView ivHover;
    @BindView(R.id.tv_set_score)
    TextView tvSetScore;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.rv_channel)
    MaxRecyclerView rvChannel;

    @BindView(R.id.barrageView)
    BarrageView barrageView;


    private int myScore;
    private RecommendVideoAdapter mRecommendVideoAdapter;
    private List<VideoInfo> videoInfoList = new ArrayList<>();


    private VideoChannelAdapter mVideoChannelAdapter;
    private List<HostInfo> hostInfoList = new ArrayList<>();
    private OrientationUtils orientationUtils;
    //    private ImageView mCollectionIv;
    //    private ImageView mShareIv;
    //    private ImageView mReportIv;
    //    private ImageView mSettingIv;

    private ENPlayView mPlayButton;
    private LinearLayout mSeekLayout;

    private int is_fav;
    private int is_like;
    private VideoInfo mVideoInfo;
    private String videoName, biz_id;
    private String videoUri;
    private ChannelInfo mChannelInfo;
    private String shareCnontent;
    private long startTime, endTime;
    private String errorMsg = "网络异常";

    private List<DanmuInfo> danmuInfoList = new ArrayList<>();
    private String videoPlaylist;

    private static final String MSG_REPORT = "msg_report";
    private static final String GET_SHARE = "GET_SHARE";
    private static final String FAVORITE_LIKE = "favorite_like";
    private static final String UN_FAVORITE_LIKE = "un_favorite_like";
    private static final String GET_VIDEO_PRICE = "get_live_price";
    private static final String GET_VIDEO_STREAM = "get_video_stream";
    private static final String BUY_VIDEO = "buy_live";
    private static final String GET_TASK_SHARE = "GET_TASK_SHARE";
    private static final String OPAER_SCORE = "opaer_score";
    private static final String GET_VIDEO_DETAIL = "get_video_detail";
    private static final String GET_VIDEO_RECOMMEND = "get_video_recommend";

    private static final String VIDEO_COLLECTION = "video_collection";
    private static final String VIDEO_UNCOLLECTION = "video_uncollection";
    private static final String GET_DAN_MU = "get_dan_mu";

    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int GET_VIDEO_PRICE_SUCCESS = 0x03;
    private static final int BUY_VIDEO_SUCCESS = 0x05;
    private static final int SET_STATISTICS = 0x06;
    private static final int GET_STREAM_REQUEST = 0x09;
    private static final int FAVORITE_LIKE_SUCCESS = 0x08;
    private static final int UN_FAVORITE_LIKE_SUCCESS = 0x10;
    private static final int GET_SHARE_CODE = 0x11;
    private static final int GET_SHARE_SUCCESS = 0x12;
    private static final int GET_TASK_SHARE_CODE = 0x13;
    private static final int GET_TASK_SHARE_SUCCESS = 0x14;
    private static final int SHARE_PHOTO_REQUEST_CODE = 0x91;
    private static final int MSG_REPORT_SUCCESS = 0x21;
    private static final int OPEAR_SCORE_SUCCESS = 0x16;
    private static final int GET_VIDEO_DETAIL_SUCCESS = 0x17;


    private static final int VIDEO_COLLECTION_SUCCESS = 0x18;
    private static final int VIDEO_UNCOLLECTION_SUCCESS = 0x19;

    private static final int GET_VIDEO_RECOMMEND_SUCCESS = 0x20;

    private static final int GET_VIDEO_CURRENT_POSITION = 0x31;

    private static final int GET_DAN_MU_CODE = 0x33;
    private static final int GET_DAN_MU_SUCCESS = 0x32;

    private static final int SHOW_TOAST = 0X34;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(VideoPlayActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    VideoStreamHandler mVideoStreamHandler = (VideoStreamHandler) msg.obj;

                    mChannelInfo = mVideoStreamHandler.getChannelInfo();
                    if (null != mChannelInfo)
                    {
                        videoUri = mVideoStreamHandler.getUri();
                        String uri = mChannelInfo.getCm() + videoUri;
                        LogUtil.e("TAG", uri);
                        //                        has_favorite = mVideoStreamHandler
                        // .getHas_favorite();


                        //                        videoPlayer.setIS_SCREEN_ORIENTATION_LANDSCAPE
                        // (mVideoStreamHandler.getStand());
                        //                      if(mVideoStreamHandler.getStand())
                        //                      {
                        //                          mSeekLayout.setVisibility(View.INVISIBLE);
                        //                          orientationUtils.setScreenType( ActivityInfo
                        // .SCREEN_ORIENTATION_LANDSCAPE);
                        //                      }
                        //                      else
                        //                      {
                        //                          orientationUtils.setScreenType( ActivityInfo
                        // .SCREEN_ORIENTATION_PORTRAIT);
                        //                          orientationUtils .resolveByClick();
                        //                      }

                        playVideo(uri);
                    }
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(VideoPlayActivity.this, msg.obj.toString());
                    break;

                case GET_VIDEO_PRICE_SUCCESS:
                    LivePriceInfoHandler mLivePriceInfoHandler = (LivePriceInfoHandler) msg.obj;

                    final PriceInfo mLivePriceInfo = mLivePriceInfoHandler.getLivePriceInfo();

                    if (null != mLivePriceInfo)
                    {


                        DialogUtils.showVideoPriceDialog(VideoPlayActivity.this, mLivePriceInfo, new View.OnClickListener()
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

                                    buyVideo(mLivePriceInfo.getFinger());


                                }
                                //购买VIP
                                else if ("3".equals(content))
                                {
                                    startActivity(new Intent(VideoPlayActivity.this, MemberActivity.class));
                                }
                                else//去做任务
                                {

                                    // sendBroadcast(new Intent(MainActivity.TAB_TASK));
                                    startActivity(new Intent(VideoPlayActivity.this, TaskActivity.class));
                                    finish();
                                }

                            }
                        }).show();
                    }
                    break;

                case BUY_VIDEO_SUCCESS:
                    getVideoStream();
                    break;

                case SET_STATISTICS:
                    // setStatistics(0);
                    break;


                case GET_STREAM_REQUEST:
                    getVideoStream();
                    break;
                case FAVORITE_LIKE_SUCCESS:
                    is_like = 1;
                    ToastUtil.show(VideoPlayActivity.this, "点赞成功");
                    ivHover.setSelected(true);
                    break;
                case UN_FAVORITE_LIKE_SUCCESS:
                    is_like = 0;
                    ToastUtil.show(VideoPlayActivity.this, "取消成功");
                    ivHover.setSelected(false);
                    break;
                case VIDEO_COLLECTION_SUCCESS:
                    is_fav = 1;
                    ToastUtil.show(VideoPlayActivity.this, "收藏成功");
                    tvCollection.setSelected(true);
                    tvCollection.setText("已收藏");
                    break;
                case VIDEO_UNCOLLECTION_SUCCESS:
                    is_fav = 0;
                    ToastUtil.show(VideoPlayActivity.this, "取消成功");
                    tvCollection.setSelected(false);
                    tvCollection.setText("未收藏");
                    break;


                case GET_SHARE_CODE:
                    getShareUrl();
                    break;

                case GET_SHARE_SUCCESS:
                    ShareInfoHandler mShareInfoHandler = (ShareInfoHandler) msg.obj;
                    ShareInfo shareInfo = mShareInfoHandler.getShareInfo();
                    if (null != shareInfo)
                    {
                        shareCnontent = shareInfo.getTitle() + ":" + shareInfo.getUrl();

                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.putExtra(Intent.EXTRA_TEXT, shareCnontent);
                        intent1.setType("text/plain");
                        startActivityForResult(Intent.createChooser(intent1, "分享"), SHARE_PHOTO_REQUEST_CODE);
                    }
                    break;

                case GET_TASK_SHARE_CODE:
                    getTaskShareUrl();
                    break;

                case GET_TASK_SHARE_SUCCESS:
                    SignInfoHandler mSignInfoHandler = (SignInfoHandler) msg.obj;
                    SignInfo signInfo = mSignInfoHandler.getSignInfo();

                    if (null != signInfo)
                    {
                        String title = "分享成功";
                        String desc = signInfo.getVal() + "积分";
                        String task = "连续分享" + signInfo.getSeries() + "天";
                        DialogUtils.showTaskDialog(VideoPlayActivity.this, title, desc, task);
                    }
                    break;

                case MSG_REPORT_SUCCESS:
                    ToastUtil.show(VideoPlayActivity.this, "信息上报成功");
                    break;

                case OPEAR_SCORE_SUCCESS:
                    ToastUtil.show(VideoPlayActivity.this, "评价成功");
                    break;

                case GET_VIDEO_DETAIL_SUCCESS:
                    VideoDetailHandler mVideoDetailHandler = (VideoDetailHandler) msg.obj;

                    VideoDetailInfo mVideoDetailInfo = mVideoDetailHandler.getVideoDetailInfo();

                    if (null != mVideoDetailInfo)
                    {
                        hostInfoList.clear();
                        hostInfoList.addAll(mVideoDetailInfo.getHostInfoList());
                        mVideoChannelAdapter.notifyDataSetChanged();
                        videoPlayer.release();
                        mPlayButton.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(mVideoDetailInfo.getPlaylist()))
                        {
                            videoPlaylist = mVideoDetailInfo.getPlaylist();
                        }
                        tvVideoName.setText(mVideoDetailInfo.getIname());


                        getVideoRecommend(mVideoDetailInfo.getTags());
                        if (mVideoDetailInfo.getCash() > 0)
                        {
                            ivVip.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ivVip.setVisibility(View.GONE);
                        }
                        starBar.setStarMark(mVideoDetailInfo.getScore());
                        tvScore.setText(mVideoDetailInfo.getScore() + "分");
                        tvCollectionCount.setText(mVideoDetailInfo.getFavour_count());
                        tvPlayCount.setText(mVideoDetailInfo.getView_count());
                        tvAddTime.setText(mVideoDetailInfo.getAdd_time());


                        UserInfo mUserInfo = mVideoDetailInfo.getUserInfo();

                        if (null != mUserInfo)
                        {
                            ImageLoader.getInstance().displayImage(mUserInfo.getUface(), ivUserPic);
                            tvFans.setText(mUserInfo.getFans() + "粉丝");
                        }


                        MybizInfo mybizInfo = mVideoDetailInfo.getMybizInfo();


                        if (null != mybizInfo)
                        {
                            myScore = mybizInfo.getScore();
                            is_fav = mybizInfo.getIs_fav();
                            if (is_fav == 0)
                            {
                                tvCollection.setSelected(false);
                                tvCollection.setText("未收藏");
                            }
                            else

                            {
                                tvCollection.setSelected(true);
                                tvCollection.setText("已收藏");
                            }
                            is_like = mybizInfo.getIs_like();
                            if (is_like == 1)
                            {
                                ivHover.setSelected(true);
                            }
                            else
                            {
                                ivHover.setSelected(false);
                            }


                        }
                    }
                    break;

                case GET_VIDEO_RECOMMEND_SUCCESS:
                    VideoInfoListHandler mVideoInfoListHandler = (VideoInfoListHandler) msg.obj;
                    videoInfoList.clear();
                    videoInfoList.addAll(mVideoInfoListHandler.getVideoInfoList());
                    mRecommendVideoAdapter.notifyDataSetChanged();

                    break;

                case GET_VIDEO_CURRENT_POSITION:
                    LogUtil.e("TAG", "CurrentPosition = " + videoPlayer.getGSYVideoManager().getCurrentPosition());

                    long mCurrentPosition = videoPlayer.getGSYVideoManager().getCurrentPosition();
                    List<DanmuInfo> mDanmuInfoList = getTimePosDanmuList(mCurrentPosition);

                    for (int i = 0; i < mDanmuInfoList.size(); i++)
                    {
                        LogUtil.e("TAG", "danmuInfoList.size == " + i);
                         DanmuInfo mDanmuInfo = mDanmuInfoList.get(i);
                         String stryle = mDanmuInfo.getStyle();
                        String color = "#0000FF";
                        if (null != stryle)
                        {
                            color = stryle.split(",")[0];
                        }
                        barrageView.addBarrage(new Barrage(mDanmuInfo.getData(), Color.parseColor(color)));


                    }

                    mHandler.sendEmptyMessageDelayed(GET_VIDEO_CURRENT_POSITION, 1000);
                    break;

                case GET_DAN_MU_CODE:
                    getVideoDanmu();
                    mHandler.sendEmptyMessageDelayed(GET_DAN_MU_CODE, 300 * 1000);
                    break;

                case GET_DAN_MU_SUCCESS:
                    DanmuInfoListHandler mDanmuInfoListHandler = (DanmuInfoListHandler) msg.obj;
                    danmuInfoList.addAll(mDanmuInfoListHandler.getDanmuInfoList());


                    break;

                case SHOW_TOAST:
                    ToastUtil.show(VideoPlayActivity.this, msg.obj.toString());
                    initWebSocket();
                    break;
            }
        }
    };

    @Override
    protected void initData()
    {
        mVideoInfo = (VideoInfo) getIntent().getSerializableExtra("VideoInfo");
        if (null != mVideoInfo)
        {
            videoName = mVideoInfo.getV_name();
            biz_id = mVideoInfo.getId();
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(VideoPlayActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivHover.setOnClickListener(this);
        tvSetScore.setOnClickListener(this);
        tvCollection.setOnClickListener(this);
        etContent.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                    String sendMsg = etContent.getText().toString();

                    if (!TextUtils.isEmpty(sendMsg) && null != mMySocketConnection && mMySocketConnection.isConnected())
                    {
                        if (null != videoPlayer)
                        {
                            Gson gson = new Gson();
                            DanmuInfo danmuInfo = new DanmuInfo();
                            danmuInfo.setAction("");
                            danmuInfo.setData(sendMsg);
                            danmuInfo.setType("say");
                            danmuInfo.setTimpos(videoPlayer.getGSYVideoManager().getCurrentPosition());
                            String json = gson.toJson(danmuInfo);
                            String sendContent = "{\"type\":\"say\",\"data\":\"" + sendMsg + "\"," + "\"action\":\"\"}";
                            LogUtil.e("TAG", "sendContent-->" + json);
                            mMySocketConnection.sendTextMessage(json);
                            etContent.setText("");
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
        initWebSocket();
        mHandler.sendEmptyMessage(GET_DAN_MU_CODE);
        tvTitle.setText("视频播放");
        //        mCollectionIv = (ImageView) videoPlayer.findViewById(R.id.iv_collection);
        //        mShareIv = (ImageView) videoPlayer.findViewById(R.id.iv_share);
        //        mSettingIv = (ImageView) videoPlayer.findViewById(R.id.iv_setting);
        //        mReportIv = (ImageView) videoPlayer.findViewById(R.id.iv_report);
        mSeekLayout = (LinearLayout) videoPlayer.findViewById(R.id.sp_layout);
        mPlayButton = (ENPlayView) videoPlayer.findViewById(R.id.video_play);
        mPlayButton.setOnClickListener(this);

        //        mCollectionIv.setOnClickListener(this);
        //        mShareIv.setOnClickListener(this);
        //        mSettingIv.setOnClickListener(this);
        //        mReportIv.setOnClickListener(this);

        //  videoPlayer.setUp(source1, true, "测试视频");
        //增加封面
        //        ImageView imageView = new ImageView(this);
        //        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //        imageView.setImageResource(R.drawable.ic_launcher);
        //  videoPlayer.setThumbImageView(imageView);
        //增加title
        //   videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);

        //设置返回键
        //    videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        //        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        //        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick(View view)
        //            {
        //                orientationUtils.resolveByClick();
        //            }
        //        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        //        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick(View v)
        //            {
        //                onBackPressed();
        //            }
        //        });
        // videoPlayer.startPlayLogic();


        videoPlayer.setVideoAllCallBack(new VideoAllCallBack()
        {
            //开始加载，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onStartPrepared(String s, Object... objects)
            {
                hideProgressDialog();
            }

            //加载成功，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onPrepared(String s, Object... objects)
            {
                mHandler.sendEmptyMessageDelayed(GET_VIDEO_CURRENT_POSITION, 1000);
                startTime = System.currentTimeMillis();
                //setStatistics(0);
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
                hideProgressDialog();
                LogUtil.e("TAG", "播放错误111111111111111111111111111111111111111111111111111");
                errorMsg = s;
                ToastUtil.show(VideoPlayActivity.this, "该网络暂无法播放，请切换网络重试");
                mPlayButton.setVisibility(View.VISIBLE);
                // mChannelLayout.setVisibility(View.VISIBLE);
                //showChannelPop();

                videoPlayer.showSettingWidget();
                //                DialogUtils.showChannelDialog(VideoPlayActivity.this, new
                // MyItemClickListener()
                //                {
                //                    @Override
                //                    public void onItemClick(View view, int position)
                //                    {
                //                        switch (position)
                //                        {
                //                            case 1:
                //                                playVideo(mChannelInfo.getYd() + videoUri);
                //                                break;
                //                            case 2:
                //                                playVideo(mChannelInfo.getDx() + videoUri);
                //                                break;
                //                            case 3:
                //                                playVideo(mChannelInfo.getCm() + videoUri);
                //                                break;
                //                            case 4:
                //                                finish();
                //                                break;
                //                        }
                //
                //
                //                    }
                //    });


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


        rvRecommendVideo.setLayoutManager(new FullyGridLayoutManager(VideoPlayActivity.this, 2));
        mRecommendVideoAdapter = new RecommendVideoAdapter(videoInfoList, VideoPlayActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                biz_id = videoInfoList.get(position).getId();
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getVideoDetail();
                    }
                });
            }
        });
        rvRecommendVideo.setAdapter(mRecommendVideoAdapter);


        rvChannel.setLayoutManager(new FullyGridLayoutManager(VideoPlayActivity.this, 3));
        mVideoChannelAdapter = new VideoChannelAdapter(hostInfoList, VideoPlayActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                HostInfo mHostInfo = hostInfoList.get(position);

                if ("vip".equals(mHostInfo.getLine()))
                {
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            getLivePrice();
                        }
                    });


                }
            }
        });
        rvChannel.setAdapter(mVideoChannelAdapter);
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                getVideoDetail();
            }
        });

    }

    private int timepos = 0;

    private void getVideoDanmu()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        valuePairs.put("timepos", timepos + "");
        valuePairs.put("duration", "300");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getDanmuUrl(), this, HttpRequest.GET, GET_DAN_MU, valuePairs, new
                DanmuInfoListHandler());
        timepos += 300;
    }


    private void getVideoDetail()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getVideoDetailUrl(), this, HttpRequest.GET, GET_VIDEO_DETAIL, valuePairs, new
                VideoDetailHandler());
    }

    private void getVideoRecommend(String tags)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("tags", tags);
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getVideoRecommendUrl(), this, HttpRequest.GET, GET_VIDEO_RECOMMEND, valuePairs,
                new VideoInfoListHandler());
    }

    private void getVideoStream()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getVideoStreamUrl(), this, HttpRequest.GET, GET_VIDEO_STREAM, valuePairs, new
                VideoStreamHandler());
    }


    private void getLivePrice()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getVideoPriceUrl(), this, HttpRequest.POST, GET_VIDEO_PRICE, valuePairs, new
                LivePriceInfoHandler());

    }

    //    //通知单服务器
    //    private void setStatistics(long duration)
    //    {
    //        Map<String, String> valuePairs = new HashMap<>();
    //        valuePairs.put("biz_id", biz_id);
    //        valuePairs.put("co_biz", "video");
    //        valuePairs.put("duration", String.valueOf(duration));
    //        DataRequest.instance().request(VideoPlayActivity.this, Urls.getStatisticsUrl(), this,
    //                HttpRequest.POST, "SET_STATISTICS", valuePairs, new ResultHandler());
    //    }

    private void playVideo(String uri)
    {
        showProgressDialog();
        LogUtil.e("TAG", "uri--->" + uri);
        if (null != videoPlayer)
        {
            videoPlayer.setUp(uri, false, videoName);
            videoPlayer.startPlayLogic();
        }
    }

    //兑换
    private void buyVideo(String finger)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        valuePairs.put("finger", finger);
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getBuyLiveUrl(), this, HttpRequest.POST, BUY_VIDEO, valuePairs, new
                ResultHandler());
    }

    private void getTaskShareUrl()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getTaskShareUrl(), this, HttpRequest.GET, GET_TASK_SHARE, valuePairs, new
                SignInfoHandler());
    }

    private void getShareUrl()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getShareUrl(), this, HttpRequest.GET, GET_SHARE, valuePairs, new
                ShareInfoHandler());
    }


    private void opearScore(float value)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "photo");
        valuePairs.put("action", "score");
        valuePairs.put("value", String.valueOf(Math.round(value) * 10));
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getOperaUrl(), this, HttpRequest.POST, OPAER_SCORE, valuePairs, new
                ResultHandler());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivHover)
        {
            if (is_like == 1)
            {
                unFavoriteLike();
            }
            else
            {
                favoriteLike();
            }

        }
        else if (v == mPlayButton)
        {
            if (TextUtils.isEmpty(videoPlaylist))
            {

                if (!hostInfoList.isEmpty())
                {

                    if ("vip".equals(hostInfoList.get(0).getLine()))
                    {

                        if (!ConfigManager.instance().getValid_vip())
                        {
                            //提示购买VIP
                        }
                    }
                    else

                    {
                        mHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                getLivePrice();
                            }
                        });
                    }
                }


            }
            else
            {
                if (hostInfoList.isEmpty())
                {
                    playVideo(videoPlaylist);
                }
                else
                {
                    playVideo(hostInfoList.get(0).getLine() + videoPlaylist);
                }

                videoPlayer.updateStartImage();
                mPlayButton.setVisibility(View.GONE);
            }
        }
        else if (v == tvCollection)
        {
            if (is_fav == 1)
            {
                unCollect();
            }
            else
            {
                collect();
            }
        }
        else if (v == tvSetScore)
        {
            if (myScore == 0)
            {
                DialogUtils.showScoreDialog(VideoPlayActivity.this, "视频评价", "这个视频怎么样，请做出您的评价", new MyOnClickListener.OnFloatSubmitListener()


                {
                    @Override
                    public void onSubmit(float content)
                    {
                        opearScore(content);
                    }
                });
            }
            else
            {
                ToastUtil.show(VideoPlayActivity.this, "您已评价过了！");
            }
        }
        //        else if (v == mShareIv)
        //        {
        //            showProgressDialog();
        //            mHandler.sendEmptyMessage(GET_SHARE_CODE);
        //
        //        }
        //        else if (v == mSettingIv)
        //        {
        //            showChannelPop();
        //        }
        //        else if (v == mReportIv)
        //        {
        //            DialogUtils.showReportDialog(this, new MyOnClickListener.OnSubmitListener()
        //            {
        //                @Override
        //                public void onSubmit(String content)
        //                {
        //                    switch (Integer.parseInt(content))
        //                    {
        //                        case 1:
        //                            reportMsg("不显示画面,有声音");
        //                            break;
        //                        case 2:
        //                            reportMsg("显示画面,无声音");
        //                            break;
        //
        //                        case 3:
        //                            reportMsg("画面显示错位");
        //                            break;
        //                        case 4:
        //                            reportMsg(errorMsg);
        //                            break;
        //                    }
        //                }
        //            });
        //        }
    }


    private void reportMsg(String msg)
    {
        showProgressDialog();
        String content = "{\"model\":\"" + SystemUtil.getSystemModel() + "\",\"error\":\"" + msg + "\",\"sys\":\"" + SystemUtil.getSystemVersion()
                + "\",\"video_id\":\"" + biz_id + "\"}";
        LogUtil.e("TAG", "error-->" + content);

        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("content", content);
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getMsgReportUrl(), this, HttpRequest.POST, MSG_REPORT, valuePairs, new
                ResultHandler());

    }


    private void collect()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        valuePairs.put("action", "collect");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getOperaUrl(), this, HttpRequest.POST, VIDEO_COLLECTION, valuePairs, new
                ResultHandler());
    }

    private void unCollect()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        valuePairs.put("action", "uncollect");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getOperaUrl(), this, HttpRequest.POST, VIDEO_UNCOLLECTION, valuePairs, new
                ResultHandler());
    }

    private void favoriteLike()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        valuePairs.put("action", "like");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getOperaUrl(), this, HttpRequest.POST, FAVORITE_LIKE, valuePairs, new
                ResultHandler());
    }

    private void unFavoriteLike()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "video");
        valuePairs.put("action", "unlike");
        DataRequest.instance().request(VideoPlayActivity.this, Urls.getOperaUrl(), this, HttpRequest.POST, UN_FAVORITE_LIKE, valuePairs, new
                ResultHandler());
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
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        //        if (orientationUtils != null)
        //            orientationUtils.releaseListener();

        endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 100;
        //setStatistics(duration);

        mHandler.removeMessages(GET_VIDEO_CURRENT_POSITION);

        if (null != barrageView)
        {
            barrageView.destroy();
        }
    }

    @Override
    public void onBackPressed()
    {
        //        //先返回正常状态
        //        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        //        {
        //            videoPlayer.getFullscreenButton().performClick();
        //            return;
        //        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_VIDEO_STREAM.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }

            else if ("1101".equals(resultCode))
            {
                getLivePrice();
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_VIDEO_PRICE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_VIDEO_PRICE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (BUY_VIDEO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(BUY_VIDEO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (FAVORITE_LIKE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(FAVORITE_LIKE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (UN_FAVORITE_LIKE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(UN_FAVORITE_LIKE_SUCCESS, obj));
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
        else if (GET_TASK_SHARE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_TASK_SHARE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (MSG_REPORT.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(MSG_REPORT_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (OPAER_SCORE.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(OPEAR_SCORE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }

        }
        else if (GET_VIDEO_DETAIL.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_VIDEO_DETAIL_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }

        else if (VIDEO_COLLECTION.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(VIDEO_COLLECTION_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (VIDEO_UNCOLLECTION.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(VIDEO_UNCOLLECTION_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_VIDEO_RECOMMEND.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_VIDEO_RECOMMEND_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_DAN_MU.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_DAN_MU_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DemoActivity", "requestCode=" + requestCode + " resultCode=" + resultCode);
        if ((int) (Math.random() * 100) <= 80) mHandler.sendEmptyMessage(GET_TASK_SHARE_CODE);

    }


    @OnClick({R.id.iv_back, R.id.iv_share})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                break;
        }
    }


    private List<DanmuInfo> getTimePosDanmuList(long timepos)
    {
        List<DanmuInfo> timepostList = new ArrayList<>();
        for (int i = 0; i < danmuInfoList.size(); i++)
        {

            if (danmuInfoList.get(i).getTimpos() < timepos)
            {
                timepostList.add(danmuInfoList.get(i));
                danmuInfoList.remove(i);
            }
        }
        return danmuInfoList;
    }


    //****************webscoket

    private MySocketConnection mMySocketConnection;

    private Timer timer = new Timer();
    private TimerTask webSocketTask;

    private void initWebSocket()
    {
        webSocketTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (null != mMySocketConnection && mMySocketConnection.isConnected())
                {

                    mMySocketConnection.sendTextMessage("ping");
                }
            }
        };
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mMySocketConnection = MySocketConnection.getInstance();
                mMySocketConnection.setSocketListener(new SocketListener()
                {
                    @Override
                    public void OnOpend()
                    {
                        timer.schedule(webSocketTask, 0, 30000);
                    }

                    @Override
                    public void OnPushMsg(String message)
                    {

                        if (!TextUtils.isEmpty(message))
                        {
                            try
                            {
                                DanmuInfo chatInfo = new DanmuInfo(new JSONObject(message));

                                if (null != chatInfo)
                                {
                                    String action = chatInfo.getAction();
                                    String data = chatInfo.getData();
                                    String type = chatInfo.getType();
                                    String style = chatInfo.getStyle();

                                    String color = "#0000FF";

                                    if (!TextUtils.isEmpty(style))
                                    {
                                        color = style.split(",")[0];
                                    }

                                    if ("say".equals(type))
                                    {
                                        barrageView.addBarrage(new Barrage(data, Color.parseColor(color)));
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
                    public void OnError(String msg)
                    {
                        Message mMessage = new Message();
                        mMessage.obj = msg;
                        mMessage.what = SHOW_TOAST;
                        if (null != mHandler) mHandler.sendMessageDelayed(mMessage, 10 * 1000);


                    }
                });

                String wsUri = ConfigManager.instance().getChatUrl() + "/" + ConfigManager.instance().getUniqueCode() + "?co_biz=video&biz_id=" +
                        biz_id;


                if (null != mMySocketConnection) mMySocketConnection.startConnection(wsUri);

            }
        }).start();
    }


}
