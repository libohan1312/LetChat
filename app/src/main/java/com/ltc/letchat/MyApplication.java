package com.ltc.letchat;

import android.app.Application;
import android.util.Log;

import com.ltc.letchat.RxBus.RxBus;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.protocol.websocket.javawebsocket.JWChatManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.java_websocket.handshake.ServerHandshake;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/31.
 */
public class MyApplication extends Application {

    private static IChat chatManager;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
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

    @Subscribe
    public void receiveAll(String msg){
        Log.e("all received",msg);
    }

    public static IChat getChatManager() {
        return chatManager;
    }

}
