package com.zb.wyd.json;


import com.zb.wyd.entity.FortuneInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.utils.ConfigManager;

import org.json.JSONObject;


/**
 */
public class UserInfoHandler extends JsonHandler
{
    private UserInfo userInfo;

    public UserInfo getUserInfo()
    {
        return userInfo;
    }


    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {

            JSONObject obj = jsonObj.optJSONObject("data");
            if (null != obj)
            {
                userInfo = new UserInfo(obj);

                if(null !=userInfo )
                {
                    FortuneInfo mFortuneInfo = new FortuneInfo(obj.optJSONObject("fortune"));
                    userInfo.setFortuneInfo(mFortuneInfo);
                    ConfigManager.instance().setValid_vip(userInfo.isValid_vip());
                }

            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
