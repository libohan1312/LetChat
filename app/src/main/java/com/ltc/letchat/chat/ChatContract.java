package com.ltc.letchat.chat;

import com.ltc.letchat.base.BasePresenter;
import com.ltc.letchat.base.BaseView;

public interface ChatContract {
    public interface View extends BaseView<Presenter>{
        void showMessage(boolean success,String from, String url,String msg);
        void afterSendMessage(boolean success);
    }

    public interface Presenter extends BasePresenter{
        boolean sendMessage(String userId, String msg);
    }
}
