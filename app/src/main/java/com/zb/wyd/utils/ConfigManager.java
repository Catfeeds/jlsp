package com.zb.wyd.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author
 */
public class ConfigManager
{
    /**
     * instance
     */
    private static ConfigManager     sConfigManager     = null;
    /**
     * mCtx
     */
    private        Context           mCtx               = null;
    /**
     * SharedPreferences
     */
    private        SharedPreferences mSharedPreferences = null;
    /**
     * config
     */
    private static String            CONFIG_NAME        = "config";

    private static final String IS_FRIST_LOGING = "IS_FRIST_LOGING";
    private static final String USER_PWD        = "user_pwd";//密码
    private static final String USER_NAME       = "user_name";// 用户名
    private static final String USER_ID         = "user_id";
    private static final String IDENTIFIER      = "identifier";
    private static final String PREF_UUID       = "miei";
    private static final String UNIQUE_CODE     = "unique_code";
    private static final String USER_MOBILE     = "mobile";
    private static final String IS_SAVE_PWD     = "is_save_pwd";
    private static final String GUEST_ID        = "guest_id";
    private static final String USER_NICK_NAME  = "user_nick_name";
    private static final String USER_EMAIL      = "user_email";
    private static final String TOKEN           = "TOKEN";
    private static final String USER_PIC        = "useer_pic";
    private static final String USER_SEX        = "user_sex";

    private static final String SYSTEM_EMAIL = "system_email";
    private static final String SYSTEM_QQ    = "system_qq";
    private static final String BG_LOGIN     = "bg_login";
    private static final String BG_STARTUP   = "bg_startup";

    private static final String DOMAIN_NAME = "domain_name";

    private static final String CROSSFIRE       = "crossfire";
    private static final String REG_CLOSED      = "reg_closed";
    private static final String IP_LOOK_UP      = "ip_look_up";
    private static final String VIP_LEVEL       = "vip_level";
    private static final String VIP_TYPE        = "VIP_TYPE";
    private static final String ZDY_DOMAIN_NAME = "zdy_domain_name";

    private static final String USER_ROLE = "user_role";
    private static final String UPLOAD_URL = "upload_url";
    private static final String CHAT_URL = "chat_url";
    private static final String VALID_VIP = "valid_vip";
    private static final String UPLOAD_IMG = "upload_img";
    private static final String UPLOAD_DY= "upload_dy";
    /**
     * 返回实例
     *
     * @return
     */
    public static ConfigManager instance()
    {
        if (sConfigManager == null)
        {
            sConfigManager = new ConfigManager();
        }

        return sConfigManager;
    }

    /**
     * init shared Preferences
     *
     * @param context
     */
    public void init(Context context)
    {
        mCtx = context;
        mSharedPreferences = context.getSharedPreferences(CONFIG_NAME, 0);

    }


    public String getUserPwd()
    {
        return mSharedPreferences.getString(USER_PWD, "");
    }

    public void setUserPwd(String pwd)
    {
        mSharedPreferences.edit().putString(USER_PWD, pwd).commit();
        return;
    }


