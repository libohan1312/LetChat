package com.ltc.letchat.net.protocol.websocket.javawebsocket;

import android.util.Log;

import com.ltc.letchat.RxBus.RxBus;
import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.protocol.websocket.ChatManagerWS;
import com.ltc.letchat.net.request.GetContacts;
import com.ltc.letchat.net.response.BaseResponse;
import com.ltc.letchat.util.Utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import java.io.IOException;
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
    public static final String EVENT_CONNECT = "event_connect";
    public static class EventConnect extends RxBus.Event{
        public EventConnect(){
            type = EVENT_CONNECT;
        }
        @Override
        public Object getData() {
            return null;
        }
    }
    WebSocketClient client;
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

    private Disposable connectDisposable;

    public boolean isConnect() {
        return client.getConnection().isOpen();
    }

    private JWChatManager(Map<String,String> heads) throws URISyntaxException {
        super(heads);
    }

    @Override
    protected void init(Map<String,String> heads) {
        client = new WebSocketClient(serverUri,draft,heads,2000) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                onConnectOpen.onOpen(handshakedata);
            }

            @Override
            public void onMessage(String message) {

                try {
                    String type = Utils.getProtocolType(message);
                    if(BaseResponse.TYPE_GETCONTACTS_RESP.equals(type)){
                        List<Contact> constants = Utils.getContacts(message);
                        if(getContactsListener == null) return;
                        getContactsListener.onContactReturn(constants);
                        return;
                    }else if(BaseResponse.TYPE_TOKE_RESP.equals(type)){
                        if(listener != null){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onReceive(getURI().getHost(),message);
                                }
                            });
                        }
                        return;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                Log.d("receiveMsgNoHandle",message);
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
    public void receiveMsg(OnReceiveMsgListener listener) {
        this.listener = listener;
    }

    @Override
    public void getContacts(OnGetContactsListener listener) {

        if(client == null){
            Log.e("get contacts error","client is null");
            return;
        }
        if(!isConnect()){
            Log.e("requesterror","request should after connect");
            connectDisposable = RxBus.bus().subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) {
                    if(o instanceof RxBus.Event){
                        RxBus.Event event = (RxBus.Event) o;
                        if(EVENT_CONNECT.equals(event.type)){
                            getContactsImpl(listener);
                        }
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

    @Override
    public void onOpen(OnConnectOpen onConnectOpen) {
        this.onConnectOpen = onConnectOpen;
    }
}
