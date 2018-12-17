package com.ltc.letchat.chat;

import com.ltc.letchat.base.BasePresenter;
import com.ltc.letchat.base.BaseView;
import com.ltc.letchat.event.ChatEvent;

import java.util.List;

public interface ChatContract {
    interface View extends BaseView<Presenter>{
        void showMessage(ChatItem chatItem);
        void afterSendMessage(boolean success);
        void showAllMassage(List<ChatItem> chatItemList);
    }

    interface Presenter extends BasePresenter{
        boolean sendMessage(String userId, String msg);
        void onReceiveMsg(ChatEvent event);
        void loadAllChat();
    }
}