    // 用户名
    public String getUserName()
    {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public void setUserName(String userName)
    {
        mSharedPreferences.edit().putString(USER_NAME, userName).commit();
    }


    public String getUserID()
    {
        return mSharedPreferences.getString(USER_ID, "");
    }

    public void setUserId(String userId)
    {
        mSharedPreferences.edit().putString(USER_ID, userId).apply();
    }

    public String getIdentifier()
    {
        return mSharedPreferences.getString(IDENTIFIER, "");
    }

    public void setUUID(String uuid)
    {
        mSharedPreferences.edit().putString(PREF_UUID, uuid).commit();
    }

    public String getUUID()
    {
        return mSharedPreferences.getString(PREF_UUID, null);
    }


    public String getUniqueCode()
    {
        return mSharedPreferences.getString(UNIQUE_CODE, "");
    }

    public void setUniqueCode(String uniqueCode)
    {
        mSharedPreferences.edit().putString(UNIQUE_CODE, uniqueCode).commit();
    }

    public String getMobile()
    {
        return mSharedPreferences.getString(USER_MOBILE, "");
    }

    public void setMobile(String mobile)
    {
        mSharedPreferences.edit().putString(USER_MOBILE, mobile).commit();
    }


    public boolean getIsSavePwd()
    {
        return mSharedPreferences.getBoolean(IS_SAVE_PWD, false);
    }

    public void setIsSavePwd(boolean status)
    {
        mSharedPreferences.edit().putBoolean(IS_SAVE_PWD, status).commit();
    }


    public void setGUESTID(String id)
    {

        mSharedPreferences.edit().putString(GUEST_ID, id).commit();
    }

    public String getGUESTID()
    {
        //return StringUtils.getRandomString(5);
        return mSharedPreferences.getString(GUEST_ID, "");
    }


    public void setUserPic(String isHolder)
    {

        mSharedPreferences.edit().putString(USER_PIC, isHolder).commit();
    }

    public String getUserPic()
    {
        return mSharedPreferences.getString(USER_PIC, "");
    }


    public void setUserNickName(String nickName)
    {
        mSharedPreferences.edit().putString(USER_NICK_NAME, nickName).commit();
    }

    public String getUserNickName()
    {
        return mSharedPreferences.getString(USER_NICK_NAME, "");
    }

    public void setToken(String registerId)
    {
        mSharedPreferences.edit().putString(TOKEN, registerId).commit();
    }

    public String getToken()
    {
        return mSharedPreferences.getString(TOKEN, "");
    }

    public boolean getIsFristLogin()
    {
        return mSharedPreferences.getBoolean(IS_FRIST_LOGING, true);
    }

    public void setIsFristLogin(boolean status)
    {
        mSharedPreferences.edit().putBoolean(IS_FRIST_LOGING, status).commit();
    }

    public int getUserSex()
    {
        return mSharedPreferences.getInt(USER_SEX, 0);
    }

    public void setUserSex(int sex)
    {
        mSharedPreferences.edit().putInt(USER_SEX, sex).commit();
    }


    public void setUserEmail(String email)
    {
        mSharedPreferences.edit().putString(USER_EMAIL, email).commit();
    }

    public String getUserEmail()
    {
        return mSharedPreferences.getString(USER_EMAIL, "");
    }


    public void setSystemEmail(String email)
    {
        mSharedPreferences.edit().putString(SYSTEM_EMAIL, email).commit();
    }

    public String getSystemEmail()
    {
        return mSharedPreferences.getString(SYSTEM_EMAIL, "");
    }

    public void setSystemQq(String email)
    {
        mSharedPreferences.edit().putString(SYSTEM_QQ, email).commit();
    }

    public String getSystemQq()
    {
        return mSharedPreferences.getString(SYSTEM_QQ, "");
    }

    public void setBgLogin(String str)
    {
        mSharedPreferences.edit().putString(BG_LOGIN, str).commit();
    }

    public String getBgLogin()
    {
        return mSharedPreferences.getString(BG_LOGIN, "");
    }

    public void setBgStartup(String str)
    {
        mSharedPreferences.edit().putString(BG_STARTUP, str).commit();
    }

    public String getBgStartup()
    {
        return mSharedPreferences.getString(BG_STARTUP, "");
    }

    public void setDomainName(String str)
    {
        mSharedPreferences.edit().putString(DOMAIN_NAME, str).commit();
    }

    public String getDomainName()
    {
        return mSharedPreferences.getString(DOMAIN_NAME, "");
    }

    public void setCrossfire(String str)
    {
        mSharedPreferences.edit().putString(CROSSFIRE, str).commit();
    }

    public String getCrossfire()
    {
        return mSharedPreferences.getString(CROSSFIRE, "");
    }


    public void setRegClosed(boolean str)
    {
        mSharedPreferences.edit().putBoolean(REG_CLOSED, str).commit();
    }

    public boolean getRegClosed()
    {
        return mSharedPreferences.getBoolean(REG_CLOSED, false);
    }

    public void setIpLookUp(String str)
    {
        mSharedPreferences.edit().putString(IP_LOOK_UP, str).commit();
    }

    public String getIpLookUp()
    {
        return mSharedPreferences.getString(IP_LOOK_UP, "");
    }

    public void setVipLevel(int str)
    {
        mSharedPreferences.edit().putInt(VIP_LEVEL, str).commit();
    }

    public int getVipLevelVIE()
    {
        return mSharedPreferences.getInt(VIP_LEVEL, 0);
    }

    public void setZdyDomainName(String str)
    {
        mSharedPreferences.edit().putString(ZDY_DOMAIN_NAME, str).commit();
    }

    public String getZydDomainName()
    {
        return mSharedPreferences.getString(ZDY_DOMAIN_NAME, "");
    }


    public void setUserRole(int str)
    {
        mSharedPreferences.edit().putInt(USER_ROLE, str).commit();
    }

    public int getUserRole()
    {
        return mSharedPreferences.getInt(USER_ROLE, 0);
    }


    public void setUploadUrl(String str)
    {
        mSharedPreferences.edit().putString(UPLOAD_URL, str).commit();
    }

    public String getUploadUrl()
    {
        return mSharedPreferences.getString(UPLOAD_URL, "");
    }


    public void setVipType(int str)
    {
        mSharedPreferences.edit().putInt(VIP_TYPE, str).commit();
    }

    public int getVipType()
    {
        return mSharedPreferences.getInt(VIP_TYPE, 0);
    }


    public void setChatUrl(String str)
    {
        mSharedPreferences.edit().putString(CHAT_URL, str).commit();
    }

    public String getChatUrl()
    {
        return mSharedPreferences.getString(CHAT_URL, "ws://116.213.43.157:9501");
    }

    public void setValid_vip(boolean str)
    {
        mSharedPreferences.edit().putBoolean(VALID_VIP, str).commit();
    }

    public boolean getValid_vip()
    {
        return mSharedPreferences.getBoolean(VALID_VIP, false);
    }




    public void setUploadImgUrl(String str)
    {
        mSharedPreferences.edit().putString(UPLOAD_IMG, str).commit();
    }

    public String getUploadImgUrl()
    {
        return mSharedPreferences.getString(UPLOAD_IMG,"");
    }

    public void setUploadDyUrl(String str)
    {
        mSharedPreferences.edit().putString(UPLOAD_DY, str).commit();
    }

    public String getUploadDyUrl()
    {
        return mSharedPreferences.getString(UPLOAD_DY,"");
    }
}

