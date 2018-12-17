package com.ltc.letchat.recentchat;/**
 * Created by Administrator on 2016/8/17.
 */

import com.ltc.letchat.base.BasePresenter;
import com.ltc.letchat.base.BaseView;
import com.ltc.letchat.event.ChatEvent;

import java.util.List;

/**
 *
 */
public interface RecentChatContract {
    interface View extends BaseView<Presenter>{

        void gotoChatView(String chatWith);

        void showRecentChat(List<RecentItem> recentItems);

        void setLoadingIndicator(final boolean active);

        void showNoChatView();

        void showNewChat(RecentItem item);

        void showNoNet();

        void showDisconnect();

        void showReConnect();
    }

    interface Presenter extends BasePresenter{

        void loadRecentChat();

        void onGetNewMsg(ChatEvent event);

        void deleteChat();

        void remendNewMsg();

    }
}
