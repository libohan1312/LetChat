package com.ltc.letchat.RxBus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Administrator on 2017/1/2.
 */

public class RxBus {

    private static PublishSubject<Object> bus = PublishSubject.create();

    public static Observable<Object> bus(){
        return bus;
    }

    public static <T extends Event> void send(T t){
        bus.onNext(t);

    }

    public static abstract class Event{
        public String type;
        public abstract Object getData();
    }
}
