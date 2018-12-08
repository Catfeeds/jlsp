package com.zb.wyd.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.zb.wyd.R;
import com.zb.wyd.entity.VideoUplaodResult;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.PhotoInfoHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.VideoUploadResultHandler;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.video.view.NormalProgressDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RecordActivity extends BaseActivity implements IRequestListener
{
    private JCameraView jCameraView;
    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    private boolean granted = false;
    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_record);
        if (Build.VERSION.SDK_INT >= 19)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View
                    .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View
                    .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        else
        {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void initEvent()
    {

    }



    @Override
    protected void initViewData()
    {
        jCameraView = (JCameraView) findViewById(R.id.jcameraview);

        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File
                .separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);

        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_POOR);
        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener()
        {
            @Override
            public void onError()
            {
                //打开Camera失败回调
                Log.i("CJT", "open camera error");
            }

            @Override
            public void AudioPermissionError()
            {
                //没有录取权限回调
                Log.i("CJT", "AudioPermissionError");
                getPermissions();
            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener()
        {
            @Override
            public void captureSuccess(Bitmap bitmap)
            {
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame)
            {
                videoPath = url;
                //获取视频路径
                Log.i("CJT", "url = " + url);

                DialogUtils.showToastDialog2Button(RecordActivity.this, "是否立即发布", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        NormalProgressDialog.showLoading(RecordActivity.this, "视频发布中...", false);
                        pushVideo(videoPath);
                    }
                });
            }
            //@Override
            //public void quit() {
            //    (1.1.9+后用左边按钮的点击事件替换)
            //}
        });

        //左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener()
        {
            @Override
            public void onClick()
            {
                RecordActivity.this.finish();
            }
        });
        //右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener()
        {
            @Override
            public void onClick()
            {

            }
        });

        //6.0动态权限获取
        getPermissions();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (granted) {
            jCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        jCameraView.stopRecored();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                granted = true;
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(RecordActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    jCameraView.onResume();
                }else{
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }



    private void pushVideo(String filePath)
    {
        File mFile = new File(filePath);
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(RecordActivity.this, ConfigManager.instance().getUploadDyUrl(), this, HttpRequest.UPLOAD_VIDEO, UPLOAD_VIDEO, valuePairs,
                mFile, new VideoUploadResultHandler());
    }

    public void dyTougao(String host, String url)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("host", host);
        valuePairs.put("uri", url);
        DataRequest.instance().request(RecordActivity.this, Urls.getDyTougaoUrl(), this, HttpRequest.POST, DOUYING_TOUGAO_REQUEST,
                valuePairs, new ResultHandler());
    }

    private VideoUplaodResult mVideoUplaodResult;
    private String videoPath;
    private static final String UPLOAD_VIDEO = "upload_video";
    private static final String DOUYING_TOUGAO_REQUEST = "douying_tougao_request";
    private static final int DOUYING_TOUGAO_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int REQUEST_SUCCESS = 0x03;
    private static final int DOUYING_TOUGAO_FAIL = 0x04;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(RecordActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case REQUEST_FAIL:
                    ToastUtil.show(RecordActivity.this, msg.obj.toString());
                    DialogUtils.showToastDialog2Button(RecordActivity.this, "发布失败是否重新发布", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            NormalProgressDialog.showLoading(RecordActivity.this, "视频发布中...", false);
                            pushVideo(videoPath);
                        }
                    });

                    break;

                case REQUEST_SUCCESS:
                    ToastUtil.show(RecordActivity.this, "发布成功");

                    VideoUploadResultHandler mVideoUploadResultHandler = (VideoUploadResultHandler) msg.obj;

                    mVideoUplaodResult = mVideoUploadResultHandler.getVideoUplaodResult();
                    if (null != mVideoUplaodResult)
                    {
                        dyTougao(mVideoUplaodResult.getHost(), mVideoUplaodResult.getSavepath() + mVideoUplaodResult.getSavename());
                    }

                    break;

                case DOUYING_TOUGAO_SUCCESS:

                    startActivity(new Intent(RecordActivity.this, WebViewActivity.class).putExtra(WebViewActivity.EXTRA_TITLE, "我的作品").putExtra
                            (WebViewActivity.IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL, Urls.getMyWorkUrl(RecordActivity.this)));
                    finish();
                    break;
                case DOUYING_TOUGAO_FAIL:
                    DialogUtils.showToastDialog2Button(RecordActivity.this, "投稿失败是否重新投稿", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            NormalProgressDialog.showLoading(RecordActivity.this, "投稿中...", false);
                            if (null != mVideoUplaodResult)
                            {
                                dyTougao(mVideoUplaodResult.getHost(), mVideoUplaodResult.getSavepath() + mVideoUplaodResult.getSavename());
                            }
                        }
                    });


                    break;


            }
        }
    };


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        NormalProgressDialog.stopLoading();
        if (UPLOAD_VIDEO.equals(action))
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
        else if (DOUYING_TOUGAO_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(DOUYING_TOUGAO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(DOUYING_TOUGAO_FAIL, resultMsg));
            }
        }
    }


}
