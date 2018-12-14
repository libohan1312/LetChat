package com.ltc.letchat.recentchat;

import android.content.Context;

import com.ltc.letchat.R;
import com.ltc.letchat.event.ChatEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class RecentChatPresenter implements RecentChatContract.Presenter {
    private RecentChatContract.View recentChatView;
    private Context context;

    public RecentChatPresenter(Context context,RecentChatContract.View view){
        recentChatView = view;
        recentChatView.onSetPresenter(this);
        this.context = context;
    }

    @Override
    public void loadRecentChat() {
        List<RecentItem> recentItems = new ArrayList<>();
        for(int i=0;i<2;i++) {
            RecentItem chatItem = new RecentItem();
            if(i%2==0){
                chatItem.head = context.getResources().getDrawable(R.drawable.me);
                chatItem.recentName = "阿凡达";
                chatItem.recentTemp = "fdadfadfasdffadsfasfafdafafasdfasfasfasfasfasfdasfasdff";
            }else {
                chatItem.head = context.getResources().getDrawable(R.drawable.others);
                chatItem.recentName = "狗日阿灿";
                chatItem.recentTemp = "法拉伐风景饿啦放假fadsfasf啊了放假啊ffadsfasdfsfdafadfasf了";
            }
            recentItems.add(chatItem);
        }
        recentChatView.showRecentChat(recentItems);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNewMsg(ChatEvent event) {
        RecentItem item = new RecentItem();
        item.head = context.getResources().getDrawable(R.drawable.others);
        item.userId = event.from;
        item.recentName = event.from;
        item.recentTemp = event.msg;
        recentChatView.showNewChat(item);
    }

    @Override
    public void deletChat() {

    }

    @Override
    public void remendNewMsg() {

    }

    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }
}
