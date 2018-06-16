package com.xily.kotlinweather.model.network.api

import com.xily.kotlinweather.model.network.bean.VersionBean
import com.xily.kotlinweather.model.network.bean.WeatherBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(ApiConfig.myApiUrl + "checkVersion2")
    fun checkVersion(): Observable<VersionBean>

    @GET(ApiConfig.meiZuApiUrl + "listWeather")
    fun getWeather(@Query("cityIds") cityId: String): Observable<WeatherBean>

}
