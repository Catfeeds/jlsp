package com.zb.wyd.entity;

import org.json.JSONObject;

public class VersionInfo
{
    private String version;//版本号
    private String version_desc;//版本描述
    private String version_name;//
    private String link;//下载链接
    private String forcedup;//是否强制升级。
    private String add_time;
    private String update_time;
    private String del_flag;
    private String text;
    private boolean reg_closed;
    private String iplookup;
    private String bg_login;//
    private String bg_startup;//
    private String qq;//
    private String emai;//
    private String crossfire;
    private String upload;
    private String chat;
    private String upload_img;
    private String upload_dy;

    public VersionInfo(JSONObject obj)
    {

        this.upload_img = obj.optString("upload_img");
        this.upload_dy = obj.optString("upload_dy");

        this.upload = obj.optString("upload");
        this.iplookup = obj.optString("iplookup");
        this.bg_login = obj.optString("bg_login");
        this.bg_startup = obj.optString("bg_startup");
        this.qq = obj.optString("qq");
        this.emai = obj.optString("email");
        this.version = obj.optString("version");
        this.version_desc = obj.optString("version_desc");
        this.version_name = obj.optString("version_name");
        this.link = obj.optString("version_down");
        this.forcedup = obj.optString("upgrade");
        this.add_time = obj.optString("add_time");
        this.update_time = obj.optString("update_time");
        this.del_flag = obj.optString("del_flag");
        this.crossfire = obj.optString("crossfire");
        this.text = obj.optString("text");
        this.reg_closed = obj.optBoolean("reg_close");
        this.chat = obj.optString("chat");
        forcedup = "1";

    }

    public String getUpload()
    {
        return upload;
    }

    public void setUpload(String upload)
    {
        this.upload = upload;
    }

    public String getCrossfire()
    {
        return crossfire;
    }

    public void setCrossfire(String crossfire)
    {
        this.crossfire = crossfire;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getVersion_desc()
    {
        return version_desc;
    }

    public void setVersion_desc(String version_desc)
    {
        this.version_desc = version_desc;
    }

    public String getVersion_name()
    {
        return version_name;
    }

    public void setVersion_name(String version_name)
    {
        this.version_name = version_name;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getForcedup()
    {
        return forcedup;
    }

    public void setForcedup(String forcedup)
    {
        this.forcedup = forcedup;
    }

    public String getAdd_time()
    {
        return add_time;
    }

    public void setAdd_time(String add_time)
    {
        this.add_time = add_time;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }

    public String getDel_flag()
    {
        return del_flag;
    }

    public void setDel_flag(String del_flag)
    {
        this.del_flag = del_flag;
    }

    public String getBg_login()
    {
        return bg_login;
    }

    public void setBg_login(String bg_login)
    {
        this.bg_login = bg_login;
    }

    public String getBg_startup()
    {
        return bg_startup;
    }

    public void setBg_startup(String bg_startup)
    {
        this.bg_startup = bg_startup;
    }

    public String getQq()
    {
        return qq;
    }

    public void setQq(String qq)
    {
        this.qq = qq;
    }

    public String getEmai()
    {
        return emai;
    }

    public void setEmai(String emai)
    {
        this.emai = emai;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public boolean isReg_closed()
    {
        return reg_closed;
    }

    public void setReg_closed(boolean reg_closed)
    {
        this.reg_closed = reg_closed;
    }

    public String getIplookup()
    {
        return iplookup;
    }

    public void setIplookup(String iplookup)
    {
        this.iplookup = iplookup;
    }

    public String getChat()
    {
        return chat;
    }

    public void setChat(String chat)
    {
        this.chat = chat;
    }

    public String getUpload_img()
    {
        return upload_img;
    }

    public void setUpload_img(String upload_img)
    {
        this.upload_img = upload_img;
    }

    public String getUpload_dy()
    {
        return upload_dy;
    }

    public void setUpload_dy(String upload_dy)
    {
        this.upload_dy = upload_dy;
    }
}
