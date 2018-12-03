package com.zb.wyd.entity;

import com.zb.wyd.utils.ConfigManager;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 描述：一句话简单描述
 */
public class UserInfo implements Serializable
{
    private String id;//12570;//
    private String nick;//晓宝宝;//
    private String face;//http:\/\/img2.inke.cn\/MTUxMzUxNTM3MDU2NSM1MjQjanBn.jpg;//
    private String uri;//http:\/\/alsource.pull.inke.cn\/live\/1529036907860561.flv?ikDnsOp=1&ikHost=ali&ikOp=0&codecInfo=8192&ikLog=0&dpSrcG=-1&ikMinBuf
    // =3800&ikMaxBuf=4800&ikSlowRate=1.0&ikFastRate=1.0;//
    private String favour_count;//0;//
    private String update_time;//1529037601;//
    private String online;//12345,
    private String is_live;//1
    private String uface;//1
    private String sex;
    private String has_favorite;

    private int fans;
    private String uname;//test003;//
    private String email;//
    private String invite;//
    private String sign_score;//
    private String role;//
    private String vip_level;//
    private String vip_expire;//
    private int    vip_type;
    private String login_time;//
    private String login_ip;//
    // private String fortune;//
    private String unick;//
    private String total_score;

    private boolean     valid_vip;
    private FortuneInfo fortuneInfo;

    private boolean has_sign;

    public UserInfo()
    {
    }

    public UserInfo(JSONObject obj)
    {
        this.fans = obj.optInt("fans");
        this.unick = obj.optString("unick");
        this.uname = obj.optString("uname");
        this.email = obj.optString("email");
        this.invite = obj.optString("invite");
        this.sign_score = obj.optString("sign_score");
        this.role = obj.optString("role");
        this.vip_level = obj.optString("vip_level");
        this.vip_expire = obj.optString("vip_expire");
        this.login_time = obj.optString("login_time");
        this.login_ip = obj.optString("login_ip");
        //  this.fortune = obj.optString("fortune");
        this.total_score = obj.optString("total_score");
        this.has_favorite = obj.optString("has_favorite");
        this.vip_type = obj.optInt("vip_type");
        this.id = obj.optString("id");
        this.uface = obj.optString("uface");
        this.nick = obj.optString("nick");
        this.face = obj.optString("face");
        this.uri = obj.optString("uri");
        this.favour_count = obj.optString("favour_count");
        this.update_time = obj.optString("update_time");
        this.online = obj.optString("online");
        this.is_live = obj.optString("is_live");
        this.valid_vip = obj.optBoolean("valid_vip");
        this.vip_type = obj.optInt("vip_type");
        this.has_sign = obj.optBoolean("has_sign");
        this.sex = obj.optString("sex");
    }

    public int getFans()
    {
        return fans;
    }

    public void setFans(int fans)
    {
        this.fans = fans;
    }

    public boolean isHas_sign()
    {
        return has_sign;
    }

    public void setHas_sign(boolean has_sign)
    {
        this.has_sign = has_sign;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNick()
    {
        return nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public String getFace()
    {
        return face;
    }

    public void setFace(String face)
    {
        this.face = face;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getFavour_count()
    {
        return favour_count;
    }

    public void setFavour_count(String favour_count)
    {
        this.favour_count = favour_count;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }

    public String getOnline()
    {
        return online;
    }

    public void setOnline(String online)
    {
        this.online = online;
    }

    public String getIs_live()
    {
        return is_live;
    }

    public void setIs_live(String is_live)
    {
        this.is_live = is_live;
    }

    public String getUface()
    {
        return uface;
    }

    public void setUface(String uface)
    {
        this.uface = uface;
    }

    public String getUname()
    {
        return uname;
    }

    public void setUname(String uname)
    {
        this.uname = uname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getInvite()
    {
        return invite;
    }

    public void setInvite(String invite)
    {
        this.invite = invite;
    }

    public String getSign_score()
    {
        return sign_score;
    }

    public void setSign_score(String sign_score)
    {
        this.sign_score = sign_score;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getVip_level()
    {
        return vip_level;
    }

    public void setVip_level(String vip_level)
    {
        this.vip_level = vip_level;
    }

    public String getVip_expire()
    {
        return vip_expire;
    }

    public void setVip_expire(String vip_expire)
    {
        this.vip_expire = vip_expire;
    }

    public String getLogin_time()
    {
        return login_time;
    }

    public void setLogin_time(String login_time)
    {
        this.login_time = login_time;
    }

    public String getLogin_ip()
    {
        return login_ip;
    }

    public void setLogin_ip(String login_ip)
    {
        this.login_ip = login_ip;
    }

    public String getUnick()
    {
        return unick;
    }

    public void setUnick(String unick)
    {
        this.unick = unick;
    }

    public String getTotal_score()
    {
        return total_score;
    }

    public void setTotal_score(String total_score)
    {
        this.total_score = total_score;
    }

    public String getHas_favorite()
    {
        return has_favorite;
    }

    public void setHas_favorite(String has_favorite)
    {
        this.has_favorite = has_favorite;
    }

    public FortuneInfo getFortuneInfo()
    {
        return fortuneInfo;
    }

    public void setFortuneInfo(FortuneInfo fortuneInfo)
    {
        this.fortuneInfo = fortuneInfo;
    }

    public int getVip_type()
    {
        return vip_type;
    }

    public void setVip_type(int vip_type)
    {
        this.vip_type = vip_type;
    }

    public boolean isValid_vip()
    {
        return valid_vip;
    }

    public void setValid_vip(boolean valid_vip)
    {
        this.valid_vip = valid_vip;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }
}
