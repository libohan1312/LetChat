package com.ltc.letchat;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.websocket.ChatManagerWS;

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

            chatManager = ChatManagerWS.init(heads);
            chatManager.onOpen(new IChat.OnConnectOpen() {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    if(chatManager.isConnect()){
                        Log.e("connect","inninin");

                        //Toast.makeText(getApplicationContext(),"connect success",Toast.LENGTH_LONG).show();
                    }else {
                        Log.e("connect","nono");
                        //Toast.makeText(getApplicationContext(),"链接不成功",Toast.LENGTH_LONG).show();
                    }
                }
            });
            chatManager.connect();

            chatManager.receiveMsg(new IChat.OnReceiveMsgListener() {
                @Override
                public void onReceive(String uri, String msg) {
                    Log.e("all received",msg);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ChatManagerWS getChatManager() {
        return chatManager;
    }

}
