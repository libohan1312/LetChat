package com.ltc.letchat.net.websocket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ltc.letchat.Constant;
import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.request.GetContacts;
import com.ltc.letchat.net.response.BaseResponse;
import com.ltc.letchat.util.Utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ChatManagerWS implements IChat {
    private Handler handler = new Handler(Looper.myLooper());
    private WebSocketClient client;
    private OnReceiveMsgListener listener;
    private OnGetContactsListener getContactsListener;
    private OnConnectOpen onConnectOpen;

    private URI serverUri;

    private Draft draft = new Draft_10();

    private static ChatManagerWS instance;

    public static ChatManagerWS init(Map<String,String> heads) throws URISyntaxException {
        if(instance == null){
            synchronized (ChatManagerWS.class){
                if(instance == null){
                    instance = new ChatManagerWS(heads);
                }
            }
        }
        return instance;
    }

    public boolean isConnect() {
        return client.getConnection().isOpen();
    }

    private ChatManagerWS(Map<String,String> heads) throws URISyntaxException {
        serverUri = new URI(Constant.serverUri);

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
