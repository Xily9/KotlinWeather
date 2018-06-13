package com.xily.kotlinweather.base

import com.trello.rxlifecycle2.LifecycleTransformer

interface IBaseView {
    fun showErrorMsg(msg: String)
    fun <T> bindToLifecycle(): LifecycleTransformer<T>
}
