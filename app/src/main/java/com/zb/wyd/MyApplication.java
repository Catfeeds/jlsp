package com.zb.wyd;

import android.app.Application;
import android.os.StrictMode;

import com.umeng.commonsdk.UMConfigure;
import com.zb.wyd.entity.NoticeInfo;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：一句话简单描述
 */
public class MyApplication extends Application
{
    private List<Integer> photoDataList = new ArrayList<>();
    private static MyApplication instance;
    private List<NoticeInfo> noticeList = new ArrayList<>();


    public static MyApplication getInstance()
    {
        return instance;
    }

    private String location;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        APPUtils.configImageLoader(getApplicationContext());
        ConfigManager.instance().init(this);
        initPhotoData();
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(true);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public List<Integer> getPhotoDataList()
    {
        return photoDataList;
    }

    private void initPhotoData()
    {
        for (int i = 0; i < 10000; i++)
        {
            int randmon = (int) (Math.random() * 200);
            photoDataList.add(randmon);
        }
    }

    public boolean isLogin()
    {
        if (StringUtils.stringIsEmpty(ConfigManager.instance().getUniqueCode()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static MyApplication getContext()
    {
        return instance;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }


    public List<NoticeInfo> getNoticeList()
    {
        return noticeList;
    }

    public void setNoticeList(List<NoticeInfo> noticeList)
    {
        this.noticeList = noticeList;
    }
}
