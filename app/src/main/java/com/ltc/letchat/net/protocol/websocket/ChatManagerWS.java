package com.ltc.letchat.net.protocol.websocket;

import android.os.Handler;
import android.os.Looper;

import com.ltc.letchat.Constant;
import com.ltc.letchat.net.api.IChat;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/30.
 */
public abstract class ChatManagerWS implements IChat {
    protected Handler handler = new Handler(Looper.myLooper());
    protected List<OnReceiveMsgListener> receiveMsgListeners;
    protected OnGetContactsListener getContactsListener;
    protected OnConnectOpen onConnectOpen;

    protected URI serverUri;

    protected Draft draft = new Draft_10();

    protected static ChatManagerWS instance;

    protected abstract void init(Map<String,String> heads);
    public abstract boolean  isConnect();

    protected ChatManagerWS(Map<String,String> heads) throws URISyntaxException {
        serverUri = new URI(Constant.serverUri_test);
        receiveMsgListeners = new ArrayList<>();
        init(heads);
    }

    public abstract void connect();

    public abstract void close();

    @Override
    public abstract boolean sendMsg(String msg);

    @Override
    public void receiveMsg(OnReceiveMsgListener listener) {
        this.receiveMsgListeners.add(listener);
    }

    @Override
    public abstract void getContacts(OnGetContactsListener listener);

    @Override
    public void onOpen(OnConnectOpen onConnectOpen) {
        this.onConnectOpen = onConnectOpen;
    }
}
