package com.ltc.letchat.net.protocol.websocket.okhttp;

import com.ltc.letchat.net.protocol.websocket.ChatManagerWS;
import com.ltc.letchat.net.response.TalkResponse;

import java.net.URISyntaxException;
import java.util.Map;

public class OKChatManager extends ChatManagerWS {
    protected OKChatManager(Map<String, String> heads) throws URISyntaxException {
        super(heads);
    }

    @Override
    public void receiveMsg(TalkResponse talkResponse) {

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
    public boolean sendMsg(String msg) {
        return false;
    }

    @Override
    public void getContacts(OnGetContactsListener listener) {

    }
}
