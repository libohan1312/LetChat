package com.ltc.letchat;

import android.app.Application;
import android.util.Log;

import com.ltc.letchat.database.DbManager;
import com.ltc.letchat.net.NetworkManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/7/31.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        NetworkManager.initChatManager();
        DbManager.init(this);
    }



    @Subscribe
    public void receiveAll(String msg){
        Log.e("all received",msg);
    }



}
