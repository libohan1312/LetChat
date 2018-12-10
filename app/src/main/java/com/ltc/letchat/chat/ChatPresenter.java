package com.ltc.letchat.chat;

import android.content.Context;

import com.ltc.letchat.MyApplication;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.request.Talk;
import com.ltc.letchat.util.Utils;

public class ChatPresenter implements ChatContract.Presenter {
    ChatContract.View view;
    Context context;
    String userId;
    public ChatPresenter(Context context , ChatContract.View view, String userId){
        this.view = view;
        this.userId = userId;
        view.onSetPresenter(this);
        MyApplication.getChatManager().receiveMsg(new IChat.OnReceiveMsgListener() {
            @Override
            public void onReceive(String uri,String msg) {
                view.showMessage(true,userId,uri,msg);
            }
        });
    }
    @Override
    public boolean sendMessage(String userId, String msg) {
        try {
            Talk talk = new Talk();
            talk.setToWho(userId);
            talk.setContent(msg);
            String json = Utils.objectToJson(talk);
            boolean success = MyApplication.getChatManager().sendMsg(json);
            view.showMessage(success,"me",null,msg);
            view.afterSendMessage(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
