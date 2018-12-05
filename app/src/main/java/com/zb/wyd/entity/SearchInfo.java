package com.zb.wyd.entity;

import org.json.JSONObject;

public class SearchInfo
{


    private String id;// 439854
    private String co_biz;// 6 //直播，电影，自拍，抖音，小说，语音  1,2,3,5,6,7,8
    private String biz_id;// 1514
    private String cata_id;//
    private String title;//
    private String host;// -
    private String cover;// -
    private String user_id;// 1
    private String add_time;// 2018-07-23
    private String url;//


    public SearchInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.co_biz = obj.optString("co_biz");
        this.biz_id = obj.optString("biz_id");
        this.cata_id = obj.optString("cata_id");
        this.title = obj.optString("title");
        this.host = obj.optString("host");
        this.cover = obj.optString("cover");
        this.user_id = obj.optString("user_id");
        this.add_time = obj.optString("add_time");
        this.url = obj.optString("url");
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCo_biz()
    {
        return co_biz;
    }

    public void setCo_biz(String co_biz)
    {
        this.co_biz = co_biz;
    }

    public String getBiz_id()
    {
        return biz_id;
    }

    public void setBiz_id(String biz_id)
    {
        this.biz_id = biz_id;
    }

    public String getCata_id()
    {
        return cata_id;
    }

    public void setCata_id(String cata_id)
    {
        this.cata_id = cata_id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getCover()
    {
        return cover;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getAdd_time()
    {
        return add_time;
    }

    public void setAdd_time(String add_time)
    {
        this.add_time = add_time;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}

