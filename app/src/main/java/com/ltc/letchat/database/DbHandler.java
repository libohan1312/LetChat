package com.ltc.letchat.database;

import android.content.Context;

import io.objectbox.Box;

public interface DbHandler {
    void init(Context context);
    <T> Box<T> getEntity(Class<T> tClass);
}
