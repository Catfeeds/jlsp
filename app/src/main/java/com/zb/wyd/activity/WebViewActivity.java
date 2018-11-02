package com.zb.wyd.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.StringUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * DESC: H5界面跳转
 */
public class WebViewActivity extends Activity
{
    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String IS_SETTITLE = "isSetTitle";
    private WebView mWebView;
    private String mUrl;
    private boolean isSetTitle;
    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mSubmitTv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
        initViewData();
        initEvent();

    }

    protected void initView()
    {


        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(WebViewActivity.this, false);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mSubmitTv = (TextView) findViewById(R.id.tv_submit);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mWebView = (WebView) findViewById(R.id.mWebView);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSService(), "native");

        String type = getIntent().getStringExtra("TYPE");
        if ("CUSTOMER".equals(type))
        {
            mSubmitTv.setText("联系客服");
            mSubmitTv.setVisibility(View.VISIBLE);
        }
        else
        {
            mSubmitTv.setVisibility(View.GONE);
        }


        mWebView.setWebViewClient(new WebViewClient()
                                  {
                                      @Override
                                      public void onPageFinished(WebView view, String url)
                                      {
                                          super.onPageFinished(view, url);
                                          if (mWebView.getContentHeight() != 0)
                                          {
                                              // 网页显示完成
                                          }
                                      }

                                      @Override
                                      public void onLoadResource(WebView view, String url)
                                      {
                                          super.onLoadResource(view, url);
                                      }

                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url)
                                      {
                                          if (url != null && url.startsWith("appay"))
                                          {
                                              return false;
                                          }
                                          if (url.startsWith("http") || url.startsWith("https"))
                                          {
                                              return super.shouldOverrideUrlLoading(view, url);
                                          }
                                          //                                          else
                                          //                                          {
                                          //                                              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                          //                                              startActivity(intent);
                                          //                                              finish();
                                          //                                              return true;
                                          //                                          }
                                          return true;
                                      }

                                      @Override
                                      public WebResourceResponse shouldInterceptRequest(WebView view, String url)
                                      {
                                          if (url.startsWith("http") || url.startsWith("https"))
                                          { //http和https协议开头的执行正常的流程
                                              return super.shouldInterceptRequest(view, url);
                                          }
                                          else
                                          {  //其他的URL则会开启一个Acitity然后去调用原生APP
                                              Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                              startActivity(in);
                                              return null;
                                          }
                                      }

                                  }

        );
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
                super.onReceivedTitle(view, title);

                if (!isSetTitle) mTitleTv.setText(title);
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed)
            {
                super.onReceivedTouchIconUrl(view, url, precomposed);
                //SLog.debug("onReceivedTouchIconUrl:" + url);
            }
        });

        mWebView.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
            {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void initViewData()
    {
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        if(!mUrl.contains("?auth"))
        {
            mUrl =mUrl + "?auth=" + ConfigManager.instance().getUniqueCode() + "&mobile_id=" + APPUtils.getDeviceId(this) + "&device=and";
        }

        LogUtil.e("TAG", "url-->" + mUrl);
        isSetTitle = getIntent().getBooleanExtra(IS_SETTITLE, true);

        if (isSetTitle)
        {
            mTitleTv.setText(getIntent().getStringExtra(EXTRA_TITLE));
        }
        if (!StringUtils.stringIsEmpty(mUrl))
        {
            mWebView.loadUrl(mUrl);
        }
    }


    private void initEvent()
    {
        mBackIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mWebView.canGoBack())
                {
                    mWebView.goBack();
                }
                else
                {
                    finish();
                }
            }
        });

        mSubmitTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(WebViewActivity.this, QuestionActivity.class));
            }
        });
    }

    public class JSService
    {
        @JavascriptInterface
        public void onPayDone()
        {
        }

        public void onPayFail()
        {
        }

        @JavascriptInterface
        public void onClosed()
        {
            finish();
        }

        @JavascriptInterface
        public void onBuy(String groupId, String boxId, String productType)
        {

        }


        @JavascriptInterface
        public void openOuterWeb(String url)
        {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        @JavascriptInterface
        public void copy(String text)
        {
            ToastUtil.show(WebViewActivity.this, "内容已复制到剪贴板");
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(text);
        }

        @JavascriptInterface
        public void onQQ(String qq)
        {
            if (!TextUtils.isEmpty(qq))
            {
                String qqurl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqurl)));
            }
        }

        @JavascriptInterface
        public void onSubscriptionSuccess(String groupId, String roomId)
        {
        }

        @JavascriptInterface
        public void videoplay(String video_id)
        {
            if (MyApplication.getInstance().isLogin())
            {
                VideoInfo mVideoInfo = new VideoInfo();
                mVideoInfo.setId(video_id);
                mVideoInfo.setV_name("");
                Bundle b = new Bundle();
                b.putSerializable("VideoInfo", mVideoInfo);
                startActivity(new Intent(WebViewActivity.this, VideoPlayActivity.class).putExtras(b));
                finish();
            }
            else
            {
                startActivity(new Intent(WebViewActivity.this, LoginActivity.class));
            }

        }

        @JavascriptInterface
        public void videolist(String cat_id)
        {
            if (!TextUtils.isEmpty(cat_id))
                startActivity(new Intent(WebViewActivity.this, VidoeListActivity.class).putExtra("sort", "new").putExtra("cta_id", cat_id));
        }

        @JavascriptInterface
        public void photolist(String cat_id)
        {
            startActivity(new Intent(WebViewActivity.this, PhotoListActivity.class).putExtra("cat_id", cat_id));
            finish();
        }

        @JavascriptInterface
        public void photoplay(String photo_id)
        {
            if (MyApplication.getInstance().isLogin())
            {
                startActivity(new Intent(WebViewActivity.this, PhotoDetailActivity.class).putExtra("biz_id", photo_id));
                finish();
            }
            else
            {
                startActivity(new Intent(WebViewActivity.this, LoginActivity.class));
            }
        }


        @JavascriptInterface
        public void share(String title, String url, String cover)
        {
            String shareCnontent = title + ":" + url;
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.putExtra(Intent.EXTRA_TEXT, shareCnontent);
            intent1.setType("text/plain");
            startActivity(Intent.createChooser(intent1, "分享"));
        }

        @JavascriptInterface
        public void shareImage(String cover)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {

                    try
                    {
                        Bitmap mBitmap = getBitmap(cover);

                        if (null != mBitmap)
                        {
                            File file = bitMap2File(mBitmap);

                            if (file != null && file.exists() && file.isFile())
                            {
                                //由文件得到uri
                                Uri imageUri = Uri.fromFile(file);
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.setType("image/*");
                                startActivity(Intent.createChooser(shareIntent, "分享图片"));
                            }
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }).

                    start();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            if (mWebView.canGoBack())
            {
                mWebView.goBack();
            }
            else
            {
                return super.onKeyDown(keyCode, event);
            }

            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }

    private Bitmap getBitmap(String path) throws IOException
    {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200)
        {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;

    }

    public File bitMap2File(Bitmap bitmap)
    {


        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            path = Environment.getExternalStorageDirectory() + File.separator;//保存到sd根目录下
        }


        //        File f = new File(path, System.currentTimeMillis() + ".jpg");
        File f = new File(path, "share" + ".jpg");
        if (f.exists())
        {
            f.delete();
        }
        try
        {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return f;
        }
    }
}
