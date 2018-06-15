package com.xily.kotlinweather.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxBus private constructor() {
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

    companion object {
        @Volatile
        private var mInstance: RxBus? = null

        /**
         * 单例模式RxBus2
         */
        val instance: RxBus
            get() {
                var rxBus = mInstance
                if (mInstance == null) {
                    synchronized(RxBus::class.java) {
                        rxBus = mInstance
                        if (mInstance == null) {
                            rxBus = RxBus()
                            mInstance = rxBus
                        }
                    }
                }
                return rxBus!!
            }
    }
}
