package com.ltc.letchat.database;

import android.content.Context;

import com.ltc.letchat.database.Entity.MyObjectBox;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class OBoxHandler implements DbHandler {
    private BoxStore boxStore;
    @Override
    public void init(Context context) {
       boxStore = MyObjectBox.builder().androidContext(context).build();
    }

    @Override
    public <T> Box<T> getEntity(Class<T> tClass) {
        return boxStore.boxFor(tClass);
    }
}
