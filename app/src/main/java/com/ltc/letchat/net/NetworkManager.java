package com.ltc.letchat.net;

import android.util.Log;

import com.ltc.letchat.Constant;
import com.ltc.letchat.RxBus.RxBus;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.protocol.websocket.javawebsocket.JWChatManager;

import org.java_websocket.handshake.ServerHandshake;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private static IChat chatManager;

    public static void initChatManager() {
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
                    }else {
                        Log.e("connect","nono");
                    }
                }
            });

            chatManager.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IChat getChatManager() {
        if(chatManager == null){
            initChatManager();
        }
        return chatManager;
    }
}
