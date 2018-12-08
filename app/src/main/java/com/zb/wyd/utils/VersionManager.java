package com.zb.wyd.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;


import com.zb.wyd.R;
import com.zb.wyd.activity.DomainNameActivity;
import com.zb.wyd.entity.VersionInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.BaiduHandler;
import com.zb.wyd.json.VersionInfoHandler;
import com.zb.wyd.listener.MyOnClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VersionManager implements IRequestListener
{
    private Context mContext;

    // 返回的安装包url
    private String apkUrl = "";

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/wyd/";

    private static final String saveFileName = savePath + "jlsp.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 0x01;
    private static final int DOWN_OVER = 0x02;
    private static final int REQUEST_SUCCESS = 0x03;
    private static final int REQUEST_FAIL = 0x04;
    private static final int BATDU_SUCCESS = 0x05;
    private static final String GET_VERSION = "get_version";
    private static final String GET_BAIDU = "get_baidu";
    private static final String TEST_DOMAINNAME = "test_domainname";

    private static final int TEST_DOMAINNAME_FAIL = 0x06;
    private static final int TEST_DOMAINNAME_SUCCESS = 0x07;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    if (null != downloadDialog)
                    {
                        downloadDialog.dismiss();
                    }


                    installApk();
                    break;
                case REQUEST_SUCCESS:

                    VersionInfoHandler mVersionInfoHandler = (VersionInfoHandler) msg.obj;
                    VersionInfo mVersionInfo = mVersionInfoHandler.getVersionInfo();


                    if (null != mVersionInfo)
                    {
                        ConfigManager.instance().setSystemEmail(mVersionInfo.getEmai());
                        ConfigManager.instance().setSystemQq(mVersionInfo.getQq());
                        ConfigManager.instance().setBgLogin(mVersionInfo.getBg_login());
                        ConfigManager.instance().setBgStartup(mVersionInfo.getBg_startup());
                        ConfigManager.instance().setCrossfire(mVersionInfo.getCrossfire());
                        ConfigManager.instance().setRegClosed(mVersionInfo.isReg_closed());
                        ConfigManager.instance().setIpLookUp(mVersionInfo.getIplookup());
                        ConfigManager.instance().setUploadUrl(mVersionInfo.getUpload());
                        ConfigManager.instance().setChatUrl(mVersionInfo.getChat());
                        ConfigManager.instance().setUploadDyUrl(mVersionInfo.getUpload_dy());
                        ConfigManager.instance().setUploadImgUrl(mVersionInfo.getUpload_img());

                        if (!StringUtils.stringIsEmpty(mVersionInfo.getText()))
                        {
                            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(mVersionInfo.getText());
                        }

                        showNoticeDialog(mVersionInfo);
                    }

                    break;

                case REQUEST_FAIL:
                    //                    Map<String, String> valuePairs = new HashMap<>();
                    //                    DataRequest.instance().request(mContext, "https://www.baidu.com", VersionManager.this, HttpRequest.GET, GET_BAIDU, valuePairs,
                    //                            new BaiduHandler());
                    break;

                case BATDU_SUCCESS:
                    BaiduHandler mResultHandler = (BaiduHandler) msg.obj;
                    String content = mResultHandler.getContent();

                    if (content.contains("baidu.com"))
                    {

                        String crossfire = ConfigManager.instance().getCrossfire();
                        if (TextUtils.isEmpty(crossfire))
                        {
                            setDomainName();
                        }
                        else
                        {
                            domianName = crossfire.split(";");
                            testDomainName();
                        }

                    }
                    break;

                case TEST_DOMAINNAME_SUCCESS:
                    ConfigManager.instance().setDomainName(mDomainName);
                    break;

                case TEST_DOMAINNAME_FAIL:
                    testDomainName();
                    break;
            }
        }

    };

    private String domianName[];
    private int p = 0;

    private String mDomainName;

    private void testDomainName()
    {
        LogUtil.e("DomainName", "PPPPPPPPPPPPPPPP->" + p);
        if (p < domianName.length)
        {
            mDomainName = domianName[p];
            p++;

            Map<String, String> valuePairs1 = new HashMap<>();
            DataRequest.instance().request(mContext, Urls.getVersionUrl(), VersionManager.this, HttpRequest.GET, TEST_DOMAINNAME, valuePairs1, new VersionInfoHandler());
        }
        else
        {
            setDomainName();
        }
    }

    private void setDomainName()
    {
        ConfigManager.instance().setSystemEmail("xjshangmen@gmail.com");
        DialogUtils.showToastDialog2Button(mContext, "不幸的告诉您，域名可能已被封，可发邮件到xjshangmen@gmail.com获取最新地址并进行设置操作。", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mContext.startActivity(new Intent(mContext, DomainNameActivity.class));
            }
        });
    }

    public VersionManager(Context context)
    {
        this.mContext = context;
    }


    public void init()
    {
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(mContext, Urls.getVersionUrl(), this, HttpRequest.GET, GET_VERSION, valuePairs, new VersionInfoHandler());
    }

    private void showNoticeDialog(final VersionInfo mVersionBean)
    {

        if (mVersionBean.getVersion().compareTo(APPUtils.getVersionName(mContext)) > 0)
        {
            apkUrl = mVersionBean.getLink();
            DialogUtils.showVersionUpdateDialog(mContext, mVersionBean.getVersion_desc(), new MyOnClickListener.OnSubmitListener()
            {

                @Override
                public void onSubmit(String content)
                {
                    if ("1".equals(content))
                    {
                        showDownloadDialog();
                    }
                    else if("3".equals(content))
                    {
                        Uri uri = Uri.parse(apkUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        if ("1".equals(mVersionBean.getForcedup()))
                        {
                            System.exit(0);
                        }
                    }
                }
            });
        }
    }

    private void showDownloadDialog()
    {


        downloadDialog = new Dialog(mContext, R.style.dialogNoAnimation);
        downloadDialog.setCancelable(false);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null);
        downloadDialog.setContentView(view);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        //Dialog部分
        Window mWindow = downloadDialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.75);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setAttributes(lp);
        downloadDialog.setCancelable(false);
        downloadDialog.show();


        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists())
                {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do
                {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0)
                    {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                }
                while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     */

    private void downloadApk()
    {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


    /**
     * 安装apk
     */
    private void installApk()
    {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists())
        {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.zb.wyd.fileprovider", apkfile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
        else
        {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            mContext.startActivity(i);
        }

    }


    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity()
    {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (GET_VERSION.equals(action))
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
        else if (GET_BAIDU.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(BATDU_SUCCESS, obj));
            }
        }
        else if (TEST_DOMAINNAME.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(TEST_DOMAINNAME_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(TEST_DOMAINNAME_FAIL, resultMsg));
            }
        }
    }
}
