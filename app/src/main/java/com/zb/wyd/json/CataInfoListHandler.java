package com.zb.wyd.json;


import com.zb.wyd.entity.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CataInfoListHandler extends JsonHandler
{
    private List<CategoryInfo> cataInfoList = new ArrayList<>();

    public List<CategoryInfo> getCataInfoList()
    {
        return cataInfoList;
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
                    CategoryInfo mCataInfo = new CategoryInfo(arr.get(i).toString());
                    cataInfoList.add(mCataInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
