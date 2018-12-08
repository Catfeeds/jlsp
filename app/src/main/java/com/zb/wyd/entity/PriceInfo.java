package com.zb.wyd.entity;

import org.json.JSONObject;

/**
 * 描述：一句话简单描述
 */
public class PriceInfo
{
    private String finger;//安全指纹，提交支付的时候需要带上该参数
    private String  user_money;//用户余额
    private String vip_free;//是否可以免费观看
    private String id;//直播ID
    private int    off_amount;//折扣
    private int    old_amount;//原价
    private String msg;

    public PriceInfo(JSONObject obj)
    {
        this.finger = obj.optString("finger");
        this.user_money = obj.optString("user_money");
        this.vip_free = obj.optString("vip_free");
        this.id = obj.optString("id");
        this.off_amount = obj.optInt("amount");
        this.old_amount = obj.optInt("old_amount");
        this.msg = obj.optString("msg");
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getFinger()
    {
        return finger;
    }

    public void setFinger(String finger)
    {
        this.finger = finger;
    }

    public String getUser_money()
    {
        return user_money;
    }

    public void setUser_money(String user_money)
    {
        this.user_money = user_money;
    }

    public String getVip_free()
    {
        return vip_free;
    }

    public void setVip_free(String vip_free)
    {
        this.vip_free = vip_free;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getOff_amount()
    {
        return off_amount;
    }

    public void setOff_amount(int off_amount)
    {
        this.off_amount = off_amount;
    }

    public int getOld_amount()
    {
        return old_amount;
    }

    public void setOld_amount(int old_amount)
    {
        this.old_amount = old_amount;
    }
}
