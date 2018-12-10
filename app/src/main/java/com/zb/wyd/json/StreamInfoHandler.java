package com.zb.wyd.json;


import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.MybizInfo;
import com.zb.wyd.entity.StreamInfo;

import org.json.JSONObject;

/**
 */
public class StreamInfoHandler extends JsonHandler
{
    private StreamInfo streamInfo;

    public StreamInfo getStreamInfo()
    {
        return streamInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {

            JSONObject obj = jsonObj.optJSONObject("data");
            if(null !=obj )
            {
                streamInfo = new StreamInfo(obj);

                JSONObject mybizObj = obj.optJSONObject("mybiz");

                if(null != mybizObj)
                {
                    streamInfo.setMybizInfo(new MybizInfo(mybizObj));
                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
