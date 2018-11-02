package com.zb.wyd.utils;

/**
 * URL管理类
 *
 * @since[产品/模块版本]
 * @seejlj
 */
public class Urls
{
    //UI https://lanhuapp.com/web/#/item/board?pid=3f958e36-6866-49c2-9934-24a0edd9ca7c
    //http://api.crap.cn/project.do#/152894835244607000287/interface/list/152894841894709000288
    public static final String HTTP_IP = "http://www.883974.com/";

    //public String  ConfigManager.instance().getDomainName() = ConfigManager.instance().getDomainName();

    //获取版本信息
    public static String getVersionUrl()
    {
        return ConfigManager.instance().getDomainName() + "/index/config";
    }


    //用戶登录
    public static String getLoginUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/login";
    }


    //用戶注册
    public static String getRegisterUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/register";
    }


    //限时免费
    public static String getFreeLive()
    {
        return ConfigManager.instance().getDomainName() + "/live/free";
    }

    //新晋主播
    public static String getNewLive()
    {
        return ConfigManager.instance().getDomainName() + "/live/index";
    }

    //点播
    public static String getVideoListUrl()
    {
        return ConfigManager.instance().getDomainName() + "/video/list";
    }

    //抖音
    public static String getDouyinListUrl()
    {
        return ConfigManager.instance().getDomainName() + "/douyin/list";
    }

    //获取真实的直播地址
    public static String getLiveStreamUrl()
    {
        return ConfigManager.instance().getDomainName() + "/live/stream";
    }

    //获取直播价格
    public static String getLivePriceUrl()
    {
        return ConfigManager.instance().getDomainName() + "/live/get_price";
    }

    //购买直播
    public static String getBuyLiveUrl()
    {
        return ConfigManager.instance().getDomainName() + "/fortune/buy";
    }

    //统计时长
    public static String getStatisticsUrl()
    {
        return ConfigManager.instance().getDomainName() + "/data/statistics";
    }

    //获取广告
    public static String getAdListUrl()
    {
        return ConfigManager.instance().getDomainName() + "/position/get_ad";
    }

    //获取在线人数
    public static String getOnlinerUrl()
    {
        return ConfigManager.instance().getDomainName() + "/data/get_onliner";
    }


    //获取点播流
    public static String getVideoStreamUrl()
    {
        return ConfigManager.instance().getDomainName() + "/video/stream";
    }

    //获取点播流
    public static String getDyVideoStreamUrl()
    {
        return ConfigManager.instance().getDomainName() + "/douyin/stream";
    }


    //获取点播分类
    public static String getVideoCataUrl()
    {
        return ConfigManager.instance().getDomainName() + "/video/get_cata";
    }


    //图片分类
    public static String getPhotoCataUrl()
    {
        return ConfigManager.instance().getDomainName() + "/photo/get_cata";
    }

    //获取图集
    public static String getPhotoListUrl()
    {
        return ConfigManager.instance().getDomainName() + "/photo/list";
    }


    //获取图集
    public static String getVideoPriceUrl()
    {
        return ConfigManager.instance().getDomainName() + "/video/get_price";
    }

    //获取自拍详情
    public static String getPhotoDetailUrl()
    {
        return ConfigManager.instance().getDomainName() + "/photo/show";
    }

    //获取自拍详情
    public static String getPhotoUploadUrl()
    {
        return ConfigManager.instance().getUploadUrl();
    }

    //获取自拍详情
    public static String getAddPhotoUrl()
    {
        return ConfigManager.instance().getDomainName() + "/photo/create";
    }

    //获取定位
    public static String getIplookupUrl()
    {
        return "http://ip.ccydj.com/lbs/iplookup";
    }

    public static String getUserInfoUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/index";
    }

    public static String getUserSignUrl()
    {
        return ConfigManager.instance().getDomainName() + "/task/sign";
    }

    public static String getTaskUrl()
    {
        return ConfigManager.instance().getDomainName() + "/task/index";
    }


    public static String getEmailCodeUrl()
    {
        return ConfigManager.instance().getDomainName() + "/data/vcode";
    }

    public static String getTaskprofileUrl()
    {
        return ConfigManager.instance().getDomainName() + "/task/profile";
    }

    public static String getFortuneDetailUrl()
    {
        return ConfigManager.instance().getDomainName() + "/fortune/order_log";
    }

    public static String getFortuneOrderDetailUrl()
    {
        return ConfigManager.instance().getDomainName() + "/fortune/order";
    }

    public static String getRankingUrl()
    {
        return ConfigManager.instance().getDomainName() + "/fortune/rank";
    }

    public static String getMessageUrl()
    {
        return ConfigManager.instance().getDomainName() + "/msg/index";
    }

    public static String getFavoritUrl()
    {
        return ConfigManager.instance().getDomainName() + "/favorite/index";
    }

    public static String getCollectionRequestUrl()
    {
        return ConfigManager.instance().getDomainName() + "/favorite/like";
    }

    public static String getFavoriteUnLikeUrl()
    {
        return ConfigManager.instance().getDomainName() + "/favorite/unlike";
    }

    public static String getCommentlistUrl()
    {
        return ConfigManager.instance().getDomainName() + "/photo/list_say";
    }


    public static String getSendCommentUrl()
    {
        return ConfigManager.instance().getDomainName() + "/photo/say";
    }

    public static String getAnchorDetailUrl()
    {
        return ConfigManager.instance().getDomainName() + "/live/biuty";
    }

    public static String getSharePhotoUrl(String biz_id)
    {
        return ConfigManager.instance().getDomainName() + "/photo/share?token=" + ConfigManager.instance().getUniqueCode() + "&device=and&biz_id=" + biz_id;
    }

    public static String getShareVideoUrl(String biz_id)
    {
        return ConfigManager.instance().getDomainName() + "/video/share?token=" + ConfigManager.instance().getUniqueCode() + "&device=and&biz_id=" + biz_id;
    }

    public static String getSearchUrl()
    {
        return ConfigManager.instance().getDomainName() + "/data/find";
    }


    public static String getShareUrl()
    {
        return ConfigManager.instance().getDomainName() + "/data/share";
    }

    public static String getTaskShareUrl()
    {
        return ConfigManager.instance().getDomainName() + "/task/share";
    }


    public static String getUserForgotUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/forgot";
    }

    public static String getSrviceUrl()
    {
        return ConfigManager.instance().getDomainName() + "/service";
    }


    public static String getInviteUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/invite";
    }


    public static String getMessageReportUrl()
    {
        return ConfigManager.instance().getDomainName() + "/msg/report";
    }

    public static String getCooperationUrl()
    {
        return ConfigManager.instance().getDomainName() + "/service/cooperation";
    }

    public static String getResetunameUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/reset_uname";
    }

    public static String getPhoneCodeUrl()
    {
        return ConfigManager.instance().getDomainName() + "/data/scode";
    }


    public static String getPayUrl(int amount, String product, String mobile_id)
    {
        return ConfigManager.instance().getDomainName() + "/paypal?auth=" + ConfigManager.instance().getUniqueCode() + "&amount=" + amount + "&product=" + product + "&mobile_id=" + mobile_id + "&device=and";
    }

    //判断gift是否可以赠送
    public static String getFortuneGiftUrl()
    {
        return ConfigManager.instance().getDomainName() + "/fortune/gift";
    }

    //赠送礼物
    public static String getFortuneBuyUrl()
    {
        return ConfigManager.instance().getDomainName() + "/fortune/buy";
    }


    //获取消息
    public static String getNoticeUrl()
    {
        return ConfigManager.instance().getDomainName() + "/msg/notice";
    }


    //获取消息
    public static String getMsgReportUrl()
    {
        return ConfigManager.instance().getDomainName() + "/msg/report";
    }


    public static String getWithdrawUrl()
    {
        return ConfigManager.instance().getDomainName() + "/paypal/withdraw";
    }

    public static String getPaypalPriceUrl()
    {
        return ConfigManager.instance().getDomainName() + "/paypal/price";
    }


}

