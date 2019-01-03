package com.ltc.letchat.net.api;

import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.response.TalkResponse;

import java.util.List;
import java.util.Map;

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

    void onOpen();

    void reConnect(Map<String,String> header);

    interface OnGetContactsListener{
        void onContactReturn(List<Contact> contacts);
    }

}
