package com.xily.kotlinweather.presenter

import android.content.ContentValues
import android.text.TextUtils

import com.google.gson.Gson
import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.PagerContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.bean.CityListBean
import com.xily.kotlinweather.model.bean.WeatherBean
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal

import javax.inject.Inject

class PagerPresenter @Inject
constructor(private val mDataManager: DataManager) : BasePresenter<PagerContract.View>(), PagerContract.Presenter {

    private var cityList: CityListBean? = null

    override val cityId: Int
        get() = if (cityList != null) cityList!!.id else -1

    override fun getWeather(isRefreshing: Boolean) {
        if (cityList != null) {
            val offline = Observable.just(Unit)
                    .map {
                        val data = cityList!!.weatherData
                        if (isRefreshing || System.currentTimeMillis() - cityList!!.updateTime > 1000 * 60 * 60 || TextUtils.isEmpty(data)) {
                            Unit
                        } else {
                            Gson().fromJson(data, WeatherBean::class.java)
                        }
                    }
            val online = mDataManager
                    .getWeather(cityList!!.weatherId.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { mView!!.setRefreshing(true) }
                    .doFinally { mView!!.setRefreshing(false) }
                    .doOnNext { weatherInfo ->
                        val cityListUpdate = ContentValues()
                        cityListUpdate.put("weatherData", Gson().toJson(weatherInfo))
                        cityListUpdate.put("updateTime", System.currentTimeMillis())
                        cityListUpdate.put("updateTimeStr", weatherInfo.value!![0].realtime!!.time!!.substring(11, 16))
                        LitePal.update(CityListBean::class.java, cityListUpdate, cityList!!.id.toLong())
                        mView!!.setUpdateTime("${cityListUpdate.get("updateTimeStr")} 更新")
                        cityList = mDataManager.getCityById(cityList!!.id)
                        mView!!.sendBroadcast()
                    }
            Observable.concat(offline, online)
                    .filter { weatherInfo -> weatherInfo is WeatherBean }
                    .firstElement()
                    .toObservable()
                    .bindToLifecycle()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ weatherBean -> mView!!.showWeather(weatherBean as WeatherBean) }) { throwable ->
                        throwable.printStackTrace()
                        mView!!.showErrorMsg(throwable.message!!)
                    }
        }
    }

    override fun getCityInfo(position: Int) {
        val cityLists = mDataManager.cityList
        if (cityLists.size > position) {
            cityList = cityLists[position]
        }
    }

    override fun getSetTitleUpdateTime() {
        mView!!.setTitle(if (cityList != null) cityList!!.cityName else "")
        mView!!.setUpdateTime(if (cityList != null) cityList!!.updateTimeStr!! + "更新" else "")
    }
}
