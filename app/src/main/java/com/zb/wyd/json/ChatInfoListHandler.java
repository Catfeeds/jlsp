package com.zb.wyd.json;


import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.entity.LiveInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ChatInfoListHandler extends JsonHandler
{
    private List<ChatInfo> chatInfoList = new ArrayList<>();

    public List<ChatInfo> getChatInfoList()
    {
        return chatInfoList;
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
                    ChatInfo mChatInfo = new ChatInfo(arr.optJSONObject(i));
                    chatInfoList.add(mChatInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
