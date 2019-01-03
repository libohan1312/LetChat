package com.ltc.letchat.net;

import com.ltc.letchat.Constant;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.protocol.websocket.okhttp.OKChatManager;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private static IChat chatManager;

    public static void initChatManager() {
        try {
            Map<String,String> heads = new HashMap<>();
            heads.put(Constant.USERID,Constant.userid);

//            chatManager = JWChatManager.getInstance(heads);
            chatManager = OKChatManager.getInstance(heads);

            chatManager.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reConnect(){
        Map<String,String> heads = new HashMap<>();
        heads.put(Constant.USERID,Constant.userid);
        chatManager.reConnect(heads);
    }

    public static IChat getChatManager() {
        if(chatManager == null){
            initChatManager();
        }
        return chatManager;
    }
}
