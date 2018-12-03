package com.zb.wyd.json;


import com.zb.wyd.entity.HostInfo;
import com.zb.wyd.entity.MybizInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.entity.VideoDetailInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class VideoDetailHandler extends JsonHandler
{

    private VideoDetailInfo videoDetailInfo;

    public VideoDetailInfo getVideoDetailInfo()
    {
        return videoDetailInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {

            JSONObject obj = jsonObj.optJSONObject("data");

            if (null != obj)
            {

                videoDetailInfo = new VideoDetailInfo(obj);

                JSONArray array = obj.optJSONArray("ihost");


                JSONObject mybizoObj = obj.optJSONObject("mybiz");
                JSONObject userObj = obj.optJSONObject("userinfo");

                if (null != mybizoObj)
                {
                    videoDetailInfo.setMybizInfo(new MybizInfo(mybizoObj));
                }

                if (null != userObj)
                {
                    videoDetailInfo.setUserInfo(new UserInfo(userObj));
                }

                List<HostInfo> hostInfoList = new ArrayList<>();
                if (null != array)
                {
                    for (int i = 0; i < array.length(); i++)
                    {
                        HostInfo mHostInfo = new HostInfo(array.optJSONObject(i));

                        if (i == 0)
                        {
                            mHostInfo.setSelected(true);
                        }
                        hostInfoList.add(mHostInfo);
                    }
                    videoDetailInfo.setHostInfoList(hostInfoList);

                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
