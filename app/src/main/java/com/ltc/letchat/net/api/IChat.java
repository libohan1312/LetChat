package com.ltc.letchat.net.api;

import com.ltc.letchat.contacts.data.Contact;

import org.java_websocket.handshake.ServerHandshake;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public interface IChat {
    void sendMsg(String msg);
    void receiveMsg(OnReceiveMsgListener listener);

    interface OnReceiveMsgListener{
        void onReceive(String uri,String msg);
    }

    void getContacts(OnGetContactsListener listener);

    void onOpen(OnConnectOpen onConnectOpen);

    interface OnGetContactsListener{
        void onContactReturn(List<Contact> contacts);
    }

    interface OnConnectOpen{
        void onOpen(ServerHandshake handshakedata);
    }
}
