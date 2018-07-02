package com.xily.kotlinweather.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

object RxBus {
    private val bus: Subject<Any> = PublishSubject.create<Any>().toSerialized()


    /**
     * 发送消息
     */
    fun post(`object`: Any) {
        bus.onNext(`object`)
    }


    /**
     * 接收消息
     */
    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return bus.ofType(eventType)
    }
}
