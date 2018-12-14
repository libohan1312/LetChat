package com.ltc.letchat.net.api;

import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.response.TalkResponse;

import org.java_websocket.handshake.ServerHandshake;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public interface IChat {
    void connect();
    boolean  isConnect();
    void close();
    boolean sendMsg(String msg);
    void receiveMsg(TalkResponse talkResponse);

    void getContacts(OnGetContactsListener listener);

    void onOpen(OnConnectOpen onConnectOpen);

    interface OnGetContactsListener{
        void onContactReturn(List<Contact> contacts);
    }

    interface OnConnectOpen{
        void onOpen(ServerHandshake handshakedata);
    }
}
