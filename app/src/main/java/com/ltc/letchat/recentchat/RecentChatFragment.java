package com.ltc.letchat.recentchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ltc.letchat.R;
import com.ltc.letchat.base.BaseFragment;
import com.ltc.letchat.chat.ChatActivity;

import java.util.List;

public class RecentChatFragment extends BaseFragment implements RecentChatContract.View{

    RecentListAdapter chatListAdapter;
    RecyclerView recyclerView;
    LinearLayout noRecentLayout;

    RecentChatContract.Presenter recentChatPresenter;

    public static RecentChatFragment newInstance() {
        RecentChatFragment fragment = new RecentChatFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.chatrecentlist,null);
        recyclerView =  view.findViewById(R.id.recentlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatListAdapter = new RecentListAdapter();
        recyclerView.setAdapter(chatListAdapter);

        noRecentLayout = view.findViewById(R.id.no_recent_layout);
        noRecentLayout.setVisibility(View.GONE);

        chatListAdapter.notifyDataSetChanged();
        chatListAdapter.listener = (viewIn,recentItem) -> {
            gotoChatView(recentItem.recentName);
        };

        recentChatPresenter.loadRecentChat();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(recentChatPresenter == null){
            new RecentChatPresenter(getContext(),this);
        }
        recentChatPresenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recentChatPresenter.unsubscribe();
    }

    @Override
    public void gotoChatView(String chatWith) {
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("name",chatWith);
        intent.putExtra("userId",chatWith);
        startActivity(intent);
    }

    @Override
    public void showRecentChat(List<RecentItem> recentItems) {
        if (recentItems == null || recentItems.size() <= 0) {
            noRecentLayout.setVisibility(View.VISIBLE);
            return;
        }
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
    public void showNewChat(RecentItem item) {
        chatListAdapter.addItem(item);
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
    public void onSetPresenter(@NonNull RecentChatContract.Presenter presenter) {
        recentChatPresenter = presenter;
    }
}
