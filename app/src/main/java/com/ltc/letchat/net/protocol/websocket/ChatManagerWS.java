package com.ltc.letchat.net.protocol.websocket;

import android.util.Log;

import com.ltc.letchat.Constant;
import com.ltc.letchat.RxBus.RxBus;
import com.ltc.letchat.event.ChatEvent;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.protocol.websocket.javawebsocket.JWChatManager;
import com.ltc.letchat.net.response.TalkResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/7/30.
 */
public abstract class ChatManagerWS implements IChat {
    public static final String EVENT_CONNECT = "event_connect";
    protected OnGetContactsListener getContactsListener;
    protected Disposable connectDisposable;
    protected URI serverUri;

    protected static ChatManagerWS instance;

    protected ChatManagerWS(Map<String,String> heads) throws URISyntaxException {
        serverUri = new URI(Constant.serverUri_test);
    }

    @Override
    public void receiveMsg(TalkResponse response) {
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.from = response.fromWho;
        chatEvent.msg = response.content;
        chatEvent.success = true;
        EventBus.getDefault().post(chatEvent);
    }

    @Override
    public void onOpen() {
        if(isConnect()){
            Log.e("connect","inninin");
            RxBus.send(new JWChatManager.EventConnect());
        }else {
            Log.e("connect","nono");
        }
    }
}
