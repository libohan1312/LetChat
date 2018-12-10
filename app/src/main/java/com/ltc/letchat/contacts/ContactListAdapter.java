package com.ltc.letchat.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ltc.letchat.Constant;
import com.ltc.letchat.R;
import com.ltc.letchat.contacts.data.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    List<Contact> datas = new ArrayList<>();

    public void setDatas(List<Contact> contacts){
        datas.clear();
        for (Contact contact : contacts) {
            if (!Constant.userid.equals(contact.getUserId())) {
                datas.add(contact);
            }
        }
        notifyDataSetChanged();
    }

    public void setContactItemClickListener(OnContactItemClickListener contactItemClickListener){
        listener = contactItemClickListener;
    }

    interface OnContactItemClickListener{
        void onClick(String userId);
    }

    OnContactItemClickListener listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClick(datas.get(viewHolder.getLayoutPosition()).getUserId());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.contactName.setText(datas.get(position).getUserId());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.contact_name)
        TextView contactName;

        @BindView(R.id.contactitemlayout)
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }
}
