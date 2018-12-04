package com.zb.wyd.json;


import com.zb.wyd.entity.DanmuInfo;
import com.zb.wyd.entity.LiveInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class DanmuInfoListHandler extends JsonHandler
{
    private List<DanmuInfo> danmuInfoList = new ArrayList<>();

    public List<DanmuInfo> getDanmuInfoList()
    {
        return danmuInfoList;
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
                    DanmuInfo mDanmuInfo = new DanmuInfo(arr.optJSONObject(i));
                    danmuInfoList.add(mDanmuInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
