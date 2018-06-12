package com.xily.kotlinweather.base

import rx.Observable

interface IBaseView {
    fun showErrorMsg(msg: String)

    fun <T> bindToLifecycle(): Observable.Transformer<T, T>
}
