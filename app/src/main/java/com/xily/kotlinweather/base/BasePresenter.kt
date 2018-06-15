package com.xily.kotlinweather.base

import io.reactivex.Observable

abstract class BasePresenter<T : IBaseView> : IBasePresenter {
    protected lateinit var mView: T

    override fun attachView(view: IBaseView) {
        @Suppress("UNCHECKED_CAST")
        mView = view as T
    }

    override fun detachView() {

    }

    fun <T> Observable<T>.bindToLifecycle(): Observable<T> {
        return compose(mView.bindToLifecycle())
    }
}
