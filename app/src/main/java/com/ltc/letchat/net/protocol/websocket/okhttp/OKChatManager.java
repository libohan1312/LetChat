package com.ltc.letchat.net.protocol.websocket.okhttp;

import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.protocol.websocket.ChatManagerWS;

import java.net.URISyntaxException;
import java.util.Map;

public class OKChatManager extends ChatManagerWS {
    protected OKChatManager(Map<String, String> heads) throws URISyntaxException {
        super(heads);
    }

    @Override
    protected void init(Map<String, String> heads) {

    }

    @Override
    public boolean isConnect() {
        return false;
    }

    @Override
    public void connect() {

    }

    @Override
    public void close() {

    }

    @Override
    public void sendMsg(String msg) {

    }

    @Override
    public void getContacts(OnGetContactsListener listener) {

    }
}
