package com.zb.wyd.entity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoDetailInfo
{
    private String iname;//LenaPaul_ReaganFoxx
    private String idesc;//
    private String cover;//
    private String tags;//
    private String playlist;//
    private String favour_count;//162
    private String view_count;//0
    private int score;// 5,
    private String add_time;//2018-11-14
    private int  cash;
    private MybizInfo mybizInfo;
    private List<HostInfo> hostInfoList = new ArrayList<>();
    private UserInfo userInfo;


    public VideoDetailInfo(JSONObject obj)
    {
        this.iname = obj.optString("iname");
        this.idesc = obj.optString("idesc");
        this.cover = obj.optString("cover");
        this.tags = obj.optString("tags");
        this.playlist = obj.optString("playlist");
        this.favour_count = obj.optString("favour_count");
        this.view_count = obj.optString("view_count");
        this.add_time = obj.optString("add_time");
        this.score = obj.optInt("score");
        this.cash = obj.optInt("cash");
    }

    public int getCash()
    {
        return cash;
    }

    public void setCash(int cash)
    {
        this.cash = cash;
    }

    public String getIname()
    {
        return iname;
    }

    public void setIname(String iname)
    {
        this.iname = iname;
    }

    public String getIdesc()
    {
        return idesc;
    }

    public void setIdesc(String idesc)
    {
        this.idesc = idesc;
    }

    public String getCover()
    {
        return cover;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getPlaylist()
    {
        return playlist;
    }

    public void setPlaylist(String playlist)
    {
        this.playlist = playlist;
    }

    public String getFavour_count()
    {
        return favour_count;
    }

    public void setFavour_count(String favour_count)
    {
        this.favour_count = favour_count;
    }

    public String getView_count()
    {
        return view_count;
    }

    public void setView_count(String view_count)
    {
        this.view_count = view_count;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public String getAdd_time()
    {
        return add_time;
    }

    public void setAdd_time(String add_time)
    {
        this.add_time = add_time;
    }

    public MybizInfo getMybizInfo()
    {
        return mybizInfo;
    }

    public void setMybizInfo(MybizInfo mybizInfo)
    {
        this.mybizInfo = mybizInfo;
    }

    public List<HostInfo> getHostInfoList()
    {
        return hostInfoList;
    }

    public void setHostInfoList(List<HostInfo> hostInfoList)
    {
        this.hostInfoList = hostInfoList;
    }

    public UserInfo getUserInfo()
    {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }
}
