package com.ltc.letchat.net.protocol.websocket.javawebsocket;

import android.util.Log;

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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/30.
 */
public class JWChatManager extends ChatManagerWS {

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
                    String type = Utils.getProtocalType(message);
                    if(BaseResponse.TYPE_GETCONTACTS_RESP.equals(type)){
                        List<Contact> constants = Utils.getContacts(message);
                        getContactsListener.onContactReturn(constants);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(listener != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onReceive(getURI().getHost(),message);
                        }
                    });
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e("close",code+" "+reason+" "+remote);
            }

            @Override
            public void onError(Exception ex) {
                Log.e("on error",ex.getMessage());
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
    public void sendMsg(String msg) {
        client.send(msg);
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


        try {
            this.getContactsListener = listener;
            String getContactsProtocol = Utils.objectToJson(new GetContacts());
            client.send(getContactsProtocol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(OnConnectOpen onConnectOpen) {
        this.onConnectOpen = onConnectOpen;
    }
}
