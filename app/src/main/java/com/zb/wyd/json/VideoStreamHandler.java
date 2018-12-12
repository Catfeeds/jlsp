package com.zb.wyd.json;


import com.zb.wyd.entity.ChannelInfo;
import com.zb.wyd.entity.HostInfo;
import com.zb.wyd.entity.LiveInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class VideoStreamHandler extends JsonHandler
{
    private String uri;

    public String getUri()
    {
        return uri;
    }

    private List<HostInfo> hostInfoList = new ArrayList<>();

    public List<HostInfo> getHostInfoList()
    {
        return hostInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {

            JSONObject obj = jsonObj.optJSONObject("data");

            if (null != obj)
            {
                uri = obj.optString("uri");

                JSONArray arr = obj.optJSONArray("hosts");

                if(null != arr)
                {
                    for (int i = 0; i < arr.length(); i++)
                    {
                        HostInfo mHostInfo = new HostInfo(arr.optJSONObject(i));
                        hostInfoList.add(mHostInfo);

                    }
                }

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
