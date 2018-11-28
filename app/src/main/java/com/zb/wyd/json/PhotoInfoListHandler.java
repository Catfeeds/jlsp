package com.zb.wyd.json;


import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.PriceInfo;
import com.zb.wyd.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class PhotoInfoListHandler extends JsonHandler
{

    private List<PhotoInfo> photoInfoList = new ArrayList<>();

    public List<PhotoInfo> getPhotoInfoList()
    {
        return photoInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONArray array = jsonObj.optJSONArray("data");


            if (null != array)
            {

                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject obj = array.optJSONObject(i);
                    PhotoInfo photoInfo = new PhotoInfo(obj);
                    if (null != obj.optJSONObject("userinfo"))
                        photoInfo.setUserInfo(new UserInfo(obj.optJSONObject("userinfo")));

                    if (null != obj.optJSONObject("price"))
                        photoInfo.setPriceInfo(new PriceInfo(obj.optJSONObject("price")));

                    photoInfoList.add(photoInfo);
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
