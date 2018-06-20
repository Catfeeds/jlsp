package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.zb.wyd.R;
import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.LiveInfoHandler;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.LiveVideoPlayer;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 描述：一句话简单描述
 */
public class LiveActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.detail_player)
    LiveVideoPlayer videoPlayer;

    //private OrientationUtils orientationUtils;


    private static final String      GET_LIVE_STREAM = "get_live_stream";
    private static final int         REQUEST_SUCCESS = 0x01;
    private static final int         REQUEST_FAIL    = 0x02;
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler        = new BaseHandler(LiveActivity.this)
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
                        String source1 = "http://vod3.vxinda.com/20180523/1381/playlist.m3u8";

                        String source2 = "rtmp://live.hkstv.hk.lxdns.com/live/hks";

                        String rtsp = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";

                        String flv = "rtmp://stream.pull.dianxunba.com/vod/cdb485af5d15e049ab86fac38d6d83a4";
                        String uri = mLiveInfo.getUri();
                        LogUtil.e("TAG", uri);
                        videoPlayer.setUp(uri, false, "测试视频");
                        videoPlayer.startPlayLogic();
                    }
                    break;


                case REQUEST_FAIL:
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live);
    }

    @Override
    protected void initEvent()
    {
    }

    @Override
    protected void initViewData()
    {

        //增加封面
        //        ImageView imageView = new ImageView(this);
        //        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //        imageView.setImageResource(R.drawable.ic_launcher);
        //        videoPlayer.setThumbImageView(imageView);
        //增加title
//        videoPlayer.getTitleTextView().setVisibility(View.GONE);
//        //设置返回键
//        videoPlayer.getBackButton().setVisibility(View.GONE);
//        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        videoPlayer.setIsTouchWigetFull(true);


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
        loadData();
    }


    private void loadData()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", getIntent().getStringExtra("biz_id"));
        DataRequest.instance().request(LiveActivity.this, Urls.getLiveStream(), this, HttpRequest.POST, GET_LIVE_STREAM, valuePairs,
                new LiveInfoHandler());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
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
