package com.ltc.letchat.recentchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ltc.letchat.R;
import com.ltc.letchat.chat.ChatActivity;

import java.util.List;

public class RecentChatFragment extends Fragment implements RecentChatContract.View{

    RecentListAdapter chatListAdapter;
    RecyclerView recyclerView;
    LinearLayout noRecentLayout;

    RecentChatContract.Presenter recentChatPresenter;

    public static RecentChatFragment newInstance() {
        RecentChatFragment fragment = new RecentChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        recentChatPresenter.loadRecentChat();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.chatrecentlist,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recentlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatListAdapter = new RecentListAdapter();
        recyclerView.setAdapter(chatListAdapter);

        noRecentLayout = (LinearLayout) view.findViewById(R.id.no_recent_layout);
        noRecentLayout.setVisibility(View.GONE);

        chatListAdapter.notifyDataSetChanged();
        chatListAdapter.listener = new RecentListAdapter.ViewHolder.OnChatItemClickListener() {
            @Override
            public void onClick(View v, RecentItem item) {
                gotoChatView(item.recentName);
            }
        };
        return view;
    }

    @Override
    public void gotoChatView(String chatWith) {
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("name",chatWith);
        startActivity(intent);
    }

    @Override
    public void showRecentChat(List<RecentItem> recentItems) {
        chatListAdapter.datas = recentItems;
        chatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showNoChatView() {
        recyclerView.setVisibility(View.GONE);
        noRecentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNewChat() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void showDisconnect() {

    }

    @Override
    public void showReConnect() {

    }

    @Override
    public void setPresenter(@NonNull RecentChatContract.Presenter presenter) {
        recentChatPresenter = presenter;
    }
}
