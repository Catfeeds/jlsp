package com.zb.wyd.entity;

import org.json.JSONObject;

public class StreamInfo
{
    private String biz_id;//3207
    private String author;//"0;//
    private String favour_count;//"353;//
    private String view_count;//"0;//
    private String comment_count;//0,
    private String score;//"0;//
    private String share_count;//"0;//
    private String playlist;//

    private boolean pay_for;
    private MybizInfo mybizInfo;

    public StreamInfo(JSONObject obj)
    {
        this.biz_id = obj.optString("biz_id");
        this.author = obj.optString("author");
        this.favour_count = obj.optString("favour_count");
        this.view_count = obj.optString("view_count");
        this.comment_count = obj.optString("comment_count");
        this.score = obj.optString("score");
        this.share_count = obj.optString("share_count");
        this.playlist = obj.optString("playlist");
        this.pay_for = obj.optBoolean("pay_for");
    }


    public String getBiz_id()
    {
        return biz_id;
    }

    public void setBiz_id(String biz_id)
    {
        this.biz_id = biz_id;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
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

    public String getComment_count()
    {
        return comment_count;
    }

    public void setComment_count(String comment_count)
    {
        this.comment_count = comment_count;
    }

    public String getScore()
    {
        return score;
    }

    public void setScore(String score)
    {
        this.score = score;
    }

    public String getShare_count()
    {
        return share_count;
    }

    public void setShare_count(String share_count)
    {
        this.share_count = share_count;
    }

    public String getPlaylist()
    {
        return playlist;
    }

    public void setPlaylist(String playlist)
    {
        this.playlist = playlist;
    }



    public MybizInfo getMybizInfo()
    {
        return mybizInfo;
    }

    public void setMybizInfo(MybizInfo mybizInfo)
    {
        this.mybizInfo = mybizInfo;
    }

    public boolean isPay_for()
    {
        return pay_for;
    }

    public void setPay_for(boolean pay_for)
    {
        this.pay_for = pay_for;
    }
}
