package com.zb.wyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zb.wyd.R;
import com.zb.wyd.entity.ChatInfo;
import com.zb.wyd.holder.chat.ChatBaseHolder;
import com.zb.wyd.holder.chat.LogChatHolder;
import com.zb.wyd.holder.chat.MyChatHolder;
import com.zb.wyd.holder.chat.OtherChatHolder;
import com.zb.wyd.holder.chat.SystemChatHolder;
import com.zb.wyd.holder.chat.UserChatHolder;
import com.zb.wyd.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class MyChatChatAdapter extends RecyclerView.Adapter<ChatBaseHolder>
{

    private MyItemClickListener listener;
    private List<ChatInfo> list;
    private Context mContext;

    public MyChatChatAdapter(List<ChatInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ChatBaseHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ChatBaseHolder mChatHolder = null;

        switch (viewType)
        {
            case 0:
                mChatHolder = new MyChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_my, parent, false), parent.getContext(), listener);
                break;
            case 1:
                mChatHolder = new OtherChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other_user, parent, false), parent.getContext(), listener);
                break;


            default:
                mChatHolder = new OtherChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_my, parent, false), parent.getContext(), listener);
                break;
        }
        return mChatHolder;
    }


    @Override
    public void onBindViewHolder(ChatBaseHolder holder, int position)
    {
        holder.setChatInfo(list.get(position), position);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        if (list.get(position).isIs_mine())
        {
            return 0;
        }
        else
        {
            return 1;
        }

    }
}
