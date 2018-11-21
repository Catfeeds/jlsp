package com.zb.wyd.json;


import com.zb.wyd.entity.RankingInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class RankingListHandler extends JsonHandler
{
    private List<RankingInfo> rankingInfoList = new ArrayList<>();

    public List<RankingInfo> getRankingInfoList()
    {
        return rankingInfoList;
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
                    RankingInfo mRankingInfo = new RankingInfo(arr.optJSONObject(i));
                    rankingInfoList.add(mRankingInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
