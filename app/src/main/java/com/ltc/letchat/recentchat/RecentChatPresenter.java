package com.ltc.letchat.recentchat;

import android.content.Context;

import com.ltc.letchat.R;
import com.ltc.letchat.database.DbManager;
import com.ltc.letchat.database.Entity.ChatEntity;
import com.ltc.letchat.database.Entity.ChatEntity_;
import com.ltc.letchat.database.Entity.MyObjectBox;
import com.ltc.letchat.database.Entity.RecentEntity;
import com.ltc.letchat.database.Entity.RecentEntity_;
import com.ltc.letchat.event.ChatEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.objectbox.Property;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.QueryBuilder;
import io.objectbox.reactive.DataObserver;
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

        DbManager.getEntity(RecentEntity.class)
                .query()
                .order(RecentEntity_.time,QueryBuilder.DESCENDING)
                .build().find();

        Disposable disposable = Flowable.fromArray(chatEntities.toArray(new ChatEntity[]{})).map(chatEntity -> {
            ChatEvent chatEvent = new ChatEvent();
            chatEvent.from = chatEntity.fromWho;
            chatEvent.to = chatEntity.toWho;
            chatEvent.msg = chatEntity.content;
            chatEvent.success = true;
            return recentItem;
        }).toList().subscribe( recentItems2 -> {
                recentChatView.showRecentChat(recentItems2);
            }
        );
        compositeDisposable.add(disposable);

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
            RecentEntity entity = new RecentEntity();
            entity.content = chatEvent.msg;
            if("me".equals(chatEvent.from)){
                entity.recentName = chatEvent.to;
            }else {
                entity.recentName = chatEvent.from;
            }
            entity.time = System.currentTimeMillis();
            return entity;
        }).map(recentEntity -> {
            recentEntity.id = DbManager.getEntity(RecentEntity.class)
                    .query()
                    .equal(RecentEntity_.recentName,recentEntity.recentName)
                    .build()
                    .findFirst()
                    .id;
            return DbManager.getEntity(RecentEntity.class).put(recentEntity);
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
