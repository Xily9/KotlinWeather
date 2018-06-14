package com.xily.kotlinweather.presenter

import com.google.gson.Gson
import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.AlarmContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.bean.WeatherBean
import javax.inject.Inject

class AlarmPresenter @Inject
constructor(private val mDataManager: DataManager) : BasePresenter<AlarmContract.View>(), AlarmContract.Presenter {

    override fun getAlarms(cityId: Int) {
        val cityList = mDataManager.getCityById(cityId)
        if (cityList != null) {
            val weatherBean = Gson().fromJson<WeatherBean>(cityList.weatherData, WeatherBean::class.java)
            mView!!.show(weatherBean.value!![0].alarms!!)
        }
    }
}
