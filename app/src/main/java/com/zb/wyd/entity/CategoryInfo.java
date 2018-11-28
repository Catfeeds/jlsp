package com.zb.wyd.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 描述：一句话简单描述
 */
public class CategoryInfo implements Serializable
{
    private String id;

    private String  name;

    private boolean isSelected;


    public CategoryInfo(){}

    public CategoryInfo(JSONObject obj)
    {
        //this.id = obj.optString("pid");
        this.name = obj.toString();
    }
    public CategoryInfo(String name)
    {
        //this.id = obj.optString("pid");
        this.name = name;
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }
}
