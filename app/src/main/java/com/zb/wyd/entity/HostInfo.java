package com.zb.wyd.entity;

import org.json.JSONObject;

public class HostInfo
{
    private String line;
    private String title;
    private boolean isSelected;


    public HostInfo(JSONObject obj)
    {
        this.line = obj.optString("line");
        this.title = obj.optString("title");
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    public String getLine()
    {
        return line;
    }

    public void setLine(String line)
    {
        this.line = line;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
