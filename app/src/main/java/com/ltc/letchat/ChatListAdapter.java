package com.ltc.letchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/17.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    ArrayList<ChatItem> datas;
    Context context;

    public ChatListAdapter(Context context){
        this.context = context;
        datas = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void addChat(ChatItem chatItem){
        datas.add(chatItem);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatItem chatItem = datas.get(position);
        if(chatItem.isMe) {
            holder.head_others.setVisibility(View.GONE);
            holder.head_me.setVisibility(View.VISIBLE);
            holder.text.setGravity(holder.text.getGravity()|Gravity.RIGHT);
            holder.text.setBackground(context.getDrawable(R.drawable.chat_bub_me));
        }else {
            holder.head_me.setVisibility(View.GONE);
            holder.head_others.setVisibility(View.VISIBLE);
            holder.text.setGravity(holder.text.getGravity()|Gravity.LEFT);
            holder.text.setBackground(context.getDrawable(R.drawable.chat_bub_other));
        }
        holder.text.setText(datas.get(position).content);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.head_me)
        public ImageView head_me;
        @BindView(R.id.head_others)
        public ImageView head_others;
        @BindView(R.id.textView)
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
