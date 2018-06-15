package com.xily.kotlinweather.contract

import com.xily.kotlinweather.base.IBasePresenter
import com.xily.kotlinweather.base.IBaseView
import com.xily.kotlinweather.model.network.bean.WeatherBean

interface PagerContract {
    interface View : IBaseView {
        fun showWeather(weatherBean: WeatherBean)

        fun setRefreshing(isRefreshing: Boolean)

        fun setUpdateTime(updateTime: String)

        fun setTitle(title: String)

        fun sendBroadcast()
    }

    interface Presenter : IBasePresenter {

        val cityId: Int
        fun getWeather(isRefreshing: Boolean)

        fun getCityInfo(position: Int)

        fun getSetTitleUpdateTime()
    }
}
