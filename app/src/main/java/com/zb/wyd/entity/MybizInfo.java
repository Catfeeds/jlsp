package com.zb.wyd.entity;

import org.json.JSONObject;

public class MybizInfo
{
    private int score;// 0,评分
    private int is_buy;// 0,是否购买
    private int is_like;// 0,是否喜欢
    private int is_fav;// 0是否收藏

    public MybizInfo(JSONObject obj)
    {
        this.score = obj.optInt("score");
        this.is_buy = obj.optInt("is_buy");
        this.is_like = obj.optInt("is_like");
        this.is_fav = obj.optInt("is_fav");
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getIs_buy()
    {
        return is_buy;
    }

    public void setIs_buy(int is_buy)
    {
        this.is_buy = is_buy;
    }

    public int getIs_like()
    {
        return is_like;
    }

    public void setIs_like(int is_like)
    {
        this.is_like = is_like;
    }

    public int getIs_fav()
    {
        return is_fav;
    }

    public void setIs_fav(int is_fav)
    {
        this.is_fav = is_fav;
    }
}
