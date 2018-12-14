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
    protected OnGetContactsListener getContactsListener;
    protected OnConnectOpen onConnectOpen;

    protected URI serverUri;

    protected static ChatManagerWS instance;

    protected ChatManagerWS(Map<String,String> heads) throws URISyntaxException {
        serverUri = new URI(Constant.serverUri_test);
    }

    @Override
    public void onOpen(OnConnectOpen onConnectOpen) {
        this.onConnectOpen = onConnectOpen;
    }
}
