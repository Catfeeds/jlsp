package com.zb.wyd.utils;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;

import com.zb.wyd.activity.LiveActivity;
import com.zb.wyd.activity.WebViewActivity;
import com.zb.wyd.entity.SearchInfo;

//分发器
public class ItemDispenser
{

    private static void searchDispenser(Context context, SearchInfo searchInfo)
    {
        //直播，电影，自拍，抖音，小说，语音
        switch (Integer.parseInt(searchInfo.getCo_biz()))
        {
            //直播
            case 1:
                break;

            //电影
            case 2:
                break;

            //自拍
            case 3:
                break;
            //抖音
            case 5:
                break;

            //小说
            case 6:
                context.startActivity(new Intent(context, WebViewActivity.class).putExtra
                        (WebViewActivity.EXTRA_TITLE, "详情").putExtra(WebViewActivity
                        .IS_SETTITLE, true).putExtra(WebViewActivity.EXTRA_URL,searchInfo.getUrl()));
                break;

            //语音
            case 7:
                break;
            case 8:
                break;
        }
    }
}
