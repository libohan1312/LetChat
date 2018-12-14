package com.ltc.letchat.database;

import android.content.Context;
import android.util.Log;

import io.objectbox.Box;

public class DbManager {
    public static final String TAG = "dbLog";
    private static DbHandler dbHandler = new OBoxHandler();

    public static void setDbHandler(DbHandler handler){
        if(handler == null) return;
        dbHandler = handler;
    }

    public static void init(Context context){
        dbHandler.init(context);
    }

    public static  <T> Box<T> getEntity(Class<T> tClass){
        return dbHandler.getEntity(tClass);
    }

    public static void LogDP(String msg){
        Log.d(TAG,msg);
    }
}
