package com.xily.kotlinweather.presenter

import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.CityContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.bean.CityListBean

import javax.inject.Inject

class CityPresenter @Inject
constructor(private val mDataManager: DataManager) : BasePresenter<CityContract.View>(), CityContract.Presenter {

    override var notificationId: Int
        get() = mDataManager.notificationId
        set(id) {
            mDataManager.notificationId = id
        }

    override val cityList: List<CityListBean>
        get() = mDataManager.cityList

    override fun deleteCity(id: Int) {
        mDataManager.deleteCity(id)
    }

    override fun setAutoUpdate(autoUpdate: Boolean) {
        mDataManager.autoUpdate = autoUpdate
    }

    override fun setNotification(notification: Boolean) {
        mDataManager.notification = notification
    }

    override fun addCity(cityListBean: CityListBean): CityListBean {
        val newCity = CityListBean(cityName = cityListBean.cityName,
                updateTime = cityListBean.updateTime,
                updateTimeStr = cityListBean.updateTimeStr,
                weatherData = cityListBean.weatherData,
                weatherId = cityListBean.weatherId
        )
        newCity.save()
        return newCity
    }
}
