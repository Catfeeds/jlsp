package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.zb.wyd.R;
import com.zb.wyd.entity.PriceInfo;
import com.zb.wyd.entity.ShareInfo;
import com.zb.wyd.entity.SignInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.DyVideoStreamHandler;
import com.zb.wyd.json.LivePriceInfoHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.ShareInfoHandler;
import com.zb.wyd.json.SignInfoHandler;
import com.zb.wyd.json.VideoInfoListHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.listener.MyOnClickListener;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.DyVideoPlayer;
import com.zb.wyd.widget.VerticalSwipeRefreshLayout;
import com.zb.wyd.widget.list.refresh.PullToRefreshBase;
import com.zb.wyd.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 描述：一句话简单描述
 */
public class DyVideoActivity extends BaseActivity implements IRequestListener, PullToRefreshBase
        .OnRefreshListener<RecyclerView>, SwipeRefreshLayout.OnRefreshListener
{
    @BindView(R.id.swipeRefresh)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.iv_cover)
    ImageView ivCover;

    private int pn = 1;
    private int mRefreshStatus;

    private String videoUri;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView; //
    private List<VideoInfo> videoInfoList = new ArrayList<>();
    private final String GET_VIDEO_LIST = "get_douyin_list";

    private int love_postion;

    private String shareCnontent;
    private final String GET_SHARE = "GET_SHARE";
    private final String FAVORITE_LIKE = "favorite_like";
    private final String UN_FAVORITE_LIKE = "un_favorite_like";
    private final String GET_VIDEO_PRICE = "get_live_price";
    private final String GET_VIDEO_STREAM = "get_dy_video_stream";
    private final String BUY_VIDEO = "buy_live";
    private final String GET_TASK_SHARE = "GET_TASK_SHARE";
    private final int REQUEST_SUCCESS = 0x01;
    private final int REQUEST_FAIL = 0x02;
    private final int GET_VIDEO_PRICE_SUCCESS = 0x03;
    private final int FAVORITE_LIKE_SUCCESS = 0x08;
    private final int UN_FAVORITE_LIKE_SUCCESS = 0x09;
    private final int GET_SHARE_CODE = 0x11;
    private final int GET_SHARE_SUCCESS = 0x12;
    private final int GET_TASK_SHARE_CODE = 0x13;
    private final int GET_TASK_SHARE_SUCCESS = 0x14;
    private final int GET_VIDEO_STREAM_FAIL = 0X15;
    private static final int GET_VIDEO_LIST_SUCCESS = 0x21;
    private static final int SHARE_PHOTO_REQUEST_CODE = 0x91;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(DyVideoActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    DyVideoStreamHandler mVideoStreamHandler = (DyVideoStreamHandler) msg.obj;

                    if (null != mVideoStreamHandler.getUri())
                    {

                        for (int i = 0; i < videoInfoList.size(); i++)
                        {
                            if (mVideoStreamHandler.getBiz_id().equals(videoInfoList.get(i).getId
                                    ()))
                            {
                                videoInfoList.get(i).setHas_favorite(mVideoStreamHandler
                                        .getHas_favorite());
                                videoInfoList.get(i).setPay_for(mVideoStreamHandler.isPay_for());
                            }
                        }

                        videoUri = mVideoStreamHandler.getUri();
                        mAdapter.notifyDataSetChanged();

                        // playVideo(mVideoStreamHandler.getUri());
                    }
                    break;
                case GET_VIDEO_LIST_SUCCESS:
                    VideoInfoListHandler mVideoInfoListHandler = (VideoInfoListHandler) msg.obj;

                    if (pn == 1)
                    {
                        VideoInfo mVideoInfo = (VideoInfo) getIntent().getSerializableExtra("VideoInfo");
                        if (null != mVideoInfo)
                        {
                            videoInfoList.add(mVideoInfo);
                            for (int i = 0; i < mVideoInfoListHandler.getVideoInfoList().size();
                                 i++)
                            {
                                if (!mVideoInfo.getId().equals(mVideoInfoListHandler
                                        .getVideoInfoList().get(i).getId()))
                                {
                                    videoInfoList.add(mVideoInfoListHandler.getVideoInfoList()
                                            .get(i));
                                }
                            }
                        }
                        else
                        {
                            videoInfoList.addAll(mVideoInfoListHandler.getVideoInfoList());
                        }


                        if (!videoInfoList.isEmpty())
                        {
                            getVideoStream(videoInfoList.get(0).getId());
                        }


                    }
                    else
                    {
                        videoInfoList.addAll(mVideoInfoListHandler.getVideoInfoList());
                    }


                    mAdapter.notifyDataSetChanged();
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(DyVideoActivity.this, msg.obj.toString());
                    break;

                case GET_VIDEO_PRICE_SUCCESS:
                    LivePriceInfoHandler mLivePriceInfoHandler = (LivePriceInfoHandler) msg.obj;

                    PriceInfo mLivePriceInfo = mLivePriceInfoHandler.getLivePriceInfo();

                    if (null != mLivePriceInfo)
                    {
                        DialogUtils.showVideoPriceDialog(DyVideoActivity.this, mLivePriceInfo,
                                new View.OnClickListener()
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

                                    // buyVideo(mLivePriceInfo.getFinger());


                                }
                                else//去做任务
                                {
                                    //sendBroadcast(new Intent(MainActivity.TAB_TASK));
                                    startActivity(new Intent(DyVideoActivity.this, TaskActivity
                                            .class));
                                    finish();
                                }
                            }
                        }).show();
                    }
                    break;


                case FAVORITE_LIKE_SUCCESS:
                    //ToastUtil.show(DyVideoActivity.this, "收藏成功");

                    videoInfoList.get(love_postion).setHas_favorite("1");
                    mAdapter.notifyDataSetChanged();
                    //mCollectionIv.setEnabled(false);
                    break;

                case UN_FAVORITE_LIKE_SUCCESS:
                    videoInfoList.get(love_postion).setHas_favorite("0");
                    mAdapter.notifyDataSetChanged();
                    break;
                case GET_SHARE_CODE:
                    //  getShareUrl();
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
                        startActivityForResult(Intent.createChooser(intent1, "分享"),
                                SHARE_PHOTO_REQUEST_CODE);
                    }
                    break;

                case GET_TASK_SHARE_CODE:
                    getTaskShareUrl(mBiz_id);
                    break;

                case GET_TASK_SHARE_SUCCESS:
                    SignInfoHandler mSignInfoHandler = (SignInfoHandler) msg.obj;
                    SignInfo signInfo = mSignInfoHandler.getSignInfo();

                    if (null != signInfo)
                    {
                        String title = "分享成功";
                        String desc = signInfo.getVal() + "积分";
                        String task = "连续分享" + signInfo.getSeries() + "天";
                        DialogUtils.showTaskDialog(DyVideoActivity.this, title, desc, task);
                    }
                    break;

                case GET_VIDEO_STREAM_FAIL:
                    DialogUtils.showToastDialog2Button(DyVideoActivity.this, msg.obj.toString(),
                            "去做任务", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            //sendBroadcast(new Intent(MainActivity.TAB_TASK));
                            startActivity(new Intent(DyVideoActivity.this, TaskActivity.class));
                            finish();
                        }
                    }, new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            finish();
                        }
                    }).show();
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dy_video_play);
    }

    @Override
    protected void initEvent()
    {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
    }

    @Override
    protected void initViewData()
    {


        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount
                        () == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mAdapter = new MyAdapter(videoInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                //收藏
                love_postion = position;
                if ("1".equals(videoInfoList.get(position).getHas_favorite()))
                {
                    unFavoriteLike(videoInfoList.get(position).getId());
                }
                else
                {
                    favoriteLike(videoInfoList.get(position).getId());

                }


            }
        }, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                //分享
                getShareUrl(videoInfoList.get(position).getId());
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        initListener();

        VideoInfo mVideoInfo = (VideoInfo) getIntent().getSerializableExtra("VideoInfo");
        if (null != mVideoInfo)
        {
            ImageLoader.getInstance().displayImage(mVideoInfo.getCover(), ivCover);

        }

        if(ConfigManager.instance().getValid_vip())
        {
            ivCover.animate().alpha(0).setDuration(1000).start();
            loadData();
        }
        else
        {
            DialogUtils.showDyTipsDialog(this, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    ivCover.animate().alpha(0).setDuration(1000).start();
                    loadData();
                }
            }, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    startActivity(new Intent(DyVideoActivity.this, MemberActivity.class));
                    finish();


                }
            }, new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    finish();
                }
            }).show();
        }

    }

    private void loadData()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        getDouyin();
    }

    private String mBiz_id;

    private void getVideoStream(String biz_id)
    {
        this.mBiz_id = biz_id;
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        DataRequest.instance().request(DyVideoActivity.this, Urls.getDyVideoStreamUrl(), this,
                HttpRequest.GET, GET_VIDEO_STREAM, valuePairs, new DyVideoStreamHandler());
    }

    private void getShareUrl(String biz_id)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "douyin");
        DataRequest.instance().request(DyVideoActivity.this, Urls.getShareUrl(), this,
                HttpRequest.GET, GET_SHARE, valuePairs, new ShareInfoHandler());
    }

    private void getTaskShareUrl(String biz_id)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "douyin");
        DataRequest.instance().request(DyVideoActivity.this, Urls.getTaskShareUrl(), this,
                HttpRequest.GET, GET_TASK_SHARE, valuePairs, new SignInfoHandler());
    }

    private void initListener()
    {
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener()
        {
            @Override
            public void onInitComplete()
            {

            }

            @Override
            public void onPageRelease(boolean isNext, int position)
            {
                //   Log.e(TAG,"释放位置:"+position +" 下一页:"+isNext);
                int index = 0;
                if (isNext)
                {
                    index = 0;

                }
                else
                {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom)
            {
                //   Log.e(TAG,"选中位置:"+position+"  是否是滑动到底部:"+isBottom);
                // playVideo("");
                getVideoStream(videoInfoList.get(position).getId());
            }


            public void onLayoutComplete()
            {
                // playVideo("");
            }

        });
    }


    private void favoriteLike(String biz_id)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "douyin");
        DataRequest.instance().request(DyVideoActivity.this, Urls.getCollectionRequestUrl(),
                this, HttpRequest.POST, FAVORITE_LIKE, valuePairs, new ResultHandler());
    }

    private void unFavoriteLike(String biz_id)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "douyin");
        DataRequest.instance().request(DyVideoActivity.this, Urls.getFavoriteUnLikeUrl(), this,
                HttpRequest.POST, UN_FAVORITE_LIKE, valuePairs, new ResultHandler());
    }

    private void playVideo(String videoUri)
    {
        videoUri = "http://v1.du10010.com/2018/0728/2913465/playlist.m3u8";
        LogUtil.e("TAG", "videoUri--->" + videoUri);
        View itemView = mRecyclerView.getChildAt(0);
        final DyVideoPlayer videoView = itemView.findViewById(R.id.dy_player);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        //  final RelativeLayout rootView = itemView.findViewById(R.id.root_view);


    }


    private void releaseVideo(int index)
    {
        View itemView = mRecyclerView.getChildAt(0);
        if (null != itemView)
        {
            final DyVideoPlayer videoView = itemView.findViewById(R.id.dy_player);
            final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
            videoView.release();
            imgThumb.animate().alpha(1).start();
        }
    }

    private void getDouyin()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "20");
        DataRequest.instance().request(this, Urls.getDouyinListUrl(), this, HttpRequest.GET,
                GET_VIDEO_LIST, valuePairs, new VideoInfoListHandler());
    }

    private void getLivePrice()
    {
        if (!TextUtils.isEmpty(mBiz_id))
        {
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("biz_id", mBiz_id);
            valuePairs.put("co_biz", "douyin");
            DataRequest.instance().request(DyVideoActivity.this, Urls.getVideoPriceUrl(), this,
                    HttpRequest.POST, GET_VIDEO_PRICE, valuePairs, new LivePriceInfoHandler());
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        //        if (orientationUtils != null)
        //            orientationUtils.releaseListener();

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
            else if ("1102".equals(resultCode))
            {
                hideProgressDialog();
                mHandler.sendMessage(mHandler.obtainMessage(GET_VIDEO_STREAM_FAIL, resultMsg));
            }
        }
        if (GET_VIDEO_LIST.equals(action))
        {
            hideProgressDialog();
            mSwipeRefreshLayout.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_VIDEO_LIST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
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
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        videoInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        getDouyin();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        getDouyin();
    }

    @Override
    public void onRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            pn = 1;
            mRefreshStatus = 0;
            getDouyin();

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


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
    {

        private List<VideoInfo> videoInfoList;

        private MyItemClickListener mLinener1;
        private MyItemClickListener mLinener2;

        public MyAdapter(List<VideoInfo> videoInfoList, MyItemClickListener mLinener1,
                         MyItemClickListener mLinener2)
        {
            this.videoInfoList = videoInfoList;
            this.mLinener1 = mLinener1;
            this.mLinener2 = mLinener2;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                    .item_dy_video_play, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            VideoInfo videoInfo = videoInfoList.get(position);
            holder.tv_title.setText(videoInfo.getV_name());
            holder.love_count.setText(videoInfo.getFavour_count());

            if(!videoInfo.isPay_for())
            {
                holder.tv_tips.setText("本次观看免费");
            }
            else
            {
                holder.tv_tips.setText("本次消费" + videoInfo.getCash() + "积分");
            }

            UserInfo userInfo = videoInfoList.get(position).getUserInfo();
            if (null != userInfo)
            {
                ImageLoader.getInstance().displayImage(userInfo.getUface(), holder.img_head);
            }
            ImageLoader.getInstance().displayImage(videoInfo.getCover(), holder.img_thumb);
            // holder.img_thumb.setImageResource(videoInfoList.get(.));
            // holder .videoView.setUp(videoList[position%5], false, "");


            if ("1".equals(videoInfo.getHas_favorite()))
            {
                holder.img_love.setSelected(true);

            }
            else
            {
                holder.img_love.setSelected(false);
            }


            holder.img_love.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mLinener1.onItemClick(v, position);
                }
            });


            holder.img_share.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mLinener2.onItemClick(v, position);
                }
            });

            holder.videoView.setIsTouchWiget(false);
            holder.videoView.setVideoAllCallBack(new VideoAllCallBack()
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
                    hideProgressDialog();
                    holder.img_thumb.animate().alpha(0).setDuration(200).start();
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
                    holder.videoView.startPlayLogic();
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
            holder.videoView.setUp(videoUri, false, "");
            holder.videoView.startPlayLogic();
        }

        @Override
        public int getItemCount()
        {
            return videoInfoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            ImageView img_love;
            ImageView img_share;
            CircleImageView img_head;
            TextView love_count;
            TextView tv_title;
            TextView tv_tips;
            ImageView img_thumb;
            DyVideoPlayer videoView;
            //  RelativeLayout rootView;

            public ViewHolder(View itemView)
            {
                super(itemView);
                img_love = itemView.findViewById(R.id.iv_love);
                img_share = itemView.findViewById(R.id.iv_share);
                img_head = itemView.findViewById(R.id.iv_user_pic);
                love_count = itemView.findViewById(R.id.tv_love_count);
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_tips = itemView.findViewById(R.id.tv_tips);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.dy_player);
                // rootView = itemView.findViewById(R.id.root_view);
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
}
