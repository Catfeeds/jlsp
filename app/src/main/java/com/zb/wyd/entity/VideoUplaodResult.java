package com.zb.wyd.entity;

import org.json.JSONObject;

public class VideoUplaodResult
{
    private String name;//VIDEO_20181208_201836.mp4
    private String type;//video\/mp4
    private String size;
    private String key;
    private String ext;//mp4
    private String md5;//3476a6dfed303d4882a32e95c1ab90e2
    private String sha1;//0318585c86d2aa64811cc7756cf71bc3129eb466
    private String savename;//DXcGiuccIKALMwSYxYET.mp4
    private String savepath;//\/1812\/08\/08\/
    private String host;//http:\/\/p1.du10010.comprivate String

    public VideoUplaodResult(JSONObject obj)
    {
        this.name = obj.optString("name");
        this.type = obj.optString("type");
        this.size = obj.optString("size");
        this.key = obj.optString("key");
        this.savename = obj.optString("savename");
        this.savepath = obj.optString("savepath");
        this.host = obj.optString("host");

    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getExt()
    {
        return ext;
    }

    public void setExt(String ext)
    {
        this.ext = ext;
    }

    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public String getSha1()
    {
        return sha1;
    }

    public void setSha1(String sha1)
    {
        this.sha1 = sha1;
    }

    public String getSavename()
    {
        return savename;
    }

    public void setSavename(String savename)
    {
        this.savename = savename;
    }

    public String getSavepath()
    {
        return savepath;
    }

    public void setSavepath(String savepath)
    {
        this.savepath = savepath;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }
}
