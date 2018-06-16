package com.xily.kotlinweather.model.network

import com.xily.kotlinweather.model.network.bean.VersionBean
import com.xily.kotlinweather.model.network.bean.WeatherBean
import io.reactivex.Observable


interface HttpHelper {

    fun checkVersion(): Observable<VersionBean>

    fun getWeather(cityId: String): Observable<WeatherBean>

}
