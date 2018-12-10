package com.ltc.letchat;

import android.app.Application;
import android.util.Log;

import com.ltc.letchat.RxBus.RxBus;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.protocol.websocket.ChatManagerWS;
import com.ltc.letchat.net.protocol.websocket.javawebsocket.JWChatManager;

import org.java_websocket.handshake.ServerHandshake;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/31.
 */
public class MyApplication extends Application {

    private static ChatManagerWS chatManager;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Map<String,String> heads = new HashMap<>();
            heads.put(Constant.USERID,Constant.userid);

            chatManager = JWChatManager.getInstance(heads);
            chatManager.onOpen(new IChat.OnConnectOpen() {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    if(chatManager.isConnect()){
                        Log.e("connect","inninin");
                        RxBus.send(new JWChatManager.EventConnect());
                        //Toast.makeText(getApplicationContext(),"connect success",Toast.LENGTH_LONG).show();
                    }else {
                        Log.e("connect","nono");
                        //Toast.makeText(getApplicationContext(),"链接不成功",Toast.LENGTH_LONG).show();
                    }
                }
            });

            chatManager.receiveMsg(new IChat.OnReceiveMsgListener() {
                @Override
                public void onReceive(String uri, String msg) {
                    Log.e("all received",msg);
                }
            });

            chatManager.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ChatManagerWS getChatManager() {
        return chatManager;
    }

}
