package com.xily.kotlinweather.base

import com.xily.kotlinweather.base.IBaseView


interface IBasePresenter {

    fun attachView(view: IBaseView)

    fun detachView()
}
