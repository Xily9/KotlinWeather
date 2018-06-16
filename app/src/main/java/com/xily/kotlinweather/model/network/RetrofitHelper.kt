package com.xily.kotlinweather.model.network

import com.xily.kotlinweather.model.network.api.WeatherApi
import com.xily.kotlinweather.model.network.bean.VersionBean
import com.xily.kotlinweather.model.network.bean.WeatherBean
import io.reactivex.Observable
import javax.inject.Inject


class RetrofitHelper @Inject
constructor(private val weatherApi: WeatherApi) : HttpHelper {

    override fun checkVersion(): Observable<VersionBean> {
        return weatherApi.checkVersion()
    }

    override fun getWeather(cityId: String): Observable<WeatherBean> {
        return weatherApi.getWeather(cityId)
    }
}
