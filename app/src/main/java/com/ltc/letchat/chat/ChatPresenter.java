package com.ltc.letchat.chat;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ltc.letchat.MyApplication;
import com.ltc.letchat.event.ChatEvent;
import com.ltc.letchat.net.request.Talk;
import com.ltc.letchat.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChatPresenter implements ChatContract.Presenter {
    ChatContract.View view;
    String userId;
    public ChatPresenter(Context context , ChatContract.View view, String userId){
        this.view = view;
        this.userId = userId;
        view.onSetPresenter(this);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(ChatEvent chatEvent){
        ChatItem chatItem = getChatItem(chatEvent);
        view.showMessage(chatItem);
    }

    @NonNull
    private ChatItem getChatItem(ChatEvent chatEvent) {
        ChatItem chatItem = new ChatItem();
        chatItem.content = chatEvent.msg;
        chatItem.isMe = "me".equals(chatEvent.from);
        chatItem.fromHow = chatEvent.from;
        chatItem.success = chatEvent.success;
        return chatItem;
    }

    @Override
    public boolean sendMessage(String userId, String msg) {
        try {
            boolean success = sendMsg(userId, msg);
            ChatEvent chatEvent = new ChatEvent();
            chatEvent.success = true;
            chatEvent.from = "me";
            chatEvent.msg = msg;
            ChatItem chatItem = getChatItem(chatEvent);
            view.showMessage(chatItem);
            view.afterSendMessage(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            view.afterSendMessage(false);
            return false;
        }
    }

    private boolean sendMsg(String userId, String msg) {
        Talk talk = new Talk();
        talk.setToWho(userId);
        talk.setContent(msg);
        String json = Utils.objectToJson(talk);
        return MyApplication.getChatManager().sendMsg(json);
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
