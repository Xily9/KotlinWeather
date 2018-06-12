package com.xily.kotlinweather.rx

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object RxHelper {
    fun <T> applySchedulers(): Observable.Transformer<T, T> {
        return Observable.Transformer {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}
