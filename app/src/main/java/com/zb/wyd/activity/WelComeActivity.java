package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.entity.LocationInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.LocationInfoHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;

/**
 * 描述：一句话简单描述
 */
public class WelComeActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_bg)
    ImageView      ivBg;
    @BindView(R.id.tv_tips)
    TextView       tvTips;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.tv_email)
    TextView       tvEmail;

    private int time = 3;
    private int count;
    private static final String TEST_DOMAINNAME = "test_domainname";

    private static final int UPDATE_CODE_VIEW     = 0X03;
    private static final int GET_LOCATION_CODE    = 0X15;
    private static final int GET_LOCATION_FAIL    = 0X16;
    private static final int GET_LOCATION_SUCCESS = 0x17;

    private static final int TEST_DOMAINNAME_FAIL    = 0x06;
    private static final int TEST_DOMAINNAME_SUCCESS = 0x07;

    private String[]     parkedDomainArr = {"0816data.com", "hedayishu.com", "dayundao.com"};
    private List<String> domainNameList  = new ArrayList<>();


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(WelComeActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case UPDATE_CODE_VIEW:
                    count++;

                    int result = time - count;

                    if (result >= 0)
                    {
                        tvTips.setText(result + "s");
                        mHandler.sendEmptyMessageDelayed(UPDATE_CODE_VIEW, 1000);
                    }
                    else
                    {
                        startActivity(new Intent(WelComeActivity.this, HostActivity.class));
                        finish();
                    }

                    break;
                case GET_LOCATION_CODE:
                    getLocation();

                    break;

                case GET_LOCATION_SUCCESS:
                    LocationInfoHandler mLocationInfoHandler = (LocationInfoHandler) msg.obj;
                    LocationInfo locationInfo = mLocationInfoHandler.getLocationInfo();
                    String location = locationInfo.getProv() + "," + locationInfo.getCity() + "," + locationInfo.getDistrict();
                    MyApplication.getInstance().setLocation(location);

                    break;

                case GET_LOCATION_FAIL:
                    break;

                case TEST_DOMAINNAME_SUCCESS:
                    ConfigManager.instance().setDomainName(mDomainName);
                    tvTips.setVisibility(View.VISIBLE);
                    tvTips.setText("3s");
                    mHandler.sendEmptyMessageDelayed(UPDATE_CODE_VIEW, 1000);
                    break;

                case TEST_DOMAINNAME_FAIL:
                    LogUtil.e("TAG", "不可用域名:" + mDomainName);
                    testDomainName();
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
        setContentView(R.layout.activity_welcome);
        StatusBarUtil.transparencyBar(WelComeActivity.this);
        StatusBarUtil.StatusBarLightMode(WelComeActivity.this, false);
    }

    @Override
    protected void initEvent()
    {

    }

    private void getLocation()
    {
        String url = ConfigManager.instance().getIpLookUp();

        if (!TextUtils.isEmpty(url))
        {
            url = url + "?auth=";
            Map<String, String> valuePairs = new HashMap<>();
            DataRequest.instance().request(this, url, this, HttpRequest.GET, "GET_LOCATION", valuePairs, new LocationInfoHandler());
        }

    }

    private String domianNameArr[];

    @Override
    protected void initViewData()
    {

        mHandler.sendEmptyMessage(GET_LOCATION_CODE);
        if (!TextUtils.isEmpty(ConfigManager.instance().getSystemEmail()))
        {
            tvEmail.setText("防丢邮箱,发邮件到" + ConfigManager.instance().getSystemEmail() + "获取最新地址");
        }


        if (!TextUtils.isEmpty(ConfigManager.instance().getBgStartup()))
        {
            ImageLoader.getInstance().displayImage(ConfigManager.instance().getBgStartup(), ivBg);
        }
        initDomainName();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void initDomainName()
    {
        boolean test = false;
        if (test)
        {
            mDomainName = "https://rzjyzy.com";
            mHandler.sendEmptyMessage(TEST_DOMAINNAME_SUCCESS);
        }
        else
        {
            String crossfire = ConfigManager.instance().getCrossfire();
            if (!TextUtils.isEmpty(crossfire))
            {
                domianNameArr = crossfire.split(";");
            }
            String myDomainName = ConfigManager.instance().getZydDomainName();

            if (!"".equals(myDomainName))
            {
                domainNameList.add(myDomainName);
            }
            if (null != domianNameArr)
            {
                for (int i = 0; i < domianNameArr.length; i++)
                {
                    domainNameList.add(domianNameArr[i]);
                }


                for (int i = 0; i < parkedDomainArr.length; i++)
                {
                    domainNameList.add("https://" + getRandomString() + "." + parkedDomainArr[i]);
                }
            }
            else
            {
                for (int i = 0; i < parkedDomainArr.length; i++)
                {
                    domainNameList.add("https://" + getRandomString() + "." + parkedDomainArr[i]);
                }
            }
            mHandler.sendEmptyMessage(TEST_DOMAINNAME_FAIL);
        }
    }


    int p = 0;
    private String mDomainName;

    private void testDomainName()
    {
        LogUtil.e("DomainName", "PPPPPPPPPPPPPPPP->" + p);
        if (p < domainNameList.size())
        {
            mDomainName = domainNameList.get(p);
            p++;

            String url = mDomainName + "/ping.js?auth=";
            Map<String, String> valuePairs1 = new HashMap<>();
            DataRequest.instance().request(this, url, WelComeActivity.this, HttpRequest.GET, TEST_DOMAINNAME, valuePairs1, new ResultHandler());
        }
        else
        {

            setDomainName();

        }
    }

    private void setDomainName()
    {
        ConfigManager.instance().setSystemEmail("xjshangmen@gmail.com");
        DialogUtils.showToastDialog2Button(WelComeActivity.this, "不幸的告诉您，域名可能已被封，可发邮件到xjshangmen@gmail.com获取最新地址并进行设置操作。", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(WelComeActivity.this, DomainNameActivity.class));
                finish();
            }
        });
    }


    public static String getRandomString()
    {
        Random rand = new Random();

        int length = rand.nextInt(2) + 3;
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiop1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i)
        {
            //产生0-61的数字
            int number = random.nextInt(36);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler.hasMessages(GET_LOCATION_CODE))
        {
            mHandler.removeMessages(GET_LOCATION_CODE);
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if ("GET_LOCATION".equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_LOCATION_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_LOCATION_FAIL, resultMsg));
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
