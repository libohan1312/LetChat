package com.ltc.letchat.net.protocol.websocket.javawebsocket;

import android.util.Log;

import com.ltc.letchat.RxBus.RxBus;
import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.event.ChatEvent;
import com.ltc.letchat.net.protocol.websocket.ChatManagerWS;
import com.ltc.letchat.net.request.GetContacts;
import com.ltc.letchat.net.response.BaseResponse;
import com.ltc.letchat.net.response.TalkResponse;
import com.ltc.letchat.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2016/7/30.
 */
public class JWChatManager extends ChatManagerWS {
    protected Draft draft = new Draft_10();

    public static class EventConnect extends RxBus.Event{
        public EventConnect(){
            type = EVENT_CONNECT;
        }
        @Override
        public Object getData() {
            return null;
        }
    }
    private WebSocketClient client;
    public static ChatManagerWS getInstance(Map<String,String> heads) throws URISyntaxException {
        if(instance == null){
            synchronized (ChatManagerWS.class){
                if(instance == null){
                    instance = new JWChatManager(heads);
                }
            }
        }
        return instance;
    }

    private JWChatManager(Map<String,String> heads) throws URISyntaxException {
        super(heads);
        init(heads);
    }

    protected void init(Map<String,String> heads) {
        client = new WebSocketClient(serverUri,draft,heads,2000) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                JWChatManager.this.onOpen();
            }

            @Override
            public void onMessage(String message) {
                try {
                    EventBus.getDefault().post(message);
                    BaseResponse baseResponse = Utils.getBaseResponsByJson(message);
                    if(baseResponse == null){
                        return;
                    }
                    String type = baseResponse.getType();
                    if(BaseResponse.TYPE_GETCONTACTS_RESP.equals(type)){
                        List<Contact> constants = Utils.getContacts(message);
                        if(getContactsListener == null) return;
                        getContactsListener.onContactReturn(constants);
                    }else if(BaseResponse.TYPE_TOKE_RESP.equals(type)){
                        TalkResponse response = Utils.jsonToObject(message,TalkResponse.class);
                        receiveMsg(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e("close",code+" "+reason+" "+remote);
            }

            @Override
            public void onError(Exception ex) {
                //todo not do this
                Log.e("onerror",ex.getMessage());
                client = null;
            }
        };
    }

    public void connect(){
        client.connect();
    }
    public boolean isConnect() {
        return client.getConnection().isOpen();
    }
    public void close(){
        client.close();
    }

    @Override
    public boolean sendMsg(String msg) {
        try {
            if (!isConnect()) {
                return false;
            }
            client.send(msg);
            return true;
        } catch (NotYetConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void getContacts(OnGetContactsListener listener) {

        if(client == null){
            Log.e("get contacts error","client is null");
            return;
        }
        if(!isConnect()){
            Log.e("requesterror","request should after connect");
            connectDisposable = RxBus.bus().subscribe(new Consumer<RxBus.Event>() {
                @Override
                public void accept(RxBus.Event event) {
                    if (EVENT_CONNECT.equals(event.type)) {
                        getContactsImpl(listener);
                    }
                }

            });
            return;
        }else if(connectDisposable != null){
            connectDisposable.dispose();
        }

        try {
            getContactsImpl(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getContactsImpl(OnGetContactsListener listener) {
        this.getContactsListener = listener;
        String getContactsProtocol = Utils.objectToJson(new GetContacts());
        client.send(getContactsProtocol);
    }

}
