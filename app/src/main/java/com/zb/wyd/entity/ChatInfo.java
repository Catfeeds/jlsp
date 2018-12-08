package com.zb.wyd.entity;

import org.json.JSONObject;

public class ChatInfo
{

    private String data;
    private String action;
    private String type;
    private boolean is_mine;
    private String content;
    private String add_time;

    private UserInfo userInfo;


    public ChatInfo(JSONObject obj)
    {
        this.is_mine = obj.optBoolean("is_mine");
        this.content = obj.optString("content");
        this.add_time = obj.optString("add_time");

        this.data = obj.optString("data");
        this.action = obj.optString("action");
        this.type = obj.optString("type");

        JSONObject userObj = obj.optJSONObject("userinfo");

        if(null != userObj)
        {
            UserInfo mUserInfo = new UserInfo(userObj);
            setUserInfo(mUserInfo);
        }
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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public UserInfo getUserInfo()
    {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }


    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getAdd_time()
    {
        return add_time;
    }

    public void setAdd_time(String add_time)
    {
        this.add_time = add_time;
    }

    public boolean isIs_mine()
    {
        return is_mine;
    }

    public void setIs_mine(boolean is_mine)
    {
        this.is_mine = is_mine;
    }
}
