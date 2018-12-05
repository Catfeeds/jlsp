package com.zb.wyd.json;


import com.zb.wyd.entity.LiveInfo;
import com.zb.wyd.entity.SearchInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class SearchInfoListHandler extends JsonHandler
{
    private List<SearchInfo> searchInfoList = new ArrayList<>();

    public List<SearchInfo> getSearchInfoList()
    {
        return searchInfoList;
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
                    SearchInfo mSearchInfo = new SearchInfo(arr.optJSONObject(i));
                    searchInfoList.add(mSearchInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
