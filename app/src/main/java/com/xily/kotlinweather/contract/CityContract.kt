package com.xily.kotlinweather.contract

import com.xily.kotlinweather.base.IBasePresenter
import com.xily.kotlinweather.base.IBaseView
import com.xily.kotlinweather.model.bean.CityListBean

interface CityContract {
    interface View : IBaseView

    interface Presenter : IBasePresenter {

        var notificationId: Int

        val cityList: List<CityListBean>
        fun deleteCity(id: Int)

        fun setAutoUpdate(autoUpdate: Boolean)

        fun setNotification(notification: Boolean)

        fun addCity(cityListBean: CityListBean): CityListBean
    }
}
