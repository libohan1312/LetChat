package com.ltc.letchat.recentchat;

import android.content.Context;

import com.ltc.letchat.R;
import com.ltc.letchat.database.DbManager;
import com.ltc.letchat.database.Entity.ChatEntity;
import com.ltc.letchat.event.ChatEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/24.
 */
public class RecentChatPresenter implements RecentChatContract.Presenter {
    private RecentChatContract.View recentChatView;
    private Context context;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RecentChatPresenter(Context context,RecentChatContract.View view){
        recentChatView = view;
        recentChatView.onSetPresenter(this);
        this.context = context;
    }

    @Override
    public void loadRecentChat() {
        List<RecentItem> recentItems = new ArrayList<>();
//        List<ChatEntity> chatEntities = DbManager.getEntity(ChatEntity.class).query().;
//        Disposable disposable = Flowable.fromArray(chatEntities.toArray(new ChatEntity[]{})).map(chatEntity -> {
//            ChatEvent chatEvent = new ChatEvent();
//            chatEvent.from = chatEntity.fromWho;
//            chatEvent.to = chatEntity.toWho;
//            chatEvent.msg = chatEntity.content;
//            chatEvent.success = true;
//            return recentItem;
//        }).toList().subscribe( recentItems2 -> {
//                recentChatView.showRecentChat(recentItems2);
//            }
//        );
//        compositeDisposable.add(disposable);

        for(int i=0;i<2;i++) {
            RecentItem chatItem = new RecentItem();
            if(i%2==0){
                chatItem.head = context.getResources().getDrawable(R.drawable.me);
                chatItem.recentName = "阿凡达";
                chatItem.recentTemp = "fdadfadfasdffadsfasfafdafafasdfasfasfasfasfasfdasfasdff";
            }else {
                chatItem.head = context.getResources().getDrawable(R.drawable.others);
                chatItem.recentName = "狗日阿灿";
                chatItem.recentTemp = "法拉伐风景饿啦放假fadsfasf啊了放假啊ffadsfasdfsfdafadfasf了";
            }
            recentItems.add(chatItem);
        }
        recentChatView.showRecentChat(recentItems);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNewMsg(ChatEvent event) {
        RecentItem item = new RecentItem();
        item.head = context.getResources().getDrawable(R.drawable.others);
        item.userId = event.from;
        if("me".equals(event.from)){
            item.recentName = event.to;
        }else {
            item.recentName = event.from;
        }
        item.recentTemp = event.msg;
        recentChatView.showNewChat(item);
        saveMsg(event);
    }

    private void saveMsg(ChatEvent item){
        Disposable disposable = Flowable.just(item).map(chatEvent -> {
            ChatEntity entity = new ChatEntity();
            entity.content = chatEvent.msg;
            entity.fromWho = chatEvent.from;
            entity.toWho = chatEvent.to;
            entity.time = System.currentTimeMillis();
            return entity;
        }).map(chatEntity -> {
            return DbManager.getEntity(ChatEntity.class).put(chatEntity);
        }).subscribeOn(Schedulers.io()).subscribe(id -> {
                DbManager.LogDP("put:"+id);
            });
        compositeDisposable.add(disposable);
    }

    @Override
    public void deletChat() {

    }

    @Override
    public void remendNewMsg() {

    }

    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
        compositeDisposable.dispose();
    }
}
