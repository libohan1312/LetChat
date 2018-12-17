package com.ltc.letchat.recentchat;

import android.content.Context;

import com.ltc.letchat.R;
import com.ltc.letchat.database.DbManager;
import com.ltc.letchat.database.Entity.ChatEntity;
import com.ltc.letchat.database.Entity.RecentEntity;
import com.ltc.letchat.database.Entity.RecentEntity_;
import com.ltc.letchat.event.ChatEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/24.
 */
public class RecentChatPresenter implements RecentChatContract.Presenter {
    private RecentChatContract.View recentChatView;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RecentChatPresenter(Context context,RecentChatContract.View view) {
        recentChatView = view;
        recentChatView.onSetPresenter(this);
        this.context = context;
    }

    @Override
    public void loadRecentChat() {
        Disposable disposable =  Flowable
                .fromArray(DbManager.getEntity(RecentEntity.class).getAll().toArray(new RecentEntity[]{}))
                .map(recentEntity -> {
                    RecentItem item = new RecentItem();
                    item.success = recentEntity.success;
                    item.recentName = recentEntity.recentName;
                    item.recentTemp = recentEntity.content;
                    item.userId = recentEntity.recentName;
                    item.head = context.getResources().getDrawable(R.drawable.others);
                    return item;
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recentItemList -> {
                    recentChatView.showRecentChat(recentItemList);
                });

        compositeDisposable.add(disposable);

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
        item.success = event.success;
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
            entity.success = chatEvent.success;
            return entity;
        }).map(recentEntity -> {
            RecentEntity find = DbManager.getEntity(RecentEntity.class)
            .query()
            .equal(RecentEntity_.recentName, recentEntity.recentName)
            .build()
            .findFirst();
            if (find != null) {
                recentEntity.id = find.id;
            }
            return DbManager.getEntity(RecentEntity.class).put(recentEntity);
        }).subscribeOn(Schedulers.io())
                .subscribe(id -> {
                DbManager.LogDP("put_recent:"+id);
            });
        compositeDisposable.add(disposable);
        Disposable disposable2 = Flowable.just(item).map(chatEvent -> {
            ChatEntity entity = new ChatEntity();
            entity.content = chatEvent.msg;
            entity.toWho = chatEvent.to;
            entity.fromWho = chatEvent.from;
            entity.time = System.currentTimeMillis();
            entity.success = chatEvent.success;
            return entity;
        }).map(chatEntity -> {
            return DbManager.getEntity(ChatEntity.class).put(chatEntity);
        }).subscribeOn(Schedulers.io())
          .subscribe(id -> {
               DbManager.LogDP("put_chat:"+id);
          });
        compositeDisposable.add(disposable2);
    }

    @Override
    public void deleteChat() {

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
