package com.ltc.letchat.chat;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ltc.letchat.database.DbManager;
import com.ltc.letchat.database.Entity.ChatEntity;
import com.ltc.letchat.event.ChatEvent;
import com.ltc.letchat.net.NetworkManager;
import com.ltc.letchat.net.request.Talk;
import com.ltc.letchat.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatPresenter implements ChatContract.Presenter {
    ChatContract.View view;
    String userId;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
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

    @Override
    public void loadAllChat() {
        Disposable disposable = Flowable.fromArray(DbManager
                        .getEntity(ChatEntity.class)
                        .getAll()
                        .toArray(new ChatEntity[]{}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::getChatItem)
                .toList()
                .subscribe(chatItems -> {
                    view.showAllMassage(chatItems);
                });
        compositeDisposable.add(disposable);
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
    private ChatItem getChatItem(ChatEntity chatEntity){
        ChatItem chatItem = new ChatItem();
        chatItem.success = chatEntity.success;
        chatItem.fromHow = chatEntity.fromWho;
        chatItem.content = chatEntity.content;
        chatItem.isMe = "me".equals(chatEntity.fromWho);
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
            chatEvent.to = userId;
            view.afterSendMessage(success);
            //自己说话也要显示在最近聊天
            EventBus.getDefault().post(chatEvent);
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
        return NetworkManager.getChatManager().sendMsg(json);
    }

    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
        compositeDisposable.dispose();
    }
}
