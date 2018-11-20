package com.zb.wyd.json;


import com.zb.wyd.entity.LiveInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class LiveInfoListHandler extends JsonHandler
{
    private List<LiveInfo> liveInfoList = new ArrayList<>();

    public List<LiveInfo> getLiveInfoList()
    {
        return liveInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONArray arr = jsonObj.optJSONArray("data");


            if (null != arr)
            {
                for (int i = 0; i < arr.length(); i++)
                {
                    LiveInfo mLiveInfo = new LiveInfo(arr.optJSONObject(i));
                    liveInfoList.add(mLiveInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
