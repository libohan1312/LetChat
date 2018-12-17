package com.ltc.letchat.recentchat;

import android.content.Context;

import com.ltc.letchat.R;
import com.ltc.letchat.database.DbManager;
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

//        for(int i=0;i<2;i++) {
//            RecentItem chatItem = new RecentItem();
//            if(i%2==0){
//                chatItem.head = context.getResources().getDrawable(R.drawable.me);
//                chatItem.recentName = "阿凡达";
//                chatItem.recentTemp = "fdadfadfasdffadsfasfafdafafasdfasfasfasfasfasfdasfasdff";
//            }else {
//                chatItem.head = context.getResources().getDrawable(R.drawable.others);
//                chatItem.recentName = "狗日阿灿";
//                chatItem.recentTemp = "法拉伐风景饿啦放假fadsfasf啊了放假啊ffadsfasdfsfdafadfasf了";
//            }
//            recentItems.add(chatItem);
//        }

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
