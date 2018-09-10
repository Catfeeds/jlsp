package com.zb.wyd.entity;

import com.zb.wyd.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：一句话简单描述
 */
public class PhotoInfo
{
    private String    uid;//21
    private String    pname;//
    private String    desc;//
    private String    tags;//-----
    private String    location;//--
    private String    host;
    private String    contact;//
    private String    free_album;
    private String    charge_album;//
    private String    favour_count;//0
    private String    add_time;//2018-06-22
    private PriceInfo priceInfo;

    private String savename;//ALJzcAHDVkwodewfchZV.jpg",
    private String savepath;//\/18\/06\/24\/01\/",

    private UserInfo userInfo;

    private String has_favorite;

    private List<String> freePic   = new ArrayList<>();
    private List<String> chargePic = new ArrayList<>();

    public PhotoInfo(JSONObject obj)
    {
        this.savename = obj.optString("savename");
        this.savepath = obj.optString("savepath");

        this.uid = obj.optString("uid");
        this.pname = obj.optString("pname");
        this.desc = obj.optString("desc");
        this.tags = obj.optString("tags");
        this.location = obj.optString("location");
        this.host = obj.optString("host");
        this.contact = obj.optString("contact");
        this.free_album = obj.optString("free_album");
        this.charge_album = obj.optString("charge_album");
        this.favour_count = obj.optString("favour_count");
        this.add_time = obj.optString("add_time");
        this.has_favorite = obj.optString("has_favorite");
        if (!StringUtils.stringIsEmpty(free_album)) ;
        {
            String[] free = free_album.replace("$$", ";").split(";");

            for (int i = 0; i < free.length; i++)
            {
                if (!StringUtils.stringIsEmpty(free[i]))

                    if (free[i].startsWith("http"))
                    {
                        freePic.add(free[i]);
                    }
                    else
                    {
                        freePic.add(host + free[i]);
                    }

            }
        }

        if (!StringUtils.stringIsEmpty(charge_album)) ;
        {
            String[] charge = charge_album.replace("$$", ";").split(";");

            for (int i = 0; i < charge.length; i++)
            {
                if (!StringUtils.stringIsEmpty(charge[i]))
                {
                    if (charge[i].startsWith("http"))
                    {
                        chargePic.add(charge[i]);
                    }
                    else
                    {
                        chargePic.add(host + charge[i]);
                    }
                }

            }
        }
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getPname()
    {
        return pname;
    }

    public void setPname(String pname)
    {
        this.pname = pname;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getFree_album()
    {
        return free_album;
    }

    public void setFree_album(String free_album)
    {
        this.free_album = free_album;
    }

    public String getCharge_album()
    {
        return charge_album;
    }

    public void setCharge_album(String charge_album)
    {
        this.charge_album = charge_album;
    }

    public String getFavour_count()
    {
        return favour_count;
    }

    public void setFavour_count(String favour_count)
    {
        this.favour_count = favour_count;
    }

    public String getAdd_time()
    {
        return add_time;
    }

    public void setAdd_time(String add_time)
    {
        this.add_time = add_time;
    }


    public List<String> getFreePic()
    {
        return freePic;
    }

    public void setFreePic(List<String> freePic)
    {
        this.freePic = freePic;
    }

    public List<String> getChargePic()
    {
        return chargePic;
    }

    public void setChargePic(List<String> chargePic)
    {
        this.chargePic = chargePic;
    }

    public PriceInfo getPriceInfo()
    {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfo)
    {
        this.priceInfo = priceInfo;
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

    public UserInfo getUserInfo()
    {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }

    public String getHas_favorite()
    {
        return has_favorite;
    }

    public void setHas_favorite(String has_favorite)
    {
        this.has_favorite = has_favorite;
    }
}
