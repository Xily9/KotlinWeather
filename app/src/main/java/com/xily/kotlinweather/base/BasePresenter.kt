package com.xily.kotlinweather.base

abstract class BasePresenter<T : IBaseView> : IBasePresenter {
    protected var mView: T? = null

    override fun attachView(view: IBaseView) {
        @Suppress("UNCHECKED_CAST")
        mView = view as T
    }

    override fun detachView() {
        mView = null
    }
}
