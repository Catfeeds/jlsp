package com.zb.wyd.entity;

import org.json.JSONObject;

public class DanmuInfo
{
    private String data;
    private String action;
    private long timpos;
    private String style;
    private String type;
    public DanmuInfo(JSONObject obj)
    {
        this.data = obj.optString("data");
        this.action = obj.optString("action");
        this.timpos = obj.optLong("timpos");
        this.style = obj.optString("style");
        this.type = obj.optString("type");
    }
    public DanmuInfo(){}

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setTimpos(long timpos)
    {
        this.timpos = timpos;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public long getTimpos()
    {
        return timpos;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }
}
