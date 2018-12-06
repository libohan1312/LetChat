package com.ltc.letchat.recentchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ltc.letchat.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/7/19.
 */
public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.ViewHolder>{
    List<RecentItem> datas;

    public ViewHolder.OnChatItemClickListener listener;

    public RecentListAdapter(){
        datas = new ArrayList<>();
    }

    public void addItem(RecentItem item){
        datas.add(item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatrecnetitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        if(listener != null)
        viewHolder.listener = listener;
        viewHolder.btListener = btClick;

        return viewHolder;
    }

    ViewHolder.OnChatItemClickListener btClick = new ViewHolder.OnChatItemClickListener() {
        @Override
        public void onClick(View v, RecentItem item) {
            datas.remove(item.postion);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecentItem item = datas.get(position);
        holder.head.setImageDrawable(item.head);
        holder.recentName.setText(item.recentName);
        holder.recentTemp.setText(item.recentTemp);
        holder.setPostion(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.head_recent)
        public ImageView head;
        @BindView(R.id.tv_recentname)
        public TextView recentName;
        @BindView(R.id.tv_recenttemp)
        public TextView recentTemp;
        @BindView(R.id.lo_recentltem)
        LinearLayout linearLayout;

        @BindView(R.id.bt_delete)
        Button button;

        int postion;

        public int getPostion() {
            return postion;
        }

        public void setPostion(int postion) {
            this.postion = postion;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        RecentItem item = new RecentItem();
                        item.recentTemp = recentTemp.getText().toString();
                        item.recentName = recentName.getText().toString();
                        item.head = head.getDrawable();
                        item.postion = postion;
                        listener.onClick(v,item);
                    }
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentItem item = new RecentItem();
                    item.recentTemp = recentTemp.getText().toString();
                    item.recentName = recentName.getText().toString();
                    item.head = head.getDrawable();
                    item.postion = postion;
                    btListener.onClick(v,item);
                }
            });



        }

        public interface OnChatItemClickListener {
            void onClick(View v,RecentItem item);
        }

        public OnChatItemClickListener listener;

        public OnChatItemClickListener btListener;
    }
}
