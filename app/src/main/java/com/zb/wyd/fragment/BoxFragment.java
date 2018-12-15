package com.zb.wyd.fragment;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.activity.H5VideoPlayActivity;
import com.zb.wyd.activity.LoginActivity;
import com.zb.wyd.activity.PhotoDetailActivity;
import com.zb.wyd.activity.PhotoListActivity;
import com.zb.wyd.activity.VideoPlayActivity;
import com.zb.wyd.activity.VidoeListActivity;
import com.zb.wyd.entity.VideoInfo;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 描述：一句话简单描述
 */
public class BoxFragment extends BaseFragment implements View.OnClickListener
{


    private View rootView = null;

    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_box, null);
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

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {
        mWebView = (WebView) rootView.findViewById(R.id.mWebView);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSService(), "GP");
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

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {
        mWebView.loadUrl(Urls.getBoxUrl());
    }


    public  void updateView()
    {
        mWebView.loadUrl(Urls.getBoxUrl());
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onClick(View v)
    {

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
            ToastUtil.show(getActivity(), "内容已复制到剪贴板");
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
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
                startActivity(new Intent(getActivity(), H5VideoPlayActivity.class).putExtras(b));
            }
            else
            {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

        }

        @JavascriptInterface
        public void videolist(String cat_id)
        {
            if (!TextUtils.isEmpty(cat_id))
                startActivity(new Intent(getActivity(), VidoeListActivity.class).putExtra("sort", "new").putExtra("cta_id", cat_id));
        }

        @JavascriptInterface
        public void photolist(String cat_id)
        {
            startActivity(new Intent(getActivity(), PhotoListActivity.class).putExtra("cat_id", cat_id));
        }

        @JavascriptInterface
        public void photoplay(String photo_id)
        {
            if (MyApplication.getInstance().isLogin())
            {
                startActivity(new Intent(getActivity(), PhotoDetailActivity.class).putExtra("biz_id", photo_id));
            }
            else
            {
                startActivity(new Intent(getActivity(), LoginActivity.class));
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
        public void shareImage(final String cover)
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
