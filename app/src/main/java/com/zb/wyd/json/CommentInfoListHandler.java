package com.zb.wyd.json;


import com.zb.wyd.entity.AdInfo;
import com.zb.wyd.entity.CommentInfo;
import com.zb.wyd.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CommentInfoListHandler extends JsonHandler
{
    private List<CommentInfo> commentInfoList = new ArrayList<>();

    public List<CommentInfo> getCommentInfoList()
    {
        return commentInfoList;
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
                    CommentInfo mCommentInfo = new CommentInfo(arr.optJSONObject(i));

                    if(null !=arr.optJSONObject(i))
                    {
                        UserInfo userInfo = new UserInfo(arr.optJSONObject(i).optJSONObject("userinfo"));
                        mCommentInfo.setUserInfo(userInfo);
                    }


                    commentInfoList.add(mCommentInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
