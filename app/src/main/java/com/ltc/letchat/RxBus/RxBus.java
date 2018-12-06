package com.ltc.letchat.RxBus;

import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2017/1/2.
 */

public class RxBus {

    Subject<Object,Object> bus = new SerializedSubject<>(PublishSubject.create());



}
