package com.ltc.letchat.net.protocol.websocket.okhttp;

import android.util.Log;

import com.ltc.letchat.RxBus.RxBus;
import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.protocol.websocket.ChatManagerWS;
import com.ltc.letchat.net.request.GetContacts;
import com.ltc.letchat.net.response.BaseResponse;
import com.ltc.letchat.net.response.TalkResponse;
import com.ltc.letchat.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.reactivex.functions.Consumer;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class OKChatManager extends ChatManagerWS {
    private WebSocket webSocket;
    public static ChatManagerWS getInstance(Map<String,String> heads) throws URISyntaxException {
        if (instance == null) {
            synchronized (ChatManagerWS.class){
                if (instance == null) {
                    instance = new OKChatManager(heads);
                }
            }
        }
        return instance;
    }

    private OKChatManager(Map<String, String> heads) throws URISyntaxException {
        super(heads);
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Request request = null;
        try {
            request = new Request.Builder().url(serverUri.toString()).headers(Headers.of(heads)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(request == null){
            return;
        }
        httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                OKChatManager.this.webSocket = webSocket;
                OKChatManager.this.onOpen();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    EventBus.getDefault().post(text);
                    BaseResponse baseResponse = Utils.getBaseResponsByJson(text);
                    if(baseResponse == null){
                        return;
                    }
                    String type = baseResponse.getType();
                    if(BaseResponse.TYPE_GETCONTACTS_RESP.equals(type)){
                        List<Contact> constants = Utils.getContacts(text);
                        if(getContactsListener == null) return;
                        getContactsListener.onContactReturn(constants);
                    }else if(BaseResponse.TYPE_TOKE_RESP.equals(type)){
                        TalkResponse response = Utils.jsonToObject(text,TalkResponse.class);
                        receiveMsg(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }
        });
    }


    @Override
    public boolean isConnect() {
//        String test = Utils.objectToJson(new ConnectTest());
        return webSocket != null; //&& webSocket.send(test)  这个先不加，感觉太费
    }

    @Override
    public void connect() {

    }

    @Override
    public void close() {
        webSocket.close(200,"manual");
    }

    @Override
    public boolean sendMsg(String msg) {
        if(!isConnect()){
            return false;
        }
        return webSocket.send(msg);
    }

    @Override
    public void getContacts(OnGetContactsListener listener) {
        if(webSocket == null){
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
        webSocket.send(getContactsProtocol);
    }
}
