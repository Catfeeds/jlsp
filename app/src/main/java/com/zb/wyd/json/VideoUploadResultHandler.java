package com.zb.wyd.json;


import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.VideoUplaodResult;

import org.json.JSONObject;

/**
 */
public class VideoUploadResultHandler extends JsonHandler
{
    private VideoUplaodResult videoUplaodResult;

    public VideoUplaodResult getVideoUplaodResult()
    {
        return videoUplaodResult;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {


            videoUplaodResult = new VideoUplaodResult(jsonObj.optJSONObject("data"));


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
